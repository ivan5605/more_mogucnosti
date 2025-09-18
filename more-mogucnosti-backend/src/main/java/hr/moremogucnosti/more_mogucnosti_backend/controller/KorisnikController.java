package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikAdminDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikPromjenaLozinkeDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikUpdateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.security.AppUserPrincipal;
import hr.moremogucnosti.more_mogucnosti_backend.service.KorisnikService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/admin/count")
    public ResponseEntity<List<KorisnikAdminDto>> getAllWithCount(){
        List<KorisnikAdminDto> korisnici = korisnikService.findAllWithCount();
        return new ResponseEntity<>(korisnici, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteKorisnik(@AuthenticationPrincipal AppUserPrincipal user, @RequestBody String lozinka) {
        korisnikService.korisnikDeleteProfil(user, lozinka);
        return new ResponseEntity<>("Profil je izbrisan!", HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<KorisnikViewDto> updateKorisnik(@AuthenticationPrincipal AppUserPrincipal user, @RequestBody @Valid KorisnikUpdateDto updateDto) {
        KorisnikViewDto updateKorisnik = korisnikService.korisnikUpdateProfil(user, updateDto);
        return new ResponseEntity<>(updateKorisnik, HttpStatus.OK);
    }

    @PutMapping("/updateLozinka")
    public ResponseEntity<String> updateLozinka(@AuthenticationPrincipal AppUserPrincipal user, @RequestBody @Valid KorisnikPromjenaLozinkeDto promjenaLozinkeDto) {
        korisnikService.promjenaLozinke(user, promjenaLozinkeDto);
        return new ResponseEntity<>("Lozinka uspješno promijenjena!", HttpStatus.OK);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> adminDeleteKorisnik(@PathVariable("id") Long idKorisnik) {
        korisnikService.adminSoftDeleteKorisnik(idKorisnik);
        return ResponseEntity.noContent().build();
    }
}
