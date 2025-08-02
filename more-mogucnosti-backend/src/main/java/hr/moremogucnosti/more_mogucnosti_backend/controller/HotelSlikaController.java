package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.HotelSlikaDto;
import hr.moremogucnosti.more_mogucnosti_backend.service.HotelSlikaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/hotelSlika")
@CrossOrigin("http://localhost:3000")

//jel ovo uopce treba??

public class HotelSlikaController {

    private final HotelSlikaService hotelSlikaService;

    @GetMapping("{id}")
    public ResponseEntity<HotelSlikaDto> getGlavnaSlika(@PathVariable("id") Long id){
        HotelSlikaDto hotelSlikaDto = hotelSlikaService.getGlavnaSlika(id);
        return new ResponseEntity<>(hotelSlikaDto, HttpStatus.OK);
    }
}
