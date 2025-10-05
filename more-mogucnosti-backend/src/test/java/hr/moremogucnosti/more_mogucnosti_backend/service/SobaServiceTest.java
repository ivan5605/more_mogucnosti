package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Soba;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.SobaMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.SobaRepository;
import hr.moremogucnosti.more_mogucnosti_backend.service.impl.SobaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SobaServiceTest {

    @Mock
    SobaRepository repository;

    @Mock
    SobaMapper mapper;

    @InjectMocks
    SobaServiceImpl service;

    @Test
    void findById() {
        Hotel hotel = new Hotel();
        hotel.setId(1L); hotel.setNaziv("Hotel");

        Soba soba = new Soba();
        soba.setId(1L); soba.setKapacitet(2); soba.setCijenaNocenja(BigDecimal.valueOf(70)); soba.setHotel(hotel); soba.setBrojSobe(100);

        SobaResponseDto responseDto = new SobaResponseDto(
                soba.getId(), soba.getKapacitet(), soba.getCijenaNocenja(), soba.getBrojSobe(), true, true, null, null, true
        );

        when(repository.findByIdWithSlike(1L)).thenReturn(Optional.of(soba));
        when(mapper.toResponseDto(soba)).thenReturn(responseDto);

        var rezultat = service.findById(1L);

        assertEquals(2, rezultat.kapacitet());
        verify(repository).findByIdWithSlike(1L);
        verify(mapper).toResponseDto(soba);
        verifyNoMoreInteractions(mapper, repository);
    }

    @Test
    void findById_notFound() {
        when(repository.findByIdWithSlike(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));

        verify(repository).findByIdWithSlike(99L);
        verifyNoInteractions(mapper);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findAllByIdHotel() {
        Long hotelId = 7L;

        Hotel hotel = new Hotel();
        hotel.setId(hotelId);
        hotel.setNaziv("Hotel");

        Soba soba1 = new Soba();
        soba1.setId(1L);
        soba1.setKapacitet(2);
        soba1.setCijenaNocenja(BigDecimal.valueOf(70));
        soba1.setHotel(hotel);
        soba1.setBrojSobe(100);

        Soba soba2 = new Soba();
        soba2.setId(2L);
        soba2.setKapacitet(3);
        soba2.setCijenaNocenja(BigDecimal.valueOf(120));
        soba2.setHotel(hotel);
        soba2.setBrojSobe(101);

        SobaResponseDto dto1 = new SobaResponseDto(
                soba1.getId(), soba1.getKapacitet(), soba1.getCijenaNocenja(),
                soba1.getBrojSobe(), true, true, null, null, true
        );
        SobaResponseDto dto2 = new SobaResponseDto(
                soba2.getId(), soba2.getKapacitet(), soba2.getCijenaNocenja(),
                soba2.getBrojSobe(), false, true, null, null, true
        );

        when(repository.findByHotelIdWithSlike(hotelId)).thenReturn(List.of(soba1, soba2));
        when(mapper.toResponseDto(soba1)).thenReturn(dto1);
        when(mapper.toResponseDto(soba2)).thenReturn(dto2);

        List<SobaResponseDto> rezultat = service.findAllByIdHotel(hotelId);

        assertEquals(2, rezultat.size());
        verify(repository).findByHotelIdWithSlike(hotelId);
        verify(mapper).toResponseDto(soba1);
        verify(mapper).toResponseDto(soba2);
        verifyNoMoreInteractions(mapper, repository);
    }

    @Test
    void findAllByIdHotel_notFound() {
        Long hotelId = 99L;
        when(repository.findByHotelIdWithSlike(hotelId)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> service.findAllByIdHotel(hotelId));

        verify(repository).findByHotelIdWithSlike(hotelId);
        verifyNoInteractions(mapper);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void loadEntity() {
        Long sobaId = 8L;

        Soba soba = new Soba();
        soba.setId(sobaId);

        when(repository.findById(sobaId)).thenReturn(Optional.of(soba));

        Soba rezultat = service.loadEntity(sobaId);

        assertEquals(sobaId, rezultat.getId());
        verify(repository).findById(sobaId);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void loadEntity_notFound() {
        Long sobaId = 99L;
        when(repository.findById(sobaId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.loadEntity(sobaId));

        org.mockito.Mockito.verify(repository).findById(sobaId);
        org.mockito.Mockito.verifyNoMoreInteractions(repository);
    }
}