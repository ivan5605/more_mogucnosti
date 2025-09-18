package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaDetailsDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaUpdateDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Soba;

import java.util.List;

public interface SobaService {
    SobaResponseDto findById(Long idSoba);

    List<SobaResponseDto> findAllByIdHotel(Long hotelId);

    SobaDetailsDto findDetailsById(Long isSoba);

    Soba loadEntity(Long idSoba);

    SobaResponseDto createSoba(Long idHotel, SobaCreateDto sobaDto);

    void softDeleteSoba(Long idSoba);

    SobaResponseDto updateSoba(Long idSoba, SobaUpdateDto sobaDto);
}
