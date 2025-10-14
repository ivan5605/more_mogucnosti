package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthExpResponse;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthLoginRequest;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthRegistracijaRequest;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthResponse;
import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.security.AppUserPrincipal;
import hr.moremogucnosti.more_mogucnosti_backend.security.JwtService;
import hr.moremogucnosti.more_mogucnosti_backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor

@Tag(name = "Authentication")
public class AuthController {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    @Operation(summary = "Kreiraj korisnika", description = "Vraća novog korisnik")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Validacija nesupješna")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registracija(@RequestBody @Valid AuthRegistracijaRequest registracijaZahtjev) {
        AuthResponse response = authService.registracija(registracijaZahtjev);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> prijava(@RequestBody @Valid AuthLoginRequest loginZahtjev){
        AuthResponse response = authService.prijava(loginZahtjev);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public ResponseEntity<KorisnikViewDto> me(@AuthenticationPrincipal AppUserPrincipal user){ //uzmi trenutno autentificiranog korisnika iz SecurityContext i injektaj ga u ovu metodu kao argument
        KorisnikViewDto viewDto = authService.getUserInfo(user);
        return new ResponseEntity<>(viewDto, HttpStatus.OK);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/expAt")
    public ResponseEntity<AuthExpResponse> getExpAt(HttpServletRequest request){
        AuthExpResponse response = authService.getExp(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

