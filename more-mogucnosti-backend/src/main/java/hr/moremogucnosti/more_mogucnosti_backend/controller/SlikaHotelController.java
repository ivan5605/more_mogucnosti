package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaHotelResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.service.SlikaHotelService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/slikaHotel")
@CrossOrigin("http://localhost:3000")

@AllArgsConstructor

@Tag(name = "Slike - hotel")
public class SlikaHotelController {

    private final SlikaHotelService service;

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/admin/create/{hotelId}")
    public ResponseEntity<SlikaHotelResponseDto> addSlikaHotel(@PathVariable("hotelId") Long hotelId,  @RequestBody @Valid SlikaCreateDto createDto) {
        SlikaHotelResponseDto slika = service.addSlikaHotel(hotelId, createDto);
        return new ResponseEntity<>(slika, HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> deleteSlika(@PathVariable("id") Long id) {
        service.deleteSlikaHotel(id);
        return ResponseEntity.noContent().build();
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/admin/setGlavna/{id}")
    public ResponseEntity<SlikaResponseDto> setGlavna(@PathVariable("id") Long id) {
        SlikaResponseDto slika = service.setGlavna(id);
        return new ResponseEntity<>(slika, HttpStatus.OK);
    }
}