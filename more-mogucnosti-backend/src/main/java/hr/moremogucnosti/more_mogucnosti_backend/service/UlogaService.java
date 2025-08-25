package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.UlogaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Uloga;

public interface UlogaService {
    UlogaResponseDto findById(Long id); //ne koristim baš mislim da mi ne treba

    Uloga loadEntity(String naziv);
}
