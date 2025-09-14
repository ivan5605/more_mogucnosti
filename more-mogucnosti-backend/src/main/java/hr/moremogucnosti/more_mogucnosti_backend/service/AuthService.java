package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthExpResponse;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthLoginRequest;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthRegistracijaRequest;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthResponse;
import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.security.AppUserPrincipal;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    AuthResponse registracija(AuthRegistracijaRequest zahtjev);

    AuthResponse prijava(AuthLoginRequest zahtjev);

    KorisnikViewDto getUserInfo(AppUserPrincipal user);

    AuthExpResponse getExp(HttpServletRequest request);
}
