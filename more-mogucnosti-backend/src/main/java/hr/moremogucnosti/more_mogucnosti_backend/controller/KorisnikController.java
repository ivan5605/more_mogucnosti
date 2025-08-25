package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.service.KorisnikService;
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

    @GetMapping("{id}")
    public ResponseEntity<KorisnikViewDto> getKorisnik(@PathVariable("id") Long idKorisnik){
        KorisnikViewDto korisnikViewDto = korisnikService.findById(idKorisnik);
        return new ResponseEntity<>(korisnikViewDto, HttpStatus.OK);
    }
}
