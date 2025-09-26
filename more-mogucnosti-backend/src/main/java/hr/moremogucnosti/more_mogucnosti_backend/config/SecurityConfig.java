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
@EnableWebSecurity //ja želim kontrolirati pravila ne koristi default
@AllArgsConstructor
@EnableMethodSecurity //za @PreAuthorize, ADMIN ili USER
public class SecurityConfig { //mozak Spring Security-a

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable()) //gasim jer radim stateless REST API koji koristi Bearer token u headeru (ne cookie sesije)
                //browse nemre sam od sebe dodati header Authorization, taj header more postaviti sam moj JavaScript na mojoj domeni

                //problem je samo XSS (cross-site scripting) - ako neko uspe ubrizgati JS u moj frontend

                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //spring ne kreira HTTP sesije i ništa ne sprema na server-side
                //svaki request se autentificira samostalno preko JWT-a

                .authorizeHttpRequests(auth -> auth
                        // auth/ je javno (login, register, refresh...)
                        .requestMatchers("/api/auth/register",
                                "/api/auth/login")
                        .permitAll()

                        // autentikacija obavezna:
                        .requestMatchers("/api/rezervacija/create",
                                "/api/korisnik/delete",
                                "/api/korisnik/update",
                                "/api/korisnik/updateLozinka",
                                "/api/rezervacija/korisnik",
                                "/api/rezervacija/update/**",
                                "/api/rezervacija/delete/**",
                                "/api/auth/me",
                                "/api/recenzija/moja/hotel/**",
                                "/api/recenzija/korisnik",
                                "/api/recenzija/delete/**")
                        .authenticated()

                        .requestMatchers(
                                "/api/rezervacija/admin/**",
                                "/api/korisnik/admin/**",
                                "/api/recenzija/admin/**",
                                "/api/hotel/admin/**",
                                "/api/soba/admin/**",
                                "/api/slikaHotel/admin/**",
                                "/api/slikaSoba/admin/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                "/api/recenzija/moja/hotel/**",
                                "/api/recenzija/delete/**"
                        ).hasRole("USER")

                        // sve ostalo je javno
                        .anyRequest().permitAll() //ovo ide na kraju
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((req, res, ex) -> {        // 401
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
                        .accessDeniedHandler((req, res, ex) -> {             // 403
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
                //u lanac sam dodal Jwtfilter i to prije standardnog username/password filtra
                //za svaki request filter pokuša pročitati i validirati JWT te postaviti Authentication

        return http.build();
    }

    @Bean //za svaku metodu označenu s @Bean, spring je poozve i zabilježi rezultat u ApplicationContext
    CorsConfigurationSource corsConfigurationSource() { //kak da odgovara na zahtjeve sa frontenda
        var c = new CorsConfiguration();
        c.setAllowedOrigins(List.of("http://localhost:3000"));
        c.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        c.setAllowedHeaders(List.of("*"));
        c.setAllowCredentials(true);

        var s = new UrlBasedCorsConfigurationSource();
        s.registerCorsConfiguration("/**", c);
        return s; //postavljam konfiguraciju od gore na sve enpointove (root + sve podpute)
    }
}
