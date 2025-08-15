package pro.danton.gomaterials.model;

public enum BidResponseStatus {
    SUBMITTED,      // Initial status when bid response is created
    AWARDED,        // When this bid is chosen as the winner
    REJECTED,       // When this bid is not chosen as the winner
    WITHDRAWN,      // When the supplier withdraws their bid
    CANCELLED       // When the bid request is cancelled
}
