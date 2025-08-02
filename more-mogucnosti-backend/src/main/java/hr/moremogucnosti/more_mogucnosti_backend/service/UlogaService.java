package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.UlogaDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Uloga;

public interface UlogaService {
    UlogaDto getUlogaById(Long id);
    Uloga getUlogaByNaziv(String naziv);
}
