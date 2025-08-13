package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.KorisnikDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.KorisnikRegistracijaDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;

public interface KorisnikService {
    KorisnikDto registrirajKorisnik(KorisnikRegistracijaDto korisnikRegistracijaDto);
    Korisnik getEntity(Long idKorisnik);
    KorisnikDto prijavaKorisnik(String email, String lozinka);
}
