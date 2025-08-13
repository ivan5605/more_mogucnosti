package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.SobaDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.SobaZaRezervacijuDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Soba;

import java.util.List;

public interface SobaService {
    SobaDto getSoba(Long idSoba);
    List<SobaDto> getSobeHotela(Long hotelId);
    List<SobaDto> getRandomSobeHotela(Long hotelId);
    SobaZaRezervacijuDto getSobaWithHotelAndSlike(Long isSoba);
    Soba getEntity(Long idSoba);
}
