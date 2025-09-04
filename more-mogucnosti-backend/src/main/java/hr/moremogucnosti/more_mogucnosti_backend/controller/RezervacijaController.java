package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaDatumDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaDetailsDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaZaKorisnikDto;
import hr.moremogucnosti.more_mogucnosti_backend.service.RezervacijaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rezervacija")
@CrossOrigin("http://localhost:3000")


@AllArgsConstructor

public class RezervacijaController {

    private final RezervacijaService rezervacijaService;

    @PostMapping
    public ResponseEntity<RezervacijaDetailsDto> createRezervacija(@Valid @RequestBody RezervacijaCreateDto rezervacijaCreateDto, @AuthenticationPrincipal User user){ //i Authentication auth
        RezervacijaDetailsDto RezervacijaDetailsDto = rezervacijaService.createRezervacija(rezervacijaCreateDto, user);
        return new ResponseEntity<>(RezervacijaDetailsDto, HttpStatus.OK);
    }

    @GetMapping("/korisnik")
    public ResponseEntity<List<RezervacijaZaKorisnikDto>> getRezervacijeKorisnika(@AuthenticationPrincipal User user){
        List<RezervacijaZaKorisnikDto> rezervacijeKorisnika = rezervacijaService.findAll(user);
        return new ResponseEntity<>(rezervacijeKorisnika, HttpStatus.OK);
    }

    @GetMapping("/datumi/{id}")
    public ResponseEntity<List<RezervacijaDatumDto>> getZauzetiDatumi(@PathVariable("id") Long idSoba){
        List<RezervacijaDatumDto> zauzetiDatumi = rezervacijaService.findAllZauzetiDatumi(idSoba);
        return new ResponseEntity<>(zauzetiDatumi, HttpStatus.OK);
    }
}
