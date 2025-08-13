package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.RezervacijaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.RezervacijaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.service.RezervacijaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rezervacija")
@CrossOrigin("http://localhost:3000")


@AllArgsConstructor

public class RezervacijaController {

    private final RezervacijaService rezervacijaService;

    @PostMapping
    public ResponseEntity<RezervacijaResponseDto> createRezervacija(@Valid @RequestBody RezervacijaCreateDto rezervacijaCreateDto){
        RezervacijaResponseDto rezervacijaResponseDto = rezervacijaService.createRezervacija(rezervacijaCreateDto);
        return new ResponseEntity<>(rezervacijaResponseDto, HttpStatus.OK);
    }
}
