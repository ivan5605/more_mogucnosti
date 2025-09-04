package hr.moremogucnosti.more_mogucnosti_backend.service.impl;

import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.HotelDetailsDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.HotelPreviewDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.HotelMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.HotelRepository;
import hr.moremogucnosti.more_mogucnosti_backend.service.HotelService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)

public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Override
    public HotelDetailsDto findDetailById(Long id) {
        Hotel hotel = hotelRepository.findByHotelIdWithSlike(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel sa ID-jem " + id + " ne postoji!!"));

        return hotelMapper.toDetailsDto(hotel);
    }

    @Override
    public List<HotelPreviewDto> findAll() {
        List<Hotel> hoteli = hotelRepository.findAllWithSlike();

        List<HotelPreviewDto> hoteliDto = hoteli
                .stream()
                .map(hotelMapper::toPreviewDto)
                .collect(Collectors.toList());

        return hoteliDto;
    }

    @Override
    public List<HotelPreviewDto> findRandom() {
        List<Long> ids = hotelRepository.findRandomHotelIds(PageRequest.of(0, 3));

        return hotelRepository.find3RandomHotelsWithSlike(ids).stream()
                .map(hotelMapper::toPreviewDto)
                .collect(Collectors.toList());
    }

    @Override
    public Hotel loadEntity(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel sa ID-jem " + id + " ne postoji!"));
        return hotel;
    }
}
