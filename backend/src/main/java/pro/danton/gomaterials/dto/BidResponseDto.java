package pro.danton.gomaterials.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import pro.danton.gomaterials.model.BidResponseStatus;

@Data
public class BidResponseDto {
    private Long id;
    private LocalDateTime createdAt;
    private BigDecimal totalPrice;
    private LocalDate estimatedDeliveryDate;
    private String notes;
    private SupplierDto supplier;
    private BidResponseStatus status;
}
