package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.UlogaDto;
import hr.moremogucnosti.more_mogucnosti_backend.service.UlogaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/uloga")
@CrossOrigin("http://localhost:3000")

@AllArgsConstructor

public class UlogaController {

    private final UlogaService ulogaService;

    @GetMapping("{id}")
    public ResponseEntity<UlogaDto> getUloga(@PathVariable("id") Long id){
        UlogaDto ulogaDto = ulogaService.getUloga(id);
        return new ResponseEntity<>(ulogaDto, HttpStatus.OK);
    }

}
