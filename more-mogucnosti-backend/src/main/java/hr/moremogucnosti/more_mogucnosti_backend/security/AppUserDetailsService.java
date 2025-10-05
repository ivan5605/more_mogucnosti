package hr.moremogucnosti.more_mogucnosti_backend.security;

import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.repository.KorisnikRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final KorisnikRepository korisnikRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Korisnik korisnik = korisnikRepository.findByEmailWUloga(email).
                orElseThrow(() -> new UsernameNotFoundException("Korisnik sa email " + email + " ne postoji!"));

        String role = korisnik.getUloga().getNazivUloga();

        return User.withUsername(korisnik.getEmail())
                .password(korisnik.getLozinka())
                .roles(role)
                .build();
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        Korisnik korisnik = korisnikRepository.findByIdWUloga(id)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik sa ID-jem " + id + " ne postoji!"));

        String uloga = korisnik.getUloga().getNazivUloga();
        var auths = List.of(new SimpleGrantedAuthority("ROLE_" + uloga));

        return new AppUserPrincipal(
                korisnik.getId(),
                korisnik.getEmail(),
                korisnik.getLozinka(),
                auths
        );
    }
}
