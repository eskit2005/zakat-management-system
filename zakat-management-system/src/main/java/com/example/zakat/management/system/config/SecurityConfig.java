package com.example.zakat.management.system.config;

import com.example.zakat.management.system.entities.Role;
import com.example.zakat.management.system.filters.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        // ── Public (no login required) ──────────────────────────────
                        .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/auth/refresh").permitAll()
                        // Transparency pages — public-facing accountability showcase
                        .requestMatchers(HttpMethod.GET,  "/api/report").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/history").permitAll()

                        // ── Admin only (admin can also access donor/beneficiary endpoints below) ──
                        .requestMatchers(HttpMethod.POST, "/api/assignments").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET,  "/api/assignments").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET,  "/api/beneficiaries/queue").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET,  "/api/dashboard").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET,  "/api/users").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET,  "/api/receipts").hasRole(Role.ADMIN.name())

                        // ── Donor + Admin ────────────────────────────────────────────
                        .requestMatchers(HttpMethod.POST, "/api/donations")
                        .hasAnyRole(Role.DONOR.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET,  "/api/receipts/donation/**")
                        .hasAnyRole(Role.DONOR.name(), Role.ADMIN.name())

                        // ── Beneficiary + Admin ──────────────────────────────────────
                        .requestMatchers(HttpMethod.POST, "/api/beneficiaries")
                        .hasAnyRole(Role.BENEFICIARY.name(), Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/api/eligibility")
                        .hasAnyRole(Role.BENEFICIARY.name(), Role.ADMIN.name())

                        // ── Any authenticated user ───────────────────────────────────
                        .requestMatchers(HttpMethod.GET,    "/api/auth/me").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/auth/logout").authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(c -> {
                    c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
                    c.accessDeniedHandler((req, res, ex) -> res.setStatus(HttpStatus.FORBIDDEN.value()));
                });

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
