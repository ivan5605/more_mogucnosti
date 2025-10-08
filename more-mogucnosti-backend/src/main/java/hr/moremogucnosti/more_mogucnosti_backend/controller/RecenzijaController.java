package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.recenzija.*;
import hr.moremogucnosti.more_mogucnosti_backend.security.AppUserPrincipal;
import hr.moremogucnosti.more_mogucnosti_backend.service.RecenzijaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recenzija")
@CrossOrigin("http://localhost:3000")

@AllArgsConstructor

@Tag(name = "Recenzije")
public class RecenzijaController {

    private final RecenzijaService recenzijaService;

    @GetMapping("/hotel/{id}")
    public ResponseEntity<List<RecenzijaZaHotelDto>> getRecenzijeHotela(@PathVariable("id") Long idHotel){
        List<RecenzijaZaHotelDto> recenzijeHotela = recenzijaService.findAllByHotelId(idHotel);
        return new ResponseEntity<>(recenzijeHotela, HttpStatus.OK);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/moja/hotel/{id}")
    public ResponseEntity<RecenzijaDetailsDto> upsertRecenzija (@PathVariable("id") Long hotelId, @AuthenticationPrincipal AppUserPrincipal user, @RequestBody @Valid RecenzijaCreateDto createDto){
        RecenzijaDetailsDto recenzijaDetailsDto = recenzijaService.editOrCreateRecenzija(user.getId(), hotelId, createDto);
        return new ResponseEntity<>(recenzijaDetailsDto, HttpStatus.OK);
    }

    @GetMapping("/hotel/info/{id}")
    public ResponseEntity<RecenzijaHotelStatusDto> getRecenzijeInfo (@PathVariable("id") Long hotelId) {
        RecenzijaHotelStatusDto recenzijaHotelStatusDto = recenzijaService.findRecenzijeStatus(hotelId);
        return new ResponseEntity<>(recenzijaHotelStatusDto, HttpStatus.OK);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/korisnik")
    public ResponseEntity<List<RecenzijaZaKorisnikDto>> getRecenzijePrijavljenogKorisnikaById (@AuthenticationPrincipal AppUserPrincipal user) {
        List<RecenzijaZaKorisnikDto> recenzije = recenzijaService.findAllWithHotelById(user.getId());
        return new ResponseEntity<>(recenzije, HttpStatus.OK);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/admin/korisnik/{id}")
    public ResponseEntity<List<RecenzijaZaKorisnikDto>> getRecenzijeKorisnikaById (@PathVariable("id") Long idKorisnik) {
        List<RecenzijaZaKorisnikDto> recenzije = recenzijaService.findAllByKorisnikId(idKorisnik);
        return new ResponseEntity<>(recenzije, HttpStatus.OK);
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRecenzijaById (@AuthenticationPrincipal AppUserPrincipal user,
                                                       @PathVariable("id") Long idRecenzija){
        recenzijaService.deleteById(idRecenzija, user.getId());
        return new ResponseEntity<>("Recenzija uspješno obrisana!", HttpStatus.OK);
    }
}
