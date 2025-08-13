package hr.moremogucnosti.more_mogucnosti_backend.service.impl;

import hr.moremogucnosti.more_mogucnosti_backend.dto.HotelResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.HotelMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.HotelRepository;
import hr.moremogucnosti.more_mogucnosti_backend.service.HotelService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Override
    public HotelResponseDto getDto(Long id) {
        Hotel hotel = hotelRepository.findByHotelIdWithSlike(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel sa ID-jem " + id + " ne postoji!!"));

        return hotelMapper.toHotelResponseDto(hotel);
    }

    @Override
    public List<HotelResponseDto> getAllHotels() {
        List<Hotel> hoteli = hotelRepository.findAllWithSlike();
//        List<HotelDto> hoteliDto = new ArrayList<>();

//        for (Hotel hotel : hoteli){
//            HotelDto hotelDto = hotelMapper.toHotelDto(hotel);
//            hotelDto.setGrad(hotel.getGrad().getImeGrad());
//            hoteliDto.add(hotelDto);
//        }

        //List<HotelDto> hoteliDto = hoteli.stream().map((hotel) -> hotelMapper.toHotelDto(hotel)).collect(Collectors.toList());

        List<HotelResponseDto> hoteliDto = hoteli
                .stream()
                .map(hotelMapper::toHotelResponseDto)
                .collect(Collectors.toList());

        return hoteliDto;
    }

    @Override
    public List<HotelResponseDto> getRandomHotels() {
        //List<Hotel> hoteli = hotelRepository.find3RandomHotels();
        //List<HotelDto> hotelDtos = hoteli.stream().map((hotel) -> hotelMapper.toHotelDto(hotel)).collect(Collectors.toList());

        List<Long> ids = hotelRepository.findRandomHotelIds(PageRequest.of(0, 3));

        return hotelRepository.find3RandomHotelsWithSlike(ids).stream()
                .map(hotelMapper::toHotelResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Hotel getEntity(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel sa ID-jem " + id + " ne postoji!"));
        return hotel;
    }
}
