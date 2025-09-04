package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.recenzija.*;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface RecenzijaService {
    List<RecenzijaZaHotelDto> findAllByHotelId(Long idHotel);

    RecenzijaDetailsDto editOrCreateRecenzija(User user, Long idHotel, RecenzijaCreateDto recenzijaCreateDto);

    RecenzijaHotelStatusDto findRecenzijeStatus(Long idHotel);

//    List<RecenzijaZaKorisnikDto> findAllByKorisnikEmail(User user);

    List<RecenzijaZaKorisnikDto> findAllWithHotelByEmail(User user);
}
