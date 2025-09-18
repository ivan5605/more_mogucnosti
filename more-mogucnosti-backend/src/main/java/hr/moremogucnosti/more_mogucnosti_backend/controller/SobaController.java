package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaDetailsDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaUpdateDto;
import hr.moremogucnosti.more_mogucnosti_backend.service.SobaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/soba")
@CrossOrigin("http://localhost:3000")

@AllArgsConstructor
public class SobaController {

    private final SobaService sobaService;

    @GetMapping("{id}")
    public ResponseEntity<SobaResponseDto> getSoba(@PathVariable("id") Long id){
        SobaResponseDto sobaDto = sobaService.findById(id);
        return new ResponseEntity<>(sobaDto, HttpStatus.OK);
    }

    @GetMapping("/hotel/{id}")
    public ResponseEntity<List<SobaResponseDto>> getAllSobaHotel(@PathVariable("id") Long id){
        List<SobaResponseDto> sobeHotela = sobaService.findAllByIdHotel(id);
        return new ResponseEntity<>(sobeHotela, HttpStatus.OK);
    }

    @GetMapping("/withHotelAndSlike/{id}")
    public ResponseEntity<SobaDetailsDto> getDetailsSoba(@PathVariable("id") Long id){
        SobaDetailsDto sobaDetailsDto = sobaService.findDetailsById(id);
        return new ResponseEntity<>(sobaDetailsDto,HttpStatus.OK);
    }

    @PostMapping("/admin/create/{id}")
    public ResponseEntity<SobaResponseDto> createSoba(@PathVariable("id") Long hotelId ,@RequestBody @Valid SobaCreateDto sobaDto) {
        SobaResponseDto sobaResponseDto = sobaService.createSoba(hotelId, sobaDto);
        return new ResponseEntity<>(sobaResponseDto, HttpStatus.OK);
    }

    @PutMapping("/admin/softDelete/{id}")
    public ResponseEntity<Void> softDeleteSoba(@PathVariable("id") Long idSoba) {
        sobaService.softDeleteSoba(idSoba);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<SobaResponseDto> updateSoba(@PathVariable("id") Long idSoba, @Valid @RequestBody SobaUpdateDto sobaDto) {
        SobaResponseDto soba = sobaService.updateSoba(idSoba, sobaDto);
        return new ResponseEntity<>(soba, HttpStatus.OK);
    }
}
