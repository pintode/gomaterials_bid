package pro.danton.gomaterials.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.danton.gomaterials.dto.AuthRequestDto;
import pro.danton.gomaterials.dto.AuthResponseDto;
import pro.danton.gomaterials.model.Landscaper;
import pro.danton.gomaterials.model.ProfileInfo;
import pro.danton.gomaterials.model.Supplier;
import pro.danton.gomaterials.model.ProfileType;
import pro.danton.gomaterials.repository.LandscaperRepository;
import pro.danton.gomaterials.repository.SupplierRepository;
import pro.danton.gomaterials.config.JwtUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = { "http://localhost:4200", "https://gomaterials.danton.pro" })
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private LandscaperRepository landscaperRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody @NotNull AuthRequestDto authRequest) {
        try {
            ProfileInfo profileInfo = new ProfileInfo();
            profileInfo.setProfileType(authRequest.getProfileType());
            profileInfo.setProfileId(authRequest.getProfileId());

            if (profileInfo.getProfileType() == ProfileType.LANDSCAPER) {
                Landscaper landscaper = landscaperRepository.findById(profileInfo.getProfileId())
                        .orElseThrow(() -> new RuntimeException("Landscaper not found"));
                profileInfo.setProfileName(landscaper.getName());
            } else if (profileInfo.getProfileType() == ProfileType.SUPPLIER) {
                Supplier supplier = supplierRepository.findById(profileInfo.getProfileId())
                        .orElseThrow(() -> new RuntimeException("Supplier not found"));
                profileInfo.setProfileName(supplier.getName());
            } else {
                throw new RuntimeException("Invalid profile type");
            }

            var token = jwtUtil.generateToken(profileInfo);
            var response = new AuthResponseDto();
            response.setToken(token);
            response.setProfileType(profileInfo.getProfileType());
            response.setProfileId(profileInfo.getProfileId());
            response.setProfileName(profileInfo.getProfileName());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Authentication failed: " + e.getMessage());
        }
    }
}
