package pro.danton.gomaterials.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pro.danton.gomaterials.model.ProfileInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String jwt = null;
        ProfileInfo profileInfo = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                profileInfo = jwtUtil.extractProfile(jwt);
            } catch (Exception ex) {
                // Token is invalid, continue without authentication
                throw new RuntimeException("Invalid JWT token", ex);
            }
        }

        if (profileInfo != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (profileInfo.getExpiration().after(new Date())) {  // Changed from .before() to .after()
                // Create authorities based on profile type
                String username = null;
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                if (profileInfo != null) {
                    username = profileInfo.getProfileName();
                    String role = "ROLE_" + profileInfo.getProfileType().name();
                    authorities.add(new SimpleGrantedAuthority(role));
                                    }

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        username, null, authorities);
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Store profile info as a single object in request attributes
                request.setAttribute("profileInfo", profileInfo);

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
