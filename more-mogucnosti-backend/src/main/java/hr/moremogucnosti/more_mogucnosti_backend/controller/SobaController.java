package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.SobaDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.SobaZaRezervacijuDto;
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
    public ResponseEntity<SobaDto> getSoba(@PathVariable("id") Long id){
        SobaDto sobaDto = sobaService.getSoba(id);
        return new ResponseEntity<>(sobaDto, HttpStatus.OK);
    }

    @GetMapping("/hotel/{id}")
    public ResponseEntity<List<SobaDto>> getSobeHotela(@PathVariable("id") Long id){
        List<SobaDto> sobeHotela = sobaService.getSobeHotela(id);
        return new ResponseEntity<>(sobeHotela, HttpStatus.OK);
    }

    @GetMapping("/hotel/random/{id}")
    public ResponseEntity<List<SobaDto>> getRandomSobe(@PathVariable("id") Long id){
        List<SobaDto> sobe = sobaService.getRandomSobeHotela(id);
        return new ResponseEntity<>(sobe, HttpStatus.OK);
    }

    @GetMapping("/withHotelAndSlike/{id}")
    public ResponseEntity<SobaZaRezervacijuDto> getSobaWithHotelAndSlike(@PathVariable("id") Long id){
        SobaZaRezervacijuDto sobaZaRezervacijuDto = sobaService.getSobaWithHotelAndSlike(id);
        return new ResponseEntity<>(sobaZaRezervacijuDto,HttpStatus.OK);
    }
}
