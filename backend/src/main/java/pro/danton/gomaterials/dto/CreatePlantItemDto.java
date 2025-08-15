package pro.danton.gomaterials.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePlantItemDto {
    
    @NotBlank(message = "Plant name is required")
    @Size(min = 1, max = 255, message = "Plant name must be between 1 and 255 characters")
    private String name;
    
    @NotBlank(message = "Grade is required")
    @Size(min = 1, max = 100, message = "Grade must be between 1 and 100 characters")
    private String grade;
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    private Long quantity;
    
    @NotBlank(message = "Unit is required")
    @Size(min = 1, max = 50, message = "Unit must be between 1 and 50 characters")
    private String unit;
}
