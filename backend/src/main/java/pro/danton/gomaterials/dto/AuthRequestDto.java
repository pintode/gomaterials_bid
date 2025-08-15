package pro.danton.gomaterials.dto;

import lombok.Data;
import pro.danton.gomaterials.model.ProfileType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

@Data
public class AuthRequestDto {
    @NotNull(message = "Profile type is required")
    private ProfileType profileType;
    
    @NotNull(message = "Profile ID is required")
    @Min(value = 1, message = "Profile ID must be greater than 0")
    private Long profileId;
}

