package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.GradResponseDto;

//@Service
public interface GradService {
    GradResponseDto findById(Long id);
}
