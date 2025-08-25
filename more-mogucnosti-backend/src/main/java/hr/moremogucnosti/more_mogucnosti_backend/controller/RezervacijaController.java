package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaDetailsDto;
import hr.moremogucnosti.more_mogucnosti_backend.service.RezervacijaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

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
}
