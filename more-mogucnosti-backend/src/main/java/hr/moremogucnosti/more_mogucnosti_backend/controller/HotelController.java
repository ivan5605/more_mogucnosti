package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.HotelDetailsDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.HotelPreviewDto;
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
    public ResponseEntity<HotelDetailsDto> getHotel(@PathVariable("id") Long id){
        HotelDetailsDto hotelDto = hotelService.findDetailById(id);
        return new ResponseEntity<>(hotelDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<HotelPreviewDto>> getAllHoteli(){
        List<HotelPreviewDto> hoteliDto = hotelService.findAll();
        return new ResponseEntity<>(hoteliDto, HttpStatus.OK);
    }

    @GetMapping("/random")
    public List<HotelPreviewDto> getRandomHoteli(){
        return hotelService.findRandom();
    }
}
