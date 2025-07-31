package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.KorisnikDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.KorisnikRegistracijaDto;
import hr.moremogucnosti.more_mogucnosti_backend.service.KorisnikService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/korisnik")
@CrossOrigin("http://localhost:3000")

@AllArgsConstructor

public class KorisnikController {

    private final KorisnikService korisnikService;

    @PostMapping
    public ResponseEntity<KorisnikDto> registrirajKorisnik(@Valid @RequestBody KorisnikRegistracijaDto korisnikRegistracijaDto){
        KorisnikDto korisnikDto = korisnikService.registrirajKorisnik(korisnikRegistracijaDto);
        return new ResponseEntity<>(korisnikDto, HttpStatus.CREATED);
    }
}
