package hr.moremogucnosti.more_mogucnosti_backend.service.impl;

import hr.moremogucnosti.more_mogucnosti_backend.dto.HotelDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.HotelSlikaDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.HotelMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.HotelRepository;
import hr.moremogucnosti.more_mogucnosti_backend.service.HotelService;
import hr.moremogucnosti.more_mogucnosti_backend.service.HotelSlikaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final HotelSlikaService hotelSlikaService;

    @Override
    public HotelDto getHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel sa ID-jem " + id + " ne postoji!!"));

        String grad = hotel.getGrad().getImeGrad();
        HotelDto hotelDto = hotelMapper.toHotelDto(hotel);
        hotelDto.setGrad(grad);

        return hotelDto;
    }

    @Override
    public List<HotelDto> getAllHotels() {
        List<Hotel> hoteli = hotelRepository.findAll();
        List<HotelDto> hoteliDto = new ArrayList<>();

        for (Hotel hotel : hoteli){
            HotelDto hotelDto = hotelMapper.toHotelDto(hotel);
            hotelDto.setGrad(hotel.getGrad().getImeGrad());
            hoteliDto.add(hotelDto);
        }

        //List<HotelDto> hoteliDto = hoteli.stream().map((hotel) -> hotelMapper.toHotelDto(hotel)).collect(Collectors.toList());

        List<HotelDto> hoteliDto2 = hoteli.stream()
                .map((hotel) -> {
                    HotelDto dto = hotelMapper.toHotelDto(hotel);
                    dto.setGrad(hotel.getGrad().getImeGrad());
                    HotelSlikaDto hotelSlikaDto = hotelSlikaService.getGlavnaSlika(dto.getId());
                    if (hotelSlikaDto != null) {
                        dto.setGlavnaSlikaPutanja(hotelSlikaDto.getPutanja());
                    }
                    return dto;
                }).collect(Collectors.toList());

        return hoteliDto2;
    }

    @Override
    public List<HotelDto> getRandomHotels() {
        //List<Hotel> hoteli = hotelRepository.find3RandomHotels();
        //List<HotelDto> hotelDtos = hoteli.stream().map((hotel) -> hotelMapper.toHotelDto(hotel)).collect(Collectors.toList());

        return hotelRepository.find3RandomHotels().stream()
                .map((hotel) -> {
                    HotelDto hotelDto = hotelMapper.toHotelDto(hotel);
                    hotelDto.setGrad(hotel.getGrad().getImeGrad());
                    HotelSlikaDto hotelSlikaDto = hotelSlikaService.getGlavnaSlika(hotel.getId());
                    hotelDto.setGlavnaSlikaPutanja(hotelSlikaDto.getPutanja());
                    return  hotelDto;
                }).collect(Collectors.toList());
    }
}
