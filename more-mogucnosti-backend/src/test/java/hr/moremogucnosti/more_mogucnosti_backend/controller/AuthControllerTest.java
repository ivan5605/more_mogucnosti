package hr.moremogucnosti.more_mogucnosti_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthExpResponse;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthLoginRequest;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthRegistracijaRequest;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthResponse;
import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.exception.GlobalExceptionHandler;
import hr.moremogucnosti.more_mogucnosti_backend.security.AppUserPrincipal;
import hr.moremogucnosti.more_mogucnosti_backend.security.JwtFilter;
import hr.moremogucnosti.more_mogucnosti_backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean AuthService authService;
    @MockitoBean JwtFilter jwtFilter;
    @MockitoBean AuthenticationManager authenticationManager;

    private Authentication auth(Long id, String email) {
        AppUserPrincipal p = new AppUserPrincipal(id, email, "pass", List.of());
        return new UsernamePasswordAuthenticationToken(p, p.getPassword(), p.getAuthorities());
    }

    @Test
    void register() throws Exception {
        AuthRegistracijaRequest body = new AuthRegistracijaRequest(
                "Ivo", "Ivić", "ivo@gmail.com", "Lozinka123", "Lozinka123"
        );
        AuthResponse authResponse = new AuthResponse("jwt", "ivo@gmail.com", "Ivo", "USER", 123L);

        when(authService.registracija(any(AuthRegistracijaRequest.class))).thenReturn(authResponse);

        mvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("jwt")))
                .andExpect(jsonPath("$.email", is("ivo@gmail.com")))
                .andExpect(jsonPath("$.ime", is("Ivo")))
                .andExpect(jsonPath("$.uloga", is("USER")))
                .andExpect(jsonPath("$.expAt", is(123)));
    }

    @Test
    void register_validation_badRequest() throws Exception {
        AuthRegistracijaRequest body = new AuthRegistracijaRequest(
                "", "", "nijeEmail", "", ""
        );

        mvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Neispravni podaci")))
                .andExpect(jsonPath("$.errors.ime", notNullValue()))
                .andExpect(jsonPath("$.errors.prezime", notNullValue()))
                .andExpect(jsonPath("$.errors.email", notNullValue()))
                .andExpect(jsonPath("$.errors.lozinka", notNullValue()))
                .andExpect(jsonPath("$.errors.lozinkaPotvrda", notNullValue()));
    }

    @Test
    void login() throws Exception {
        AuthLoginRequest body = new AuthLoginRequest("ivo@gmail.com", "Lozinka123");
        AuthResponse authResponse = new AuthResponse("jwt", "ivo@gmail.com", "Ivo", "USER", 999L);

        when(authService.prijava(any(AuthLoginRequest.class))).thenReturn(authResponse);

        mvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("jwt")))
                .andExpect(jsonPath("$.email", is("ivo@gmail.com")))
                .andExpect(jsonPath("$.expAt", is(999)));
    }

    @Test
    void me() throws Exception {
        KorisnikViewDto korisnikViewDto = new KorisnikViewDto(1L, "Ivo", "Ivić", "ivo@gmail.com");
        when(authService.getUserInfo(any(AppUserPrincipal.class))).thenReturn(korisnikViewDto);

        mvc.perform(get("/api/v1/auth/me").with(authentication(auth(1L, "ivo@gmail.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.ime", is("Ivo")))
                .andExpect(jsonPath("$.email", is("ivo@gmail.com")));
    }

    @Test
    void expAt_() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        AuthExpResponse authExpResponse = new AuthExpResponse(777L);
        when(authService.getExp(any(HttpServletRequest.class))).thenReturn(authExpResponse);

        mvc.perform(get("/api/v1/auth/expAt"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.expAt", is(777)));
    }

    @Test
    void expAt_noAuthHeader_notFound() throws Exception {
        when(authService.getExp(any(HttpServletRequest.class)))
                .thenThrow(new hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException("Nema prijavljenog korisnika!"));

        mvc.perform(get("/api/v1/auth/expAt"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Nema prijavljenog korisnika")));
    }
}
