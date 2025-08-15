package pro.danton.gomaterials.dto;

import lombok.Data;
import pro.danton.gomaterials.model.ProfileType;

@Data
public class AuthResponseDto {
    private String token;

    private ProfileType profileType;

    private Long profileId;

    private String profileName;
}
