package hr.moremogucnosti.more_mogucnosti_backend.config;

import hr.moremogucnosti.more_mogucnosti_backend.Security.JwtFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity //ja želim kontrolirati pravila ne koristi default
@AllArgsConstructor
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
                        .requestMatchers("/api/auth/**").permitAll()

                        // autentikacija obavezna:
                        .requestMatchers("/api/rezervacija/**").authenticated()


                        // sve ostalo je javno
                        .anyRequest().permitAll() //ovo ide na kraju
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
