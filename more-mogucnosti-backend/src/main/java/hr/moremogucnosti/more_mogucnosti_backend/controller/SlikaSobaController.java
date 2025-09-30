package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaSobaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.service.SlikaSobaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/slikaSoba")
@CrossOrigin("http://localhost:3000")

@AllArgsConstructor

@Tag(name = "Slike - soba")
public class SlikaSobaController {

    private final SlikaSobaService service;

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/admin/create/{sobaId}")
    public ResponseEntity<SlikaSobaResponseDto> addSlika(@PathVariable("sobaId") Long sobaId,
                                                         @Valid @RequestBody SlikaCreateDto createDto) {
        SlikaSobaResponseDto responseDto = service.addSlikaSoba(sobaId, createDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> deleteSlika(@PathVariable("id") Long id) {
        service.deleteSlikaSoba(id);
        return ResponseEntity.noContent().build();
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/admin/setGlavna/{id}")
    public ResponseEntity<SlikaResponseDto> setGlavna(@PathVariable("id") Long id) {
        SlikaResponseDto responseDto = service.setGlavna(id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
