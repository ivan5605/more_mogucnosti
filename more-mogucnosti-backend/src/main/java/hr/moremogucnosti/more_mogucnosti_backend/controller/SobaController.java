package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaDetailsDto;
import hr.moremogucnosti.more_mogucnosti_backend.service.SobaService;
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

    @GetMapping("/hotel/random/{id}")
    public ResponseEntity<List<SobaResponseDto>> getRandomSobe(@PathVariable("id") Long id){
        List<SobaResponseDto> sobe = sobaService.findRandomByIdHotel(id);
        return new ResponseEntity<>(sobe, HttpStatus.OK);
    }

    @GetMapping("/withHotelAndSlike/{id}")
    public ResponseEntity<SobaDetailsDto> getDetailsSoba(@PathVariable("id") Long id){
        SobaDetailsDto sobaDetailsDto = sobaService.findDetailsById(id);
        return new ResponseEntity<>(sobaDetailsDto,HttpStatus.OK);
    }
}
