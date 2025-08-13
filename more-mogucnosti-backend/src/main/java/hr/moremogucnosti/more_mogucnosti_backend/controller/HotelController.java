package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.HotelResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.service.HotelService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotel")
@CrossOrigin("http://localhost:3000")

@AllArgsConstructor

public class HotelController {

    private final HotelService hotelService;

    @GetMapping("{id}")
    public ResponseEntity<HotelResponseDto> getHotel(@PathVariable("id") Long id){
        HotelResponseDto hotelDto = hotelService.getDto(id);
        return new ResponseEntity<>(hotelDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<HotelResponseDto>> getAllHotels(){
        List<HotelResponseDto> hoteliDto = hotelService.getAllHotels();
        return new ResponseEntity<>(hoteliDto, HttpStatus.OK);
    }

    @GetMapping("/random")
    public List<HotelResponseDto> getRandomHotels(){
        return hotelService.getRandomHotels();
    }
}
