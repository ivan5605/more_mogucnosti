package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.recenzija.*;
import hr.moremogucnosti.more_mogucnosti_backend.service.RecenzijaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recenzija")
@CrossOrigin("http://localhost:3000")

@AllArgsConstructor

public class RecenzijaController {

    private final RecenzijaService recenzijaService;

    @GetMapping("/hotel/{id}")
    public ResponseEntity<List<RecenzijaZaHotelDto>> getRecenzijeHotela(@PathVariable("id") Long idHotel){
        List<RecenzijaZaHotelDto> recenzijeHotela = recenzijaService.findAllByHotelId(idHotel);
        return new ResponseEntity<>(recenzijeHotela, HttpStatus.OK);
    }

    @PutMapping("/moja/hotel/{id}")
    public ResponseEntity<RecenzijaDetailsDto> upsertRecenzija (@PathVariable("id") Long hotelId, @AuthenticationPrincipal User user, @RequestBody RecenzijaCreateDto createDto){
        RecenzijaDetailsDto recenzijaDetailsDto = recenzijaService.editOrCreateRecenzija(user, hotelId, createDto);
        return new ResponseEntity<>(recenzijaDetailsDto, HttpStatus.OK);
    }

    @GetMapping("/hotel/info/{id}")
    public ResponseEntity<RecenzijaHotelStatusDto> getRecenzijeInfo (@PathVariable("id") Long hotelId) {
        RecenzijaHotelStatusDto recenzijaHotelStatusDto = recenzijaService.findRecenzijeStatus(hotelId);
        return new ResponseEntity<>(recenzijaHotelStatusDto, HttpStatus.OK);
    }

    @GetMapping("/korisnik")
    public ResponseEntity<List<RecenzijaZaKorisnikDto>> getRecenzijeKorisnikaById (@AuthenticationPrincipal User user) {
        List<RecenzijaZaKorisnikDto> recenzije = recenzijaService.findAllWithHotelByEmail(user);
        return new ResponseEntity<>(recenzije, HttpStatus.OK);
    }
}
