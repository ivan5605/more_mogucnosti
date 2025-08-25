package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaDetailsDto;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface RezervacijaService {
    RezervacijaDetailsDto createRezervacija(RezervacijaCreateDto rezervacijaCreateDto, User user);
    List<RezervacijaDetailsDto> findAllByIdSoba(Long idSoba);
}
