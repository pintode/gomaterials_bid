package pro.danton.gomaterials.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import pro.danton.gomaterials.model.ProfileInfo;
import pro.danton.gomaterials.model.ProfileType;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24 hours

    @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(ProfileInfo profile) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("profile", profile);
        return createToken(claims);
    }

    private String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSecretKey())
                .compact();
    }

    public ProfileInfo extractProfile(String token) {
        var claims = extractAllClaims(token);
        
        var profileMap = claims.get("profile", Map.class);

        ProfileInfo profileInfo = new ProfileInfo();
        profileInfo.setProfileId(((Number) profileMap.get("profileId")).longValue());        
        profileInfo.setProfileName((String) profileMap.get("profileName"));
        profileInfo.setProfileType( ProfileType.valueOf((String) profileMap.get("profileType")));
        profileInfo.setExpiration(claims.getExpiration());
        
        return profileInfo;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
