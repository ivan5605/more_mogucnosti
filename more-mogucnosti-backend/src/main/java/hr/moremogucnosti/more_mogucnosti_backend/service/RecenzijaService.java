package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.recenzija.*;

import java.util.List;

public interface RecenzijaService {
    List<RecenzijaZaHotelDto> findAllByHotelId(Long idHotel);

    RecenzijaDetailsDto editOrCreateRecenzija(Long userId, Long idHotel, RecenzijaCreateDto recenzijaCreateDto);

    RecenzijaHotelStatusDto findRecenzijeStatus(Long idHotel);

    List<RecenzijaZaKorisnikDto> findAllWithHotelById(Long userId);

    List<RecenzijaZaKorisnikDto> findAllByKorisnikId(Long id);

    void deleteById(Long idRecenzija, Long userId);
}
