package pro.danton.gomaterials.model;

public enum BidRequestStatus {
    OPEN,           // Initial status when bid request is created
    IN_PROGRESS,    // When bids are being submitted
    AWARDED,        // When a bid has been awarded
    COMPLETED,      // When the project is finished
    CANCELLED       // When the bid request is cancelled
}
