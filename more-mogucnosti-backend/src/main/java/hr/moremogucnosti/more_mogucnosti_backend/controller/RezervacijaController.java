package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.*;
import hr.moremogucnosti.more_mogucnosti_backend.security.AppUserPrincipal;
import hr.moremogucnosti.more_mogucnosti_backend.service.RezervacijaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rezervacija")
@CrossOrigin("http://localhost:3000")


@AllArgsConstructor

public class RezervacijaController {

    private final RezervacijaService rezervacijaService;

    @PostMapping("/create")
    public ResponseEntity<RezervacijaDetailsDto> createRezervacija(@Valid @RequestBody RezervacijaCreateDto rezervacijaCreateDto,
                                                                   @AuthenticationPrincipal AppUserPrincipal user){
        RezervacijaDetailsDto RezervacijaDetailsDto = rezervacijaService.createRezervacija(rezervacijaCreateDto, user);
        return new ResponseEntity<>(RezervacijaDetailsDto, HttpStatus.OK);
    }

    @GetMapping("/korisnik")
    public ResponseEntity<List<RezervacijaZaKorisnikDto>> getRezervacijeKorisnika(@AuthenticationPrincipal AppUserPrincipal user){
        List<RezervacijaZaKorisnikDto> rezervacijeKorisnika = rezervacijaService.findAll(user.getId());
        return new ResponseEntity<>(rezervacijeKorisnika, HttpStatus.OK);
    }

    @GetMapping("/datumi/{id}")
    public ResponseEntity<List<RezervacijaDatumDto>> getZauzetiDatumi(@PathVariable("id") Long idSoba){
        List<RezervacijaDatumDto> zauzetiDatumi = rezervacijaService.findAllZauzetiDatumi(idSoba);
        return new ResponseEntity<>(zauzetiDatumi, HttpStatus.OK);
    }

    @GetMapping("/admin/korisnikAkt/{id}")
    public ResponseEntity<List<RezervacijaDetailsDto>> getAktRezervacije (@PathVariable("id") Long idKorisnik) {
        List<RezervacijaDetailsDto> aktivne = rezervacijaService.findAllAktivneKorisnika(idKorisnik);
        return new ResponseEntity<>(aktivne, HttpStatus.OK);
    }

    @GetMapping("/admin/korisnikSt/{id}")
    public ResponseEntity<List<RezervacijaDetailsDto>> getStareRezervacije (@PathVariable("id") Long idKorisnik) {
        List<RezervacijaDetailsDto> stare = rezervacijaService.findAllStareKorisnika(idKorisnik);
        return new ResponseEntity<>(stare, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRezervacija (@AuthenticationPrincipal AppUserPrincipal user,
                                                   @PathVariable("id") Long idRezervacija) {
        rezervacijaService.deleteRezervacija(user.getId(), idRezervacija);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RezervacijaDetailsDto> updateRezervacija (@AuthenticationPrincipal AppUserPrincipal user,
                                                                    @PathVariable("id") Long idRezervacija,
                                                                    @RequestBody RezervacijaUpdateDto novaRezervacija) {
        RezervacijaDetailsDto rezervacijaDetailsDto = rezervacijaService.updateRezervacija(user.getId(), idRezervacija, novaRezervacija);
        return new ResponseEntity<>(rezervacijaDetailsDto, HttpStatus.OK);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> adminDeleteRezervacija (@PathVariable("id") Long id) {
        rezervacijaService.adminDelete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<RezervacijaDetailsDto> adminUpdateRezervacija (@PathVariable("id") Long idRezervacija,
                                                                         @RequestBody RezervacijaUpdateDto novaRezervacija) {
        RezervacijaDetailsDto rezervacija = rezervacijaService.adminUpdate(idRezervacija, novaRezervacija);
        return new ResponseEntity<>(rezervacija, HttpStatus.OK);
    }

    @GetMapping("/admin/hotel/{hotelId}")
    public ResponseEntity<List<RezervacijaDetailsDto>> getAllRezervacijeHotela (@PathVariable("hotelId") Long hotelId) {
        List<RezervacijaDetailsDto> rezervacije = rezervacijaService.findAllByHotelId(hotelId);
        return new ResponseEntity<>(rezervacije, HttpStatus.OK);
    }
}
