package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.service.SlikaHotelService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/slikaHotel")
@CrossOrigin("http://localhost:3000")

@AllArgsConstructor

public class SlikaHotelController {

    private final SlikaHotelService service;

}
