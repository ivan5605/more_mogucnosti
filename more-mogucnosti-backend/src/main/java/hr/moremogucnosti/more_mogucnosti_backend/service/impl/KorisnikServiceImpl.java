package hr.moremogucnosti.more_mogucnosti_backend.service.impl;

import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.KorisnikMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.KorisnikRepository;
import hr.moremogucnosti.more_mogucnosti_backend.service.KorisnikService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class KorisnikServiceImpl implements KorisnikService {

    private final KorisnikRepository korisnikRepository;
    private final KorisnikMapper korisnikMapper;

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
}
