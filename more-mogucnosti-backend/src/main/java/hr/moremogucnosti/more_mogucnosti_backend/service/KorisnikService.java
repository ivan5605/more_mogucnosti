package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikAdminDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikPromjenaLozinkeDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikUpdateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import hr.moremogucnosti.more_mogucnosti_backend.security.AppUserPrincipal;

import java.util.List;

public interface KorisnikService {
    KorisnikViewDto findById(Long id);

    Korisnik loadEntity(Long id);

    Korisnik loadEntityByEmail(String email);

    List<KorisnikAdminDto> findAllWithCount();

    KorisnikViewDto korisnikUpdateProfil(AppUserPrincipal user, KorisnikUpdateDto updateDto);

    void korisnikDeleteProfil(AppUserPrincipal user, String lozinka);

    void promjenaLozinke(AppUserPrincipal user, KorisnikPromjenaLozinkeDto promjenaLozinkeDto);

    void adminDeleteKorisnik(Long idKorisnik);
}
