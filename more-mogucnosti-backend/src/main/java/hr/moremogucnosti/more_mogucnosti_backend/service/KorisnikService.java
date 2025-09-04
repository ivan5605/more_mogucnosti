package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;

public interface KorisnikService {
    KorisnikViewDto findById(Long id);

    Korisnik loadEntity(Long id);

    Korisnik loadEntityByEmail(String email);
}
