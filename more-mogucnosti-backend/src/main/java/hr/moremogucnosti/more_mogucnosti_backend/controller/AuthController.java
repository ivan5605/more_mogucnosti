package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthExpResponse;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthLoginRequest;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthRegistracijaRequest;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthResponse;
import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.security.AppUserPrincipal;
import hr.moremogucnosti.more_mogucnosti_backend.security.JwtService;
import hr.moremogucnosti.more_mogucnosti_backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registracija(@RequestBody @Valid AuthRegistracijaRequest registracijaZahtjev) {
        return ResponseEntity.ok(authService.registracija(registracijaZahtjev));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> prijava(@RequestBody @Valid AuthLoginRequest loginZahtjev){
        return ResponseEntity.ok(authService.prijava(loginZahtjev));
    }

    @GetMapping("/me")
    public ResponseEntity<KorisnikViewDto> me(@AuthenticationPrincipal AppUserPrincipal user){ //uzmi trenutno autentificiranog korisnika iz SecurityContext i injektaj ga u ovu metodu kao argument
        KorisnikViewDto viewDto = authService.getUserInfo(user);
        return new ResponseEntity<>(viewDto, HttpStatus.OK);
    }

    @GetMapping("/expAt")
    public ResponseEntity<AuthExpResponse> getExpAt(HttpServletRequest request){
        AuthExpResponse response = authService.getExp(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

