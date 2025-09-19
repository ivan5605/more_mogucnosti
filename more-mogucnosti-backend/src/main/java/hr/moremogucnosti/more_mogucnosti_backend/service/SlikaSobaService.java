package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaSobaResponseDto;

public interface SlikaSobaService {

    SlikaSobaResponseDto addSlikaSoba(Long sobaId, SlikaCreateDto createDto);

    void deleteSlikaSoba(Long idSlika);

    SlikaResponseDto setGlavna(Long idSlika);
}
