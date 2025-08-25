package hr.moremogucnosti.more_mogucnosti_backend.service.impl;

import hr.moremogucnosti.more_mogucnosti_backend.Security.JwtService;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthLoginRequest;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthRegistracijaRequest;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthResponse;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import hr.moremogucnosti.more_mogucnosti_backend.exception.BadRequestException;
import hr.moremogucnosti.more_mogucnosti_backend.exception.DuplicateException;
import hr.moremogucnosti.more_mogucnosti_backend.exception.InvalidLoginException;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.repository.KorisnikRepository;
import hr.moremogucnosti.more_mogucnosti_backend.service.AuthService;
import hr.moremogucnosti.more_mogucnosti_backend.service.UlogaService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final KorisnikRepository korisnikRepository;
    private final UlogaService ulogaService;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    @Override
    public AuthResponse registracija(AuthRegistracijaRequest request) {
        if (korisnikRepository.existsByEmail(request.email())){
            throw new DuplicateException("Ova email adresa već je zauzeta!");
        } else if (!request.lozinka().equals(request.lozinkaPotvrda())) {
           throw new BadRequestException("Lozinke se ne podudaraju!");
        }

        Korisnik k = new Korisnik();
        k.setIme(request.ime());
        k.setPrezime(request.prezime());
        k.setEmail(request.email());
        k.setLozinka(encoder.encode(request.lozinka()));
        k.setUloga(ulogaService.loadEntity("USER"));

        korisnikRepository.save(k);

        String token = jwtService.generate(k.getEmail());
        return new AuthResponse(
                token,
                k.getEmail(),
                k.getIme(),
                k.getUloga().getNazivUloga()
        );
    }

    @Override
    public AuthResponse prijava(AuthLoginRequest request) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.lozinka())
            );

            Korisnik k = korisnikRepository.findByEmail(request.email()).
                    orElseThrow(() -> new ResourceNotFoundException("Korisnik za email adresom " + request.email() + " ne postoji!"));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtService.generate(request.email());
            return new AuthResponse(
                    token,
                    k.getEmail(),
                    k.getIme(),
                    k.getUloga().getNazivUloga()
            );
        } catch (BadCredentialsException e) {
            throw new InvalidLoginException("Pogrešan email ili lozinka!");
        }
    }

    @Override
    public Object getUserInfo(User user) {
        if (user == null){
            throw new ResourceNotFoundException("Niste prijavljeni!");
        }
        return user.getUsername();
    }
}
