package hr.moremogucnosti.more_mogucnosti_backend.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService; //most izmedju baze podataka i Spring Security-ja

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String auth = request.getHeader("Authorization"); //uzima authorization header, ocekujemo format Authorization: Bearer <JWT>
        if (auth != null && auth.startsWith("Bearer ")){
            String token = auth.substring(7); //sirovi token
            try {
                String email = jwtService.validateAndGetSubject(token); //jwtService provjerava potpis i istek (exp), ako štima vraća email, ako ne baca iznimku
                var details = userDetailsService.loadUserByUsername(email); //ucitavamo korisnika iz baze po emailu

                var authToken = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //napravi WebAuthenticationDetails objekt iz HttServletRequest
                //dodaje IP adresu i ako postoji session ID requesta u Authentication objekt

                SecurityContextHolder.getContext().setAuthentication(authToken);
                //upisan Authentication objekt u SecurityContext

            } catch (Exception e) {
                System.out.println("JWT error: " + e.getMessage());
            }
        }
        filterChain.doFilter(request, response); //prepustam zahtjev dalje
    }
}

//jwtFilter nije zaduzen za vracanje odgovora, pa ne postavlja response