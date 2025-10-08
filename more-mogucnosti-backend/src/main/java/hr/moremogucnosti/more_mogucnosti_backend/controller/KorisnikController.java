package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.*;
import hr.moremogucnosti.more_mogucnosti_backend.security.AppUserPrincipal;
import hr.moremogucnosti.more_mogucnosti_backend.service.KorisnikService;
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
@RequestMapping("/api/v1/korisnik")
@CrossOrigin("http://localhost:3000")

@AllArgsConstructor

@Tag(name = "Korisnici")

public class KorisnikController {

    private final KorisnikService korisnikService;

    @GetMapping("{id}")
    public ResponseEntity<KorisnikViewDto> getKorisnik(@PathVariable("id") Long idKorisnik){
        KorisnikViewDto korisnikViewDto = korisnikService.findById(idKorisnik);
        return new ResponseEntity<>(korisnikViewDto, HttpStatus.OK);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/admin/count")
    public ResponseEntity<List<KorisnikAdminDto>> getAllWithCount(){
        List<KorisnikAdminDto> korisnici = korisnikService.findAllWithCount();
        return new ResponseEntity<>(korisnici, HttpStatus.OK);
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteKorisnik(@AuthenticationPrincipal AppUserPrincipal user, @RequestBody @Valid KorisnikDeleteDto dto) {
        korisnikService.korisnikDeleteProfil(user, dto.lozinka());
        return ResponseEntity.noContent().build();
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/update")
    public ResponseEntity<KorisnikViewDto> updateKorisnik(@AuthenticationPrincipal AppUserPrincipal user, @RequestBody @Valid KorisnikUpdateDto updateDto) {
        KorisnikViewDto updateKorisnik = korisnikService.korisnikUpdateProfil(user, updateDto);
        return new ResponseEntity<>(updateKorisnik, HttpStatus.OK);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/updateLozinka")
    public ResponseEntity<String> updateLozinka(@AuthenticationPrincipal AppUserPrincipal user, @RequestBody @Valid KorisnikPromjenaLozinkeDto promjenaLozinkeDto) {
        korisnikService.promjenaLozinke(user, promjenaLozinkeDto);
        return new ResponseEntity<>("Lozinka uspješno promijenjena!", HttpStatus.OK);
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> adminDeleteKorisnik(@PathVariable("id") Long idKorisnik) {
        korisnikService.adminDeleteKorisnik(idKorisnik);
        return ResponseEntity.noContent().build();
    }
}
