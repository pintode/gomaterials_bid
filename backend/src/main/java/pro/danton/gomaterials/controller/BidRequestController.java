package pro.danton.gomaterials.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import pro.danton.gomaterials.dto.BidRequestDto;
import pro.danton.gomaterials.dto.CreateBidRequestDto;
import pro.danton.gomaterials.model.BidRequest;
import pro.danton.gomaterials.model.BidRequestStatus;
import pro.danton.gomaterials.service.BidRequestService;
import pro.danton.gomaterials.mapper.BidRequestMapper;

@Validated
@Tag(name = "Bid Requests")
@RestController
@RequestMapping("/api/v1/bid-requests")
public class BidRequestController {
    @Autowired
    private BidRequestService bidRequestService;
    @Autowired
    private BidRequestMapper bidRequestMapper;

    @Operation(summary = "Create bid request")
    @PostMapping
    @PreAuthorize("hasRole('LANDSCAPER')")
    public ResponseEntity<BidRequestDto> create(
            @Parameter(required = true) @Valid @RequestBody CreateBidRequestDto createBidRequestDto) {
        BidRequest bidRequest = bidRequestMapper.toEntity(createBidRequestDto);

        bidRequest = bidRequestService.create(bidRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(bidRequestMapper.toDto(bidRequest));
    }

    @Operation(summary = "Get bid request by id")
    @GetMapping("/{id}")
    public ResponseEntity<BidRequestDto> getById(
            @Parameter(required = true) @PathVariable Long id) {
        return bidRequestService.getById(id)
                .map(bidRequestMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all bid requests")
    @GetMapping
    public List<BidRequestDto> getAll(
            @Parameter(required = false) @RequestParam(required = false) Long landscaperId) {
        var list = bidRequestService.getAll(Optional.ofNullable(landscaperId));
        return list.stream().map(bidRequestMapper::toDto).collect(Collectors.toList());
    }

    @Operation(summary = "Award bid response to bid request")
    @PostMapping("/{bidRequestId}/award/{bidResponseId}")
    @PreAuthorize("hasRole('LANDSCAPER')")
    public ResponseEntity<BidRequestDto> awardBid(
            @Parameter(required = true) @PathVariable Long bidRequestId,
            @Parameter(required = true) @PathVariable Long bidResponseId) {
        BidRequest bidRequest = bidRequestService.updateStatus(bidRequestId, BidRequestStatus.AWARDED, bidResponseId);
        return ResponseEntity.ok(bidRequestMapper.toDto(bidRequest));
    }

    @Operation(summary = "Cancel bid request")
    @PostMapping("/{bidRequestId}/cancel")
    @PreAuthorize("hasRole('LANDSCAPER')")
    public ResponseEntity<BidRequestDto> cancelBidRequest(
            @Parameter(required = true) @PathVariable Long bidRequestId) {
        BidRequest bidRequest = bidRequestService.updateStatus(bidRequestId, BidRequestStatus.CANCELLED, null);
        return ResponseEntity.ok(bidRequestMapper.toDto(bidRequest));
    }

    @Operation(summary = "Mark awarded bid request as completed")
    @PostMapping("/{bidRequestId}/complete")
    @PreAuthorize("hasRole('LANDSCAPER')")
    public ResponseEntity<BidRequestDto> completeBidRequest(
            @Parameter(required = true) @PathVariable Long bidRequestId) {
        BidRequest bidRequest = bidRequestService.updateStatus(bidRequestId, BidRequestStatus.COMPLETED, null);
        return ResponseEntity.ok(bidRequestMapper.toDto(bidRequest));
    }
}
