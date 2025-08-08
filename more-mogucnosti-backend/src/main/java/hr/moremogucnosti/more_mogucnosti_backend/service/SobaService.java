package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.SobaDto;

import java.util.List;

public interface SobaService {
    SobaDto getSoba(Long idSoba);
    List<SobaDto> getSobeHotela(Long hotelId);
    List<SobaDto> getRandomSobeHotela(Long hotelId);
}
