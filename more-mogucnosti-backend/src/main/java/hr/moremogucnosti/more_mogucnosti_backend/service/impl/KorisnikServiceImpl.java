package hr.moremogucnosti.more_mogucnosti_backend.service.impl;

import hr.moremogucnosti.more_mogucnosti_backend.dto.KorisnikDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.KorisnikRegistracijaDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Uloga;
import hr.moremogucnosti.more_mogucnosti_backend.exception.DuplicateException;
import hr.moremogucnosti.more_mogucnosti_backend.exception.LozinkeNePodudarajuException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.KorisnikMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.KorisnikRepository;
import hr.moremogucnosti.more_mogucnosti_backend.service.KorisnikService;
import hr.moremogucnosti.more_mogucnosti_backend.service.UlogaService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class KorisnikServiceImpl implements KorisnikService {

    private final KorisnikRepository korisnikRepository;
    private final KorisnikMapper korisnikMapper;
    private final UlogaService ulogaService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public KorisnikDto registrirajKorisnik(KorisnikRegistracijaDto korisnikRegistracijaDto) {
        if (korisnikRegistracijaDto.getLozinka().equals(korisnikRegistracijaDto.getLozinkaPotvrda())){
            Korisnik korisnik = korisnikMapper.fromKorisnikRegistracijaDto(korisnikRegistracijaDto);

            if (korisnikRepository.existsByEmail(korisnik.getEmail())){
                throw new DuplicateException("Ova email adresa se već koristi!");
            } else {
                korisnik.setLozinka(passwordEncoder.encode(korisnik.getLozinka()));
                Uloga uloga = ulogaService.getUlogaByNaziv("USER");
                korisnik.setUloga(uloga);

                Korisnik savedKorsinik = korisnikRepository.save(korisnik);
                return korisnikMapper.toKorisnikDto(savedKorsinik);
            }
        } else {
            throw new LozinkeNePodudarajuException("Lozinke se ne podudaraju!");
        }
    }
}
