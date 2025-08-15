package pro.danton.gomaterials.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateBidRequestDto {
    
    @NotBlank(message = "Project name is required")
    @Size(min = 1, max = 255, message = "Project name must be between 1 and 255 characters")
    private String projectName;
    
    @NotNull(message = "Required by date is required")
    private LocalDate requiredBy;
    
    @NotEmpty(message = "At least one plant item is required")
    @Valid
    private List<CreatePlantItemDto> plantItems;
}
