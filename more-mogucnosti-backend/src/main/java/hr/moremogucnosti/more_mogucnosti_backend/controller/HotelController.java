package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.*;
import hr.moremogucnosti.more_mogucnosti_backend.service.HotelService;
import jakarta.validation.Valid;
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
        HotelDetailsDto hotelDto = hotelService.
                findDetailById(id);
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

    @PostMapping("/admin/create")
    public ResponseEntity<HotelResponseDto> createHotel(@RequestBody @Valid HotelCreateDto hotelDto) {
        HotelResponseDto hotel = hotelService.createHotel(hotelDto);
        return new ResponseEntity<>(hotel, HttpStatus.CREATED);
    }

    @PutMapping("/admin/softDelete/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable("id") Long id){
        hotelService.softDeleteHotel(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<HotelResponseDto> updateHotel(@PathVariable("id") Long id, @RequestBody @Valid HotelUpdateDto hotelDto) {
        HotelResponseDto hotel = hotelService.updateHotel(id, hotelDto);
        return new ResponseEntity<>(hotel, HttpStatus.OK);
    }
}
