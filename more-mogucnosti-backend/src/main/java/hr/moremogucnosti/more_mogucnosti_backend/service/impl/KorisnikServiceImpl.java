package hr.moremogucnosti.more_mogucnosti_backend.service.impl;

import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikAdminDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikPromjenaLozinkeDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikUpdateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import hr.moremogucnosti.more_mogucnosti_backend.exception.DuplicateException;
import hr.moremogucnosti.more_mogucnosti_backend.exception.LozinkaException;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.KorisnikMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.KorisnikRepository;
import hr.moremogucnosti.more_mogucnosti_backend.security.AppUserPrincipal;
import hr.moremogucnosti.more_mogucnosti_backend.service.KorisnikService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)

public class KorisnikServiceImpl implements KorisnikService {

    private final KorisnikRepository korisnikRepository;
    private final KorisnikMapper korisnikMapper;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;

    @Override
    public KorisnikViewDto findById(Long id) {
        Korisnik korisnik = korisnikRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik sa ID-jem " + id + " ne postoji"));
        return korisnikMapper.toViewDto(korisnik);
    }

    @Override
    public Korisnik loadEntity(Long id) {
        return korisnikRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Korisnik sa ID-jem " + id + " ne postoji"));
    }

    @Override
    public Korisnik loadEntityByEmail(String email) {
        return korisnikRepository.findByEmailWUloga(email)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik sa email-om " + email + " ne postoji!"));
    }

    @Override
    public List<KorisnikAdminDto> findAllWithCount() {
        return korisnikRepository.findKorisnikWithCount();
    }

    @Override
    @Transactional
    public KorisnikViewDto korisnikUpdateProfil(AppUserPrincipal user, KorisnikUpdateDto updateDto) {
        String noviEmail = updateDto.email().trim().toLowerCase();

        Korisnik korisnik = korisnikRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik sa email-om " + user.getId() + " ne postoji!"));

        if (korisnikRepository.existsByEmailAndIdNot(noviEmail, korisnik.getId())){
            throw new DuplicateException("Ova se email adresa već koristi!");
        }

        korisnik.setIme(updateDto.ime().trim());
        korisnik.setPrezime(updateDto.prezime().trim());
        korisnik.setEmail(updateDto.email().trim().toLowerCase());

        return korisnikMapper.toViewDto(korisnik);
    }

    @Override
    @Transactional
    public void korisnikDeleteProfil(AppUserPrincipal user, String lozinka) {
        Korisnik korisnik = korisnikRepository.findByIdWUloga(user.getId()).
                orElseThrow(() -> new ResourceNotFoundException("Korisnik sa email " + user.getUsername() + " ne postoji"));

//        if (!encoder.matches(lozinka, korisnik.getLozinka())){
//            throw new LozinkeNePodudarajuException("Netočna lozinka!");
//        }

        //brojanje pokusaja, blokade?
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), lozinka)
            );
        } catch (BadCredentialsException | AccountStatusException ex) {
            throw new LozinkaException("Netočna lozinka!");
        }


        korisnikRepository.deleteById(korisnik.getId());
    }

    @Override
    @Transactional
    public void promjenaLozinke(AppUserPrincipal user, KorisnikPromjenaLozinkeDto promjenaLozinkeDto) {
        Korisnik korisnik = korisnikRepository.findById(user.getId()).
                orElseThrow(() -> new ResourceNotFoundException("Korisnik sa ID-jem" + user.getId() + " ne postoji!"));

        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), promjenaLozinkeDto.staraLozinka())
            );
        } catch (BadCredentialsException | AccountStatusException ex) {
            throw new LozinkaException("Netočna lozinka!");
        }

        if (!promjenaLozinkeDto.novaLozinka().equals(promjenaLozinkeDto.novaLozinkaPotvrda())) {
            throw new LozinkaException("Lozinke se ne podudaraju!");
        } else if (encoder.matches(promjenaLozinkeDto.novaLozinka(), korisnik.getLozinka())) {
            throw new LozinkaException("Nova lozinka ne može biti ista kao trenutna!");
        }

        korisnik.setLozinka(encoder.encode(promjenaLozinkeDto.novaLozinka()));
        korisnikRepository.save(korisnik);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void adminSoftDeleteKorisnik(Long idKorisnik) {
        Korisnik korisnik = korisnikRepository.findById(idKorisnik)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik sa ID-jem " + idKorisnik + " ne postoji!"));

        korisnikRepository.deleteById(idKorisnik);
    }
}
