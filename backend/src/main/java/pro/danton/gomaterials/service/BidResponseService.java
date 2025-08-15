package pro.danton.gomaterials.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pro.danton.gomaterials.model.BidRequest;
import pro.danton.gomaterials.model.BidRequestStatus;
import pro.danton.gomaterials.model.BidResponse;
import pro.danton.gomaterials.model.BidResponseStatus;
import pro.danton.gomaterials.model.ProfileType;
import pro.danton.gomaterials.model.Supplier;
import pro.danton.gomaterials.repository.BidRequestRepository;
import pro.danton.gomaterials.repository.BidResponseRepository;
import pro.danton.gomaterials.repository.SupplierRepository;

@Service
public class BidResponseService {
    @Autowired
    private BidResponseRepository bidResponseRepository;
    @Autowired
    private BidRequestRepository bidRequestRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SecurityService securityService;

    @Transactional
    public BidResponse submitBid(Long bidRequestId, BidResponse bidResponse) {
        // Lock the bid request at the start of transaction to grant bid request and bid responses status consistency
        bidRequestRepository.lockById(bidRequestId);

        BidRequest bidRequest = bidRequestRepository.findByIdWithPlantItemsAndBidResponses(bidRequestId);

        Supplier supplier = supplierRepository.findById(securityService.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        // Check if bid request is open or in progress
        if (!Arrays.asList(BidRequestStatus.OPEN, BidRequestStatus.IN_PROGRESS).contains(bidRequest.getStatus())) {
            throw new RuntimeException("Cannot submit bid: Bid request is not open nor in progress");
        }

        bidResponse.setBidRequest(bidRequest);
        bidResponse.setSupplier(supplier);
        bidResponse.setStatus(BidResponseStatus.SUBMITTED);

        // Change bid request status to in progress
        bidRequest.setStatus(BidRequestStatus.IN_PROGRESS);
        bidRequest = bidRequestRepository.save(bidRequest);

        return bidResponseRepository.save(bidResponse);
    }

    @Transactional
    public BidResponse updateStatus(Long bidResponseId, BidResponseStatus newStatus) {
        // Lock the bid request at the start of transaction to grant bid request and bid responses status consistency
        bidRequestRepository.lockByBidResponseId(bidResponseId);

        BidResponse bidResponse = bidResponseRepository.findById(bidResponseId)
                .orElseThrow(() -> new RuntimeException("Bid response not found"));

        // Validate ownership
        securityService.checkOwnership(ProfileType.SUPPLIER, bidResponse.getSupplier().getId());

        if (bidResponse.getBidRequest().getStatus() != BidRequestStatus.IN_PROGRESS) {
            throw new RuntimeException("Cannot change bid response status: Bid request status not in progress");
        }

        record TransitionState(BidResponseStatus oldStatus, BidResponseStatus newStatus) {
        }
        List<TransitionState> transitionStates = Arrays.asList(
                new TransitionState(BidResponseStatus.SUBMITTED, BidResponseStatus.WITHDRAWN),
                new TransitionState(BidResponseStatus.WITHDRAWN, BidResponseStatus.SUBMITTED));

        for (TransitionState transitionState : transitionStates) {
            if (bidResponse.getStatus() == transitionState.oldStatus && newStatus == transitionState.newStatus) {
                bidResponse.setStatus(newStatus);
                return bidResponseRepository.save(bidResponse);
            }
        }
        throw new RuntimeException(
                "Invalid state transition. statusFrom: " + bidResponse.getStatus() + ", statusTo: " + newStatus);
    }
}
