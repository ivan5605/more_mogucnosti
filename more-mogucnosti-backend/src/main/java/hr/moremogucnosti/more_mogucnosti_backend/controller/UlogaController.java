package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.UlogaResponseDto;
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
    public ResponseEntity<UlogaResponseDto> getUloga(@PathVariable("id") Long id){
        UlogaResponseDto ulogaDto = ulogaService.findById(id);
        return new ResponseEntity<>(ulogaDto, HttpStatus.OK);
    }

}
