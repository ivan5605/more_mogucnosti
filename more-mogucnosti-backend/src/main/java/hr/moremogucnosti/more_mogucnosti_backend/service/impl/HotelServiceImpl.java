package hr.moremogucnosti.more_mogucnosti_backend.service.impl;

import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.*;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Grad;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.HotelMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.GradRepository;
import hr.moremogucnosti.more_mogucnosti_backend.repository.HotelRepository;
import hr.moremogucnosti.more_mogucnosti_backend.service.HotelService;
import hr.moremogucnosti.more_mogucnosti_backend.util.SlugUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)

public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final GradRepository gradRepository;
    private final SlugUtil slugUtil;

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

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public HotelResponseDto createHotel(HotelCreateDto hotelDto) {
        String gradIme = hotelDto.gradNaziv().trim();
        String slug = slugUtil.slugify(gradIme);

        Optional<Grad> gradDto = gradRepository.findBySlug(slug);

        Grad grad = gradDto.orElseGet(() -> {
            Grad g = new Grad();
            g.setImeGrad(gradIme);
            g.setSlug(slug);
            Grad savedGrad = gradRepository.save(g);
            return savedGrad;
        });

        Hotel hotel = new Hotel();
        hotel.setNaziv(hotelDto.naziv());
        hotel.setAdresa(hotelDto.adresa());
        hotel.setGrad(grad);
        hotel.setWifi(hotelDto.wifi());
        hotel.setBazen(hotelDto.bazen());
        hotel.setParking(hotelDto.parking());

        Hotel hotelSaved = hotelRepository.save(hotel);

        return hotelMapper.toResponseDto(hotelSaved);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void softDeleteHotel(Long idHotel) {
        Hotel hotel = hotelRepository.findById(idHotel)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel ne postoji."));

        hotel.setAktivno(false);
        hotelRepository.save(hotel);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public HotelResponseDto updateHotel(Long idHotel, HotelUpdateDto hotelDto) {
        Hotel hotel = hotelRepository.findById(idHotel)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel ne postoji."));

        hotel.setNaziv(hotelDto.naziv());
        hotel.setWifi(hotelDto.wifi());
        hotel.setBazen(hotelDto.bazen());
        hotel.setParking(hotelDto.parking());

        Hotel savedHotel = hotelRepository.save(hotel);
        return hotelMapper.toResponseDto(savedHotel);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void aktivirajHotel(Long idHotel) {
        Hotel hotel = hotelRepository.findById(idHotel)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel ne postoji."));

        hotel.setAktivno(true);
        hotelRepository.save(hotel);
    }
}
