package pro.danton.gomaterials.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import jakarta.servlet.http.HttpServletRequest;
import pro.danton.gomaterials.exception.AccessDeniedException;
import pro.danton.gomaterials.model.ProfileInfo;
import pro.danton.gomaterials.model.ProfileType;

/**
 * Service for handling security-related operations and ownership validation
 */
@Service
@RequestScope
public class SecurityService {

    @Autowired
    private HttpServletRequest httpServletRequest;

    // Cache the ProfileInfo for the current request
    private ProfileInfo cachedProfileInfo;

    public ProfileInfo getProfileInfo() {
        // Return cached value if available
        if (cachedProfileInfo != null) {
            return cachedProfileInfo;
        }

        // Fetch and cache for this request
        Object object = httpServletRequest.getAttribute("profileInfo");
        if (object instanceof ProfileInfo profileInfo) {
            cachedProfileInfo = profileInfo;
            return profileInfo;
        }
        return null;
    }

    public Long getAuthenticatedUserId() {
        ProfileInfo profileInfo = getProfileInfo();
        if (profileInfo == null) {
            throw new AccessDeniedException("Authentication required");
        }
        return profileInfo.getProfileId();
    }

    public Long getLandscaperId() {
        ProfileInfo profileInfo = getProfileInfo();

        if (profileInfo == null) {
            throw new AccessDeniedException("Authentication required");
        }
        if (!profileInfo.isLandscaper()) {
            throw new AccessDeniedException("Access denied: Landscaper role required");
        }

        return profileInfo.getProfileId();
    }

    public Long getSupplierId() {
        ProfileInfo profileInfo = getProfileInfo();

        if (profileInfo == null) {
            throw new AccessDeniedException("Authentication required");
        }
        if (!profileInfo.isSupplier()) {
            throw new AccessDeniedException("Access denied: Supplier role required");
        }

        return profileInfo.getProfileId();
    }

    public void checkOwnership(ProfileType profileType, Long profileId) {
        ProfileInfo profileInfo = getProfileInfo();
        if (profileInfo != null && profileInfo.getProfileType() == profileType && profileInfo.getProfileId().equals(profileId)) {
            return;
        }	
        throw new AccessDeniedException("Access denied: You can only access your own resources");
    }
}
