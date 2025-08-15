package pro.danton.gomaterials;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.ForwardedHeaderFilter;

import pro.danton.gomaterials.config.JwtAuthenticationFilter;

@Configuration
@ConditionalOnWebApplication
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .sessionManagement(session -> {
                        // No session, no JSESSIONID cookie
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                }) 
                .headers(headers -> {
                        headers.httpStrictTransportSecurity(hsts -> hsts.maxAgeInSeconds(31536000));
                })
                .csrf(csrf -> {
                    csrf.ignoringRequestMatchers(request -> {
                        var origin = request.getHeader(HttpHeaders.ORIGIN);
                        return "https://gomaterials.danton.pro".equals(origin) ||
                                "http://localhost:8080".equals(origin);
                    });
                })
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
        // Configuring X-Forwarded-Proto, X-Forwarded-Port, and X-Forwarded-For
        FilterRegistrationBean<ForwardedHeaderFilter> filterBean = new FilterRegistrationBean<>(
                new ForwardedHeaderFilter());
        filterBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterBean;
    }
}