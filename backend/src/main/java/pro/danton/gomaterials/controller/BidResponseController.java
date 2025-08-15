package pro.danton.gomaterials.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
import pro.danton.gomaterials.dto.BidResponseDto;
import pro.danton.gomaterials.dto.CreateBidResponseDto;
import pro.danton.gomaterials.model.BidResponse;
import pro.danton.gomaterials.model.BidResponseStatus;
import pro.danton.gomaterials.service.BidResponseService;
import pro.danton.gomaterials.mapper.BidResponseMapper;

@Validated
@Tag(name = "Bid Responses")
@RestController
@RequestMapping("/api/v1/bid-responses")
public class BidResponseController {
    @Autowired
    private BidResponseService bidResponseService;
    @Autowired
    private BidResponseMapper bidResponseMapper;

    @Operation(summary = "Submit bid")
    @PostMapping
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<BidResponseDto> submitBid(
            @Parameter(required = true, description = "ID of the bid request to submit a bid for") @RequestParam Long bidRequestId,
            @Parameter(required = true, description = "Bid response details") @Valid @RequestBody CreateBidResponseDto createBidResponseDto) {
        BidResponse bidResponse = bidResponseMapper.toEntity(createBidResponseDto);

        BidResponse created = bidResponseService.submitBid(bidRequestId, bidResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(bidResponseMapper.toDto(created));
    }

    @Operation(summary = "Withdraw bid response")
    @PostMapping("/{bidResponseId}/withdraw")
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<BidResponseDto> withdrawBidResponse(
            @Parameter(required = true) @PathVariable Long bidResponseId) {

        BidResponse bidResponse = bidResponseService.updateStatus(bidResponseId, BidResponseStatus.WITHDRAWN);
        return ResponseEntity.ok(bidResponseMapper.toDto(bidResponse));
    }

    @Operation(summary = "Undo withdraw (return bid response to SUBMITTED)")
    @PostMapping("/{bidResponseId}/undo-withdraw")
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<BidResponseDto> undoWithdrawBidResponse(
            @Parameter(required = true) @PathVariable Long bidResponseId) {

        BidResponse bidResponse = bidResponseService.updateStatus(bidResponseId, BidResponseStatus.SUBMITTED);
        return ResponseEntity.ok(bidResponseMapper.toDto(bidResponse));
    }
}
