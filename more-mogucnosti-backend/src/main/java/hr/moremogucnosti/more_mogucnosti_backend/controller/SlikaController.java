package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.SlikaDto;
import hr.moremogucnosti.more_mogucnosti_backend.service.SlikaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/hotelSlika")
@CrossOrigin("http://localhost:3000")

//jel ovo uopce treba??

public class SlikaController {

    private final SlikaService slikaService;

    @GetMapping("/glavnaSlikaHotel/{id}")
    public ResponseEntity<SlikaDto> getGlavnaSlikaHotel(@PathVariable("id") Long id){
        SlikaDto slikaDto = slikaService.getGlavnaSlikaHotel(id);
        return new ResponseEntity<>(slikaDto, HttpStatus.OK);
    }

    @GetMapping("/ostaleSlikeHotel/{id}")
    public ResponseEntity<List<SlikaDto>> getOstaleSlikeHotel(@PathVariable("id") Long id){
        List<SlikaDto> ostaleSlike = slikaService.getOstaleSlikeHotel(id);
        return new ResponseEntity<>(ostaleSlike, HttpStatus.OK);
    }
}
