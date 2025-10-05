package hr.moremogucnosti.more_mogucnosti_backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.moremogucnosti.more_mogucnosti_backend.security.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity //spring security, moja konfiguracija
@AllArgsConstructor
@EnableMethodSecurity //za @PreAuthorize, ADMIN ili USER
public class SecurityConfig { //mozak Spring Security-a

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{ //primjeni se na svaki http zahtjev
        http
                .csrf(csrf -> csrf.disable()) //gasim jer radim stateless REST API koji koristi Bearer token u headeru (ne server-side sesije)
                //browse nemre sam od sebe dodati header Authorization, taj header more postaviti sam moj JavaScript na mojoj domeni

                //problem je samo XSS (cros2s-site scripting) - ako neko uspe ubrizgati JS u moj frontend

                .cors(cors -> cors.configurationSource(corsConfiguration()))

                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //security ne kreira http sesije, jwt svaki zahtjev

                .authorizeHttpRequests(auth -> auth
                        // auth/ je javno (login, register, refresh...)
                        .requestMatchers("/api/v1/auth/register",
                                "/api/v1/auth/login")
                        .permitAll()

                        // autentikacija obavezna:
                        .requestMatchers("/api/v1/rezervacija/create",
                                "/api/v1/korisnik/delete",
                                "/api/v1/korisnik/update",
                                "/api/v1/korisnik/updateLozinka",
                                "/api/v1/rezervacija/korisnik",
                                "/api/v1/rezervacija/update/**",
                                "/api/v1/rezervacija/delete/**",
                                "/api/v1/auth/me",
                                "/api/v1/recenzija/korisnik")
                        .authenticated()

                        .requestMatchers(
                                "/api/v1/rezervacija/admin/**",
                                "/api/v1/korisnik/admin/**",
                                "/api/v1/recenzija/admin/**",
                                "/api/v1/hotel/admin/**",
                                "/api/v1/soba/admin/**",
                                "/api/v1/slikaHotel/admin/**",
                                "/api/v1/slikaSoba/admin/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                "/api/v1/recenzija/moja/hotel/**",
                                "/api/v1/recenzija/delete/**"
                        ).hasRole("USER")

                        // sve ostalo je javno
                        .anyRequest().permitAll() //ovo ide na kraju
                )
                .exceptionHandling(e -> e //neautentificiran korisnik
                        .authenticationEntryPoint((req, res, ex) -> {
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("application/json");
                            var body = Map.of(
                                    "status", 401,
                                    "error", "Unauthorized",
                                    "message", "Prijava je obavezna.",
                                    "path", req.getRequestURI(),
                                    "timestamp", Instant.now().toString()
                            );
                            res.getWriter().write(new ObjectMapper().writeValueAsString(body));
                        })
                        .accessDeniedHandler((req, res, ex) -> { //kriva uloga
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            res.setContentType("application/json");
                            var body = Map.of(
                                    "status", 403,
                                    "error", "Forbidden",
                                    "message", "Nemate ovlasti za ovu radnju.",
                                    "path", req.getRequestURI(),
                                    "timestamp", Instant.now().toString()
                            );
                            res.getWriter().write(new ObjectMapper().writeValueAsString(body));
                        })
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
                //u lanac sam dodal Jwtfilter, "usidril" ga pije ovog
                //za svaki request filter pokuša pročitati i validirati JWT te postaviti Authentication

        return http.build();
    }

    @Bean //za svaku metodu označenu s @Bean, spring je poozve i zabilježi rezultat u ApplicationContext
    CorsConfigurationSource corsConfiguration() { //kak da odgovara na zahtjeve sa frontenda
        var c = new CorsConfiguration();
        c.setAllowedOrigins(List.of("http://localhost:3000"));
        c.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        c.setAllowedHeaders(List.of("*"));
        c.setAllowCredentials(true); //nepotrebno jer ne koristim cookie

        var s = new UrlBasedCorsConfigurationSource();
        s.registerCorsConfiguration("/**", c);
        return s; //postavljam konfiguraciju od gore na sve enpointove (root + sve podpute)
    }
}
