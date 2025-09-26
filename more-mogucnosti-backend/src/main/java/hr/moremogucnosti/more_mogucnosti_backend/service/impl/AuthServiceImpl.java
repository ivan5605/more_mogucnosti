package hr.moremogucnosti.more_mogucnosti_backend.service.impl;

import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthExpResponse;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthLoginRequest;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthRegistracijaRequest;
import hr.moremogucnosti.more_mogucnosti_backend.dto.auth.AuthResponse;
import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import hr.moremogucnosti.more_mogucnosti_backend.exception.BadRequestException;
import hr.moremogucnosti.more_mogucnosti_backend.exception.DuplicateException;
import hr.moremogucnosti.more_mogucnosti_backend.exception.InvalidLoginException;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.RecenzijaMapper;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.RezervacijaMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.KorisnikRepository;
import hr.moremogucnosti.more_mogucnosti_backend.security.AppUserPrincipal;
import hr.moremogucnosti.more_mogucnosti_backend.security.JwtService;
import hr.moremogucnosti.more_mogucnosti_backend.service.AuthService;
import hr.moremogucnosti.more_mogucnosti_backend.service.RecenzijaService;
import hr.moremogucnosti.more_mogucnosti_backend.service.RezervacijaService;
import hr.moremogucnosti.more_mogucnosti_backend.service.UlogaService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final KorisnikRepository korisnikRepository;
    private final UlogaService ulogaService;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final RezervacijaMapper rezervacijaMapper;
    private final RecenzijaMapper recenzijaMapper;
    private final RezervacijaService rezervacijaService;
    private final RecenzijaService recenzijaService;

    @Override
    @Transactional
    public AuthResponse registracija(AuthRegistracijaRequest request) {
        String email = request.email().trim().toLowerCase();

        if (korisnikRepository.existsByEmail(email)){
            throw new DuplicateException("Ova email adresa već se koristi!");
        } else if (!request.lozinka().equals(request.lozinkaPotvrda())) {
           throw new BadRequestException("Lozinke se ne podudaraju!");
        }

        Korisnik k = new Korisnik();
        k.setIme(request.ime().trim());
        k.setPrezime(request.prezime().trim());
        k.setEmail(email);
        k.setLozinka(encoder.encode(request.lozinka()));
        k.setUloga(ulogaService.loadEntity("USER"));

        korisnikRepository.save(k);

        String token = jwtService.generateToken(k);
        long exp = jwtService.getExpirationMs(token); //da se vraca Date?

        return new AuthResponse(
                token,
                k.getEmail(),
                k.getIme(),
                k.getUloga().getNazivUloga(),
                exp
        );
    }

    @Override
    public AuthResponse prijava(AuthLoginRequest request) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.lozinka())
            );

            Korisnik k = korisnikRepository.findByEmailWUloga(request.email()).
                    orElseThrow(() -> new ResourceNotFoundException("Korisnik za email adresom " + request.email() + " ne postoji!"));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtService.generateToken(k);
            long exp = jwtService.getExpirationMs(token);
            return new AuthResponse(
                    token,
                    k.getEmail(),
                    k.getIme(),
                    k.getUloga().getNazivUloga(),
                    exp
            );
        } catch (BadCredentialsException e) {
            throw new InvalidLoginException("Pogrešan email ili lozinka!");
        }
    }

    @Override
    public KorisnikViewDto getUserInfo(AppUserPrincipal user) {
        if (user == null){
            throw new ResourceNotFoundException("Niste prijavljeni!");
        }
        Korisnik korisnik = korisnikRepository.findByEmailWUloga(user.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik sa email-om " + user.getUsername() + " ne postoji!"));

        return new KorisnikViewDto(
                korisnik.getId(),
                korisnik.getIme(),
                korisnik.getPrezime(),
                korisnik.getEmail()
        );
    }


    @Override
    public AuthExpResponse getExp(HttpServletRequest request) {
        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (auth == null){
            throw new ResourceNotFoundException("Nema prijavljenog korisnika!");
        }
        String token = (auth.startsWith("Bearer ")) ? auth.substring(7) : null;
        long exp = jwtService.getExpirationMs(token);
        return new AuthExpResponse(exp);
    }
}
