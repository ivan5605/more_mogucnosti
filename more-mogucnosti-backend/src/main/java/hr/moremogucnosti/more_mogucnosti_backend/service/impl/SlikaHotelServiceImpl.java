package hr.moremogucnosti.more_mogucnosti_backend.service.impl;

import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaHotelResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;
import hr.moremogucnosti.more_mogucnosti_backend.entity.HotelSlika;
import hr.moremogucnosti.more_mogucnosti_backend.exception.DuplicateException;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.SlikaMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.HotelRepository;
import hr.moremogucnosti.more_mogucnosti_backend.repository.SlikaHotelRepository;
import hr.moremogucnosti.more_mogucnosti_backend.service.SlikaHotelService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)

public class SlikaHotelServiceImpl implements SlikaHotelService {

    private final SlikaHotelRepository repository;
    private final HotelRepository hotelRepository;
    private final SlikaMapper mapper;

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public SlikaHotelResponseDto addSlikaHotel(Long hotelId, SlikaCreateDto createDto) {
        if (createDto.glavna()){
            repository.ocistiGlavnu(hotelId);
        }

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel ne postoji"));

        HotelSlika slika = new HotelSlika();

        slika.setPutanja(createDto.putanja().trim());
        slika.setHotel(hotel);
        slika.setGlavnaSlika(createDto.glavna());

        //safety net - hvata greske ako glavna provjera zakaze, "mreza"
        try {
            HotelSlika spremljenaSlika = repository.save(slika);
            repository.flush();
            return mapper.toHotelResponseDto(spremljenaSlika, hotel);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateException("Ovaj hotel već ima glavnu sliku");
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteSlikaHotel(Long idSlika) {
        HotelSlika slika = repository.findById(idSlika)
                .orElseThrow(() -> new ResourceNotFoundException("Slika ne postoji."));

        Hotel hotel = slika.getHotel();
        Long hotelId = hotel.getId();

        if (slika.isGlavnaSlika()) {
            Optional<HotelSlika> nova = repository.findFirstByHotelIdAndGlavnaSlikaFalseOrderByIdAsc(slika.getHotel().getId());

            if (nova.isPresent()) {
                repository.ocistiGlavnu(hotelId);
                HotelSlika s = nova.get();
                s.setGlavnaSlika(true);
                repository.save(s);
                repository.flush();
            }
        }

        repository.delete(slika);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public SlikaResponseDto setGlavna(Long idSlika) {
        HotelSlika slika = repository.findById(idSlika)
                .orElseThrow(() -> new ResourceNotFoundException("Slika ne postoji."));

        Hotel hotel = slika.getHotel();
        Long hotelId = hotel.getId();

        if (slika.isGlavnaSlika()) {
            return mapper.toResponseDto(slika);
        }

        repository.ocistiGlavnu(hotelId);

        try {
            slika.setGlavnaSlika(true);
            repository.save(slika);
            repository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateException("Ovaj hotel već ima glavnu sliku");
        }

        return mapper.toResponseDto(slika);
    }
}

//flush gurne SQL do baze, zbog toga dobim ranije grešku iz baze, s flush je uhvatim odmah i dobim lijep 409
//sve promjene koje JPA drzi u memotiji odmah se salju u DB
//ne radi commit, još uvijek sam u transakciji i morem napraviti rollback
//ako ocu sad i odmah provjeriti da DB prihvaca promjenu ili da sljedeci SELECT vec vidi promjenu
