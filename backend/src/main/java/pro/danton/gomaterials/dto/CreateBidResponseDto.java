package pro.danton.gomaterials.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateBidResponseDto {
    
    @NotNull(message = "Total price is required")
    @Positive(message = "Total price must be greater than 0")
    private BigDecimal totalPrice;
    
    @NotNull(message = "Estimated delivery date is required")
    private LocalDate estimatedDeliveryDate;
    
    @Size(max = 2048, message = "Notes cannot exceed 2048 characters")
    private String notes;
}
