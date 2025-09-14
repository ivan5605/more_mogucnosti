package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.*;
import hr.moremogucnosti.more_mogucnosti_backend.security.AppUserPrincipal;

import java.util.List;

public interface RezervacijaService {
    RezervacijaDetailsDto createRezervacija(RezervacijaCreateDto rezervacijaCreateDto, AppUserPrincipal user);

    List<RezervacijaDatumDto> findAllZauzetiDatumi(Long idSoba);

    List<RezervacijaZaKorisnikDto> findAll(Long userId);

    List<RezervacijaDetailsDto> findAllAktivneKorisnika(Long idKorisnik);

    List<RezervacijaDetailsDto> findAllStareKorisnika(Long idKorisnik);

    void deleteRezervacija(Long userId, Long rezervacijaId);

    RezervacijaDetailsDto updateRezervacija(Long userId, Long rezervacijaIdd, RezervacijaUpdateDto novaRezervacija);
}
