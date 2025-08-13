package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.RezervacijaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.RezervacijaResponseDto;

import java.util.List;

public interface RezervacijaService {
    RezervacijaResponseDto createRezervacija(RezervacijaCreateDto rezervacijaCreateDto);
    List<RezervacijaResponseDto> getRezervacijeSobe(Long idSoba);
}
