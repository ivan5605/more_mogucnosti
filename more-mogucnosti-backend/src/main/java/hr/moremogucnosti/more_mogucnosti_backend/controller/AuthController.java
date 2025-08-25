package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.Security.JwtService;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthLoginRequest;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthRegistracijaRequest;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthResponse;
import hr.moremogucnosti.more_mogucnosti_backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
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
    public ResponseEntity<?> me(@AuthenticationPrincipal User user){ //uzmi trenutno autentificiranog korisnika iz SecurityContext i injektaj ga u ovu metodu kao argument
        return new ResponseEntity<>(authService.getUserInfo(user), HttpStatus.OK);
    }
}

