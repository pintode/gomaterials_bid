package pro.danton.gomaterials.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pro.danton.gomaterials.model.BidRequest;
import pro.danton.gomaterials.model.BidRequestStatus;
import pro.danton.gomaterials.model.BidResponse;
import pro.danton.gomaterials.model.BidResponseStatus;
import pro.danton.gomaterials.model.Landscaper;
import pro.danton.gomaterials.model.ProfileType;
import pro.danton.gomaterials.repository.BidRequestRepository;
import pro.danton.gomaterials.repository.BidResponseRepository;
import pro.danton.gomaterials.repository.LandscaperRepository;

@Service
public class BidRequestService {
    @Autowired
    private BidRequestRepository bidRequestRepository;
    @Autowired
    private LandscaperRepository landscaperRepository;
    @Autowired
    private BidResponseRepository bidResponseRepository;
    @Autowired
    private SecurityService securityService;

    @Transactional
    public BidRequest create(BidRequest bidRequest) {
        Landscaper landscaper = landscaperRepository.findById(securityService.getLandscaperId())
                .orElseThrow(() -> new RuntimeException("Landscaper not found"));
        bidRequest.setLandscaper(landscaper);
        bidRequest.getPlantItems().forEach(plantItem -> plantItem.setBidRequest(bidRequest));
        bidRequest.setStatus(BidRequestStatus.OPEN);
        bidRequest.setBidResponses(new ArrayList<>());
        return bidRequestRepository.save(bidRequest);
    }

    @Transactional(readOnly = true)
    public Optional<BidRequest> getById(Long id) {
        return Optional.ofNullable(bidRequestRepository.findByIdWithPlantItemsAndBidResponses(id));
    }

    public List<BidRequest> getAll(Optional<Long> landscaperId) {
        var list = bidRequestRepository.findAllWithLandscaperAndPlantItems(landscaperId);
        list.forEach(br -> br.setBidResponses(null));
        return list;
    }

    @Transactional
    public BidRequest updateStatus(Long bidRequestId, BidRequestStatus newStatus, Long awardedBidResponseId) {
        // Lock the bid request at the start of transaction to grant bid request and bid responses status consistency
        bidRequestRepository.lockById(bidRequestId);

        BidRequest bidRequest = bidRequestRepository.findByIdWithPlantItemsAndBidResponses(bidRequestId);

        // Validate ownership
        securityService.checkOwnership(ProfileType.LANDSCAPER, bidRequest.getLandscaper().getId());

        record TransitionState(BidRequestStatus oldStatus, BidRequestStatus newStatus,
                BidResponseStatus bidResponseNewStatus, boolean requiresAwardedBidId) {
        }

        List<TransitionState> transitionStates = Arrays.asList(new TransitionState[] {
                // AWARD or COMPLET
                new TransitionState(BidRequestStatus.IN_PROGRESS, BidRequestStatus.AWARDED, BidResponseStatus.REJECTED,
                        true),
                new TransitionState(BidRequestStatus.AWARDED, BidRequestStatus.COMPLETED, BidResponseStatus.REJECTED,
                        false),
                // CANCELL
                new TransitionState(BidRequestStatus.OPEN, BidRequestStatus.CANCELLED, BidResponseStatus.CANCELLED,
                        false),
                new TransitionState(BidRequestStatus.IN_PROGRESS, BidRequestStatus.CANCELLED,
                        BidResponseStatus.CANCELLED, false),
                new TransitionState(BidRequestStatus.AWARDED, BidRequestStatus.CANCELLED, BidResponseStatus.CANCELLED,
                        false),
                // Undo CANCELL
                new TransitionState(BidRequestStatus.CANCELLED, BidRequestStatus.IN_PROGRESS,
                        BidResponseStatus.SUBMITTED, false),
        });

        for (TransitionState transitionState : transitionStates) {
            if (bidRequest.getStatus() == transitionState.oldStatus && newStatus == transitionState.newStatus
                    && (awardedBidResponseId != null) == transitionState.requiresAwardedBidId) {

                List<BidResponse> bids = bidRequest.getBidResponses().stream().filter(bid -> bid.getStatus() != BidResponseStatus.WITHDRAWN).toList();

                BidResponse awardedBid = transitionState.requiresAwardedBidId
                        ? bids.stream().filter(br -> br.getId().equals(awardedBidResponseId)).findFirst()
                                .orElseThrow(() -> new RuntimeException("Bid response not found"))
                        : newStatus == BidRequestStatus.COMPLETED
                                ? bids.stream().filter(br -> br.getStatus() == BidResponseStatus.AWARDED).findFirst()
                                        .orElseThrow(() -> new RuntimeException("Bid response not found"))
                                : null;

                for (BidResponse bid : bids) {
                    // Set the status of the awarded bid to AWARDED and all other bids status to the new status
                    bid.setStatus(bid == awardedBid ? BidResponseStatus.AWARDED : transitionState.bidResponseNewStatus);
                    bidResponseRepository.save(bid);
                }

                bidRequest.setStatus(newStatus);
                bidRequest = bidRequestRepository.save(bidRequest);

                return bidRequest;
            }
        }

        throw new RuntimeException("Invalid state transition. statusFrom: " + bidRequest.getStatus() + ", statusTo: "
                + newStatus + ", awardedBidResponseId: " + awardedBidResponseId);
    }
}
