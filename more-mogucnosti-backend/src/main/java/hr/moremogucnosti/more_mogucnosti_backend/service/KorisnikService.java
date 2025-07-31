package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.KorisnikDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.KorisnikRegistracijaDto;

public interface KorisnikService {
    KorisnikDto registrirajKorisnik(KorisnikRegistracijaDto korisnikRegistracijaDto);
}
