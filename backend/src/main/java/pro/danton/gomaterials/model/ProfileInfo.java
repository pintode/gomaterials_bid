package pro.danton.gomaterials.model;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Data
public class ProfileInfo {
    @NotNull
    private ProfileType profileType;

    @NotNull
    private Long profileId;

    @NotNull
    private String profileName;

    @NotNull
    private Date expiration;

    public boolean isLandscaper() {
        return ProfileType.LANDSCAPER.equals(this.profileType);
    }

    public boolean isSupplier() {
        return ProfileType.SUPPLIER.equals(this.profileType);
    }
}
