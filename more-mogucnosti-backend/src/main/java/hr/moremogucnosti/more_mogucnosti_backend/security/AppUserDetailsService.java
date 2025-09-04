package hr.moremogucnosti.more_mogucnosti_backend.security;

import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import hr.moremogucnosti.more_mogucnosti_backend.repository.KorisnikRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final KorisnikRepository korisnikRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Korisnik korisnik = korisnikRepository.findByEmailWUloga(email).
                orElseThrow(() -> new UsernameNotFoundException("Korisnik sa email-om " + email + " ne postoji!"));

        String role = korisnik.getUloga() != null ? korisnik.getUloga().getNazivUloga() : "USER";

        return User.withUsername(korisnik.getEmail())
                .password(korisnik.getLozinka())
                .roles(role)
                .build();
    }
}
