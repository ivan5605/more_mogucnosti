package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaHotelResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;
import hr.moremogucnosti.more_mogucnosti_backend.entity.HotelSlika;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.SlikaMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.HotelRepository;
import hr.moremogucnosti.more_mogucnosti_backend.repository.SlikaHotelRepository;
import hr.moremogucnosti.more_mogucnosti_backend.service.impl.SlikaHotelServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SlikaHotelServiceTest {

    @Mock
    SlikaHotelRepository repository;

    @Mock
    HotelRepository hotelRepository;

    @Mock
    SlikaMapper mapper;

    @InjectMocks
    SlikaHotelServiceImpl service;


    @Test
    void addSlikaHotel_glavna_ocistiGlavnu() {
        Long hotelId = 10L;
        SlikaCreateDto createDto = new SlikaCreateDto("  p1  ", true);

        Hotel hotel = new Hotel();
        hotel.setId(hotelId);

        HotelSlika spremljena = new HotelSlika();
        spremljena.setId(5L);
        spremljena.setHotel(hotel);
        spremljena.setPutanja("p1");
        spremljena.setGlavnaSlika(true);

        SlikaHotelResponseDto ocekivano = new SlikaHotelResponseDto(5L, "p1", true, null);

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(repository.save(any(HotelSlika.class))).thenReturn(spremljena);
        when(mapper.toHotelResponseDto(spremljena, hotel)).thenReturn(ocekivano);

        SlikaHotelResponseDto rezultat = service.addSlikaHotel(hotelId, createDto);

        assertEquals(5L, rezultat.id());
        verify(repository).ocistiGlavnu(hotelId);
        verify(hotelRepository).findById(hotelId);
        verify(repository).save(any(HotelSlika.class));
        verify(repository).flush();
        verify(mapper).toHotelResponseDto(spremljena, hotel);
        verifyNoMoreInteractions(repository, hotelRepository, mapper);
    }

    @Test
    void addSlikaHotel_glavna_false() {
        Long hotelId = 11L;
        SlikaCreateDto createDto = new SlikaCreateDto("p", false);

        Hotel hotel = new Hotel();
        hotel.setId(hotelId);

        HotelSlika spremljena = new HotelSlika();
        spremljena.setId(7L);
        spremljena.setHotel(hotel);
        spremljena.setPutanja("p");
        spremljena.setGlavnaSlika(false);

        SlikaHotelResponseDto dto = new SlikaHotelResponseDto(7L, "p", false, null);

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(repository.save(any(HotelSlika.class))).thenReturn(spremljena);
        when(mapper.toHotelResponseDto(spremljena, hotel)).thenReturn(dto);

        SlikaHotelResponseDto rezultat = service.addSlikaHotel(hotelId, createDto);

        assertEquals(7L, rezultat.id());
        verify(repository, never()).ocistiGlavnu(anyLong());
        verify(hotelRepository).findById(hotelId);
        verify(repository).save(any(HotelSlika.class));
        verify(repository).flush();
        verify(mapper).toHotelResponseDto(spremljena, hotel);
        verifyNoMoreInteractions(repository, hotelRepository, mapper);
    }

    @Test
    void addSlikaHotel_hotel_notFound() {
        Long hotelId = 99L;
        SlikaCreateDto createDto = new SlikaCreateDto("p", true);

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.addSlikaHotel(hotelId, createDto));

        verify(hotelRepository).findById(hotelId);
        verifyNoMoreInteractions(hotelRepository);
        verifyNoInteractions(repository, mapper);
    }

    @Test
    void deleteSlikaHotel_glavna_novaGlavna() {
        Long slikaId = 20L;
        Long hotelId = 2L;

        Hotel hotel = new Hotel(); hotel.setId(hotelId);

        HotelSlika glavna = new HotelSlika();
        glavna.setId(slikaId);
        glavna.setHotel(hotel);
        glavna.setGlavnaSlika(true);

        HotelSlika zamjena = new HotelSlika();
        zamjena.setId(21L);
        zamjena.setHotel(hotel);
        zamjena.setGlavnaSlika(false);

        when(repository.findById(slikaId)).thenReturn(Optional.of(glavna));
        when(repository.findFirstByHotelIdAndGlavnaSlikaFalseOrderByIdAsc(hotelId)).thenReturn(Optional.of(zamjena));

        service.deleteSlikaHotel(slikaId);

        verify(repository).findById(slikaId);
        verify(repository).findFirstByHotelIdAndGlavnaSlikaFalseOrderByIdAsc(hotelId);
        verify(repository).ocistiGlavnu(hotelId);
        verify(repository).save(zamjena);
        verify(repository).flush();
        verify(repository).delete(glavna);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper, hotelRepository);
    }

    @Test
    void deleteSlikaHotel_notGlavna() {
        Long slikaId = 30L;

        Hotel hotel = new Hotel(); hotel.setId(3L);

        HotelSlika sporedna = new HotelSlika();
        sporedna.setId(slikaId);
        sporedna.setHotel(hotel);
        sporedna.setGlavnaSlika(false);

        when(repository.findById(slikaId)).thenReturn(Optional.of(sporedna));

        service.deleteSlikaHotel(slikaId);

        verify(repository).findById(slikaId);
        verify(repository, never()).findFirstByHotelIdAndGlavnaSlikaFalseOrderByIdAsc(anyLong());
        verify(repository, never()).ocistiGlavnu(anyLong());
        verify(repository, never()).save(any(HotelSlika.class));
        verify(repository, never()).flush();
        verify(repository).delete(sporedna);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteSlikaHotel_notFound() {
        Long slikaId = 404L;
        when(repository.findById(slikaId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.deleteSlikaHotel(slikaId));

        verify(repository).findById(slikaId);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper, hotelRepository);
    }

    @Test
    void setGlavna_stara() {
        Long slikaId = 40L;

        Hotel hotel = new Hotel(); hotel.setId(4L);

        HotelSlika slika = new HotelSlika();
        slika.setId(slikaId);
        slika.setHotel(hotel);
        slika.setGlavnaSlika(true);

        SlikaResponseDto dto = new SlikaResponseDto(slikaId, "p", true);

        when(repository.findById(slikaId)).thenReturn(Optional.of(slika));
        when(mapper.toResponseDto(slika)).thenReturn(dto);

        SlikaResponseDto rezultat = service.setGlavna(slikaId);

        assertEquals(slikaId, rezultat.id());
        verify(repository).findById(slikaId);
        verify(mapper).toResponseDto(slika);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void setGlavna_nova() {
        Long slikaId = 41L;
        Long hotelId = 5L;

        Hotel hotel = new Hotel(); hotel.setId(hotelId);

        HotelSlika slika = new HotelSlika();
        slika.setId(slikaId);
        slika.setHotel(hotel);
        slika.setGlavnaSlika(false);

        SlikaResponseDto dto = new SlikaResponseDto(slikaId, "p", true);

        when(repository.findById(slikaId)).thenReturn(Optional.of(slika));
        when(mapper.toResponseDto(slika)).thenReturn(dto);

        SlikaResponseDto rezultat = service.setGlavna(slikaId);

        assertTrue(slika.isGlavnaSlika());
        assertEquals(slikaId, rezultat.id());
        verify(repository).findById(slikaId);
        verify(repository).ocistiGlavnu(hotelId);
        verify(repository).save(slika);
        verify(repository).flush();
        verify(mapper).toResponseDto(slika);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void setGlavna_notFound() {
        Long slikaId = 999L;
        when(repository.findById(slikaId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.setGlavna(slikaId));

        verify(repository).findById(slikaId);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper, hotelRepository);
    }
}
