package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaDatumDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaDetailsDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaZaKorisnikDto;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface RezervacijaService {
    RezervacijaDetailsDto createRezervacija(RezervacijaCreateDto rezervacijaCreateDto, User user);

    List<RezervacijaDatumDto> findAllZauzetiDatumi(Long idSoba);

    List<RezervacijaZaKorisnikDto> findAll(User user);
}
