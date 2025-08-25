package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthLoginRequest;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthRegistracijaRequest;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthResponse;
import org.springframework.security.core.userdetails.User;

public interface AuthService {
    AuthResponse registracija(AuthRegistracijaRequest zahtjev);
    AuthResponse prijava(AuthLoginRequest zahtjev);
    Object getUserInfo(User user);
}
