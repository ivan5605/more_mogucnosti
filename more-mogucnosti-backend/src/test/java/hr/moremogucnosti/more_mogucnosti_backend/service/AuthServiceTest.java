package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthExpResponse;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthLoginRequest;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthRegistracijaRequest;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthResponse;
import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Uloga;
import hr.moremogucnosti.more_mogucnosti_backend.exception.BadRequestException;
import hr.moremogucnosti.more_mogucnosti_backend.exception.DuplicateException;
import hr.moremogucnosti.more_mogucnosti_backend.exception.InvalidLoginException;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.repository.KorisnikRepository;
import hr.moremogucnosti.more_mogucnosti_backend.security.AppUserPrincipal;
import hr.moremogucnosti.more_mogucnosti_backend.security.JwtService;
import hr.moremogucnosti.more_mogucnosti_backend.service.impl.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock KorisnikRepository korisnikRepository;
    @Mock UlogaService ulogaService;
    @Mock PasswordEncoder encoder;
    @Mock JwtService jwtService;
    @Mock AuthenticationManager authManager;

    @InjectMocks AuthServiceImpl service;

    @Test
    void registracija() {
        AuthRegistracijaRequest req = new AuthRegistracijaRequest(
                " Ivo ", " Ivić ", "  IVO@GMAIL.COM ", "lozinka", "lozinka"
        );

        when(korisnikRepository.existsByEmail("ivo@gmail.com")).thenReturn(false);

        Uloga uloga = new Uloga();
        uloga.setNazivUloga("USER");
        when(ulogaService.loadEntity("USER")).thenReturn(uloga);

        when(encoder.encode("lozinka")).thenReturn("sifra");
        when(jwtService.generateToken(any(Korisnik.class))).thenReturn("jwt");
        when(jwtService.getExpirationMs("jwt")).thenReturn(12345L);

        AuthResponse res = service.registracija(req);

        assertEquals("jwt", res.token());
        assertEquals("ivo@gmail.com", res.email());
        assertEquals("Ivo", res.ime());
        assertEquals("USER", res.uloga());
        assertEquals(12345L, res.expAt());

        verify(korisnikRepository).existsByEmail("ivo@gmail.com");
        verify(ulogaService).loadEntity("USER");
        verify(encoder).encode("lozinka");
        verify(korisnikRepository).save(any(Korisnik.class));
        verify(jwtService).generateToken(any(Korisnik.class));
        verify(jwtService).getExpirationMs("jwt");
        verifyNoMoreInteractions(korisnikRepository, ulogaService, encoder, jwtService);
    }

    @Test
    void registracija_emailVecPostoji_Duplicate() {
        AuthRegistracijaRequest req = new AuthRegistracijaRequest(
                "Ivo", "Ivić", "ivo@gmail.com", "lozinka", "lozinka"
        );
        when(korisnikRepository.existsByEmail("ivo@gmail.com")).thenReturn(true);

        assertThrows(DuplicateException.class, () -> service.registracija(req));

        verify(korisnikRepository).existsByEmail("ivo@gmail.com");
        verifyNoMoreInteractions(korisnikRepository);
        verifyNoInteractions(ulogaService, encoder, jwtService);
    }

    @Test
    void registracija_lozinkeSeNePodudaraju() {
        AuthRegistracijaRequest req = new AuthRegistracijaRequest(
                "Ivo", "Ivić", "ivo@gmail.com", "lozinka", "druga"
        );
        when(korisnikRepository.existsByEmail("ivo@gmail.com")).thenReturn(false);

        assertThrows(BadRequestException.class, () -> service.registracija(req));

        verify(korisnikRepository).existsByEmail("ivo@gmail.com");
        verifyNoMoreInteractions(korisnikRepository);
        verifyNoInteractions(ulogaService, encoder, jwtService);
    }

    @Test
    void prijava() {
        AuthLoginRequest req = new AuthLoginRequest("ivo@gmail.com", "lozinka");

        Authentication auth = mock(Authentication.class);
        when(authManager.authenticate(new UsernamePasswordAuthenticationToken("ivo@gmail.com", "lozinka")))
                .thenReturn(auth);

        Korisnik k = new Korisnik();
        k.setEmail("ivo@gmail.com");
        Uloga u = new Uloga(); u.setNazivUloga("USER");
        k.setUloga(u);
        k.setIme("Ivo");

        when(korisnikRepository.findByEmailWUloga("ivo@gmail.com")).thenReturn(Optional.of(k));
        when(jwtService.generateToken(k)).thenReturn("jwt");
        when(jwtService.getExpirationMs("jwt")).thenReturn(999L);

        AuthResponse res = service.prijava(req);

        assertEquals("jwt", res.token());
        assertEquals("ivo@gmail.com", res.email());
        assertEquals("Ivo", res.ime());
        assertEquals("USER", res.uloga());
        assertEquals(999L, res.expAt());

        verify(authManager).authenticate(new UsernamePasswordAuthenticationToken("ivo@gmail.com", "lozinka"));
        verify(korisnikRepository).findByEmailWUloga("ivo@gmail.com");
        verify(jwtService).generateToken(k);
        verify(jwtService).getExpirationMs("jwt");
        verifyNoMoreInteractions(authManager, korisnikRepository, jwtService);
    }

    @Test
    void prijava_kriviPodaci() {
        AuthLoginRequest req = new AuthLoginRequest("ivo@gmail.com", "pogresna");

        when(authManager.authenticate(new UsernamePasswordAuthenticationToken("ivo@gmail.com", "pogresna")))
                .thenThrow(new BadCredentialsException("bad"));

        assertThrows(InvalidLoginException.class, () -> service.prijava(req));

        verify(authManager).authenticate(new UsernamePasswordAuthenticationToken("ivo@gmail.com", "pogresna"));
        verifyNoInteractions(korisnikRepository, jwtService);
    }

    @Test
    void getUserInfo() {
        AppUserPrincipal principal = new AppUserPrincipal(1L, "ivo@gmail.com", "N/A", java.util.List.of());

        Korisnik k = new Korisnik();
        k.setId(1L);
        k.setEmail("ivo@gmail.com");
        k.setIme("Ivo");
        k.setPrezime("Ivić");

        when(korisnikRepository.findByEmailWUloga("ivo@gmail.com")).thenReturn(Optional.of(k));

        KorisnikViewDto dto = service.getUserInfo(principal);

        assertEquals(1L, dto.id());
        assertEquals("Ivo", dto.ime());
        assertEquals("Ivić", dto.prezime());
        assertEquals("ivo@gmail.com", dto.email());

        verify(korisnikRepository).findByEmailWUloga("ivo@gmail.com");
        verifyNoMoreInteractions(korisnikRepository);
    }

    @Test
    void getUserInfo_NotFound() {
        AppUserPrincipal principal = new AppUserPrincipal(1L, "nepostoji@gmail.com", "N/A", java.util.List.of());
        when(korisnikRepository.findByEmailWUloga("nepostoji@gmail.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getUserInfo(principal));

        verify(korisnikRepository).findByEmailWUloga("nepostoji@gmail.com");
        verifyNoMoreInteractions(korisnikRepository);
    }

    @Test
    void getExp() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer abc.token");

        when(jwtService.getExpirationMs("abc.token")).thenReturn(123L);

        AuthExpResponse res = service.getExp(req);

        assertEquals(123L, res.expAt());
        verify(jwtService).getExpirationMs("abc.token");
        verifyNoMoreInteractions(jwtService);
    }

    @Test
    void getExp_nemaPrijavljenog() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> service.getExp(req));
        verifyNoInteractions(jwtService);
    }
}
