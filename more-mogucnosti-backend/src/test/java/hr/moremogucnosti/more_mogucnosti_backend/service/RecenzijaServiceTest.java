package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.recenzija.RecenzijaHotelStatusDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.recenzija.RecenzijaZaHotelDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.recenzija.RecenzijaZaKorisnikDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Recenzija;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.RecenzijaMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.RecenzijaRepository;
import hr.moremogucnosti.more_mogucnosti_backend.service.impl.RecenzijaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecenzijaServiceTest {

    @Mock
    RecenzijaRepository repository;

    @Mock
    RecenzijaMapper mapper;

    @InjectMocks
    RecenzijaServiceImpl service;


    @Test
    void findRecenzijeStatus() {
        Long hotelId = 5L;
        RecenzijaHotelStatusDto ocekivano = new RecenzijaHotelStatusDto(3L, 4.5);

        when(repository.recenzijeInfoByHotelId(hotelId)).thenReturn(ocekivano);

        RecenzijaHotelStatusDto rezultat = service.findRecenzijeStatus(hotelId);

        assertEquals(ocekivano, rezultat);
        verify(repository).recenzijeInfoByHotelId(hotelId);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper);
    }


    @Test
    void findAllWithHotelById() {
        Long korisnikId = 1L;

        Recenzija r1 = new Recenzija();
        r1.setId(10L);
        r1.setOcjena(5);
        r1.setTekst("Super");
        r1.setDatum(LocalDate.of(2025, 10, 10));

        Recenzija r2 = new Recenzija();
        r2.setId(11L);
        r2.setOcjena(3);
        r2.setTekst("Ok");
        r2.setDatum(LocalDate.of(2025, 11, 11));

        RecenzijaZaKorisnikDto dto1 = new RecenzijaZaKorisnikDto(10L, null, 5, "Super", LocalDate.of(2025, 10, 10));
        RecenzijaZaKorisnikDto dto2 = new RecenzijaZaKorisnikDto(11L, null, 3, "Ok", LocalDate.of(2025, 11, 11));

        when(repository.findWithHotelByKorisnikId(korisnikId)).thenReturn(List.of(r1, r2));
        when(mapper.toZaKorisnikaDto(r1)).thenReturn(dto1);
        when(mapper.toZaKorisnikaDto(r2)).thenReturn(dto2);

        List<RecenzijaZaKorisnikDto> rezultat = service.findAllWithHotelById(korisnikId);

        assertEquals(2, rezultat.size());
        assertTrue(rezultat.containsAll(List.of(dto1, dto2)));
        verify(repository).findWithHotelByKorisnikId(korisnikId);
        verify(mapper).toZaKorisnikaDto(r1);
        verify(mapper).toZaKorisnikaDto(r2);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void findAllWithHotelById_prazno() {
        Long korisnikId = 1L;
        when(repository.findWithHotelByKorisnikId(korisnikId)).thenReturn(List.of());

        List<RecenzijaZaKorisnikDto> rezultat = service.findAllWithHotelById(korisnikId);

        assertTrue(rezultat.isEmpty());
        verify(repository).findWithHotelByKorisnikId(korisnikId);
        verifyNoInteractions(mapper);
    }

    @Test
    void findAllByKorisnikId() {
        Long korisnikId = 2L;

        Recenzija r1 = new Recenzija();
        r1.setId(20L);
        r1.setDatum(LocalDate.of(2025, 11, 11));

        Recenzija r2 = new Recenzija();
        r2.setId(21L);
        r2.setDatum(LocalDate.of(2025, 10, 10));

        RecenzijaZaKorisnikDto dto1 = new RecenzijaZaKorisnikDto(20L, null, 0, null, LocalDate.of(2025, 11, 11));
        RecenzijaZaKorisnikDto dto2 = new RecenzijaZaKorisnikDto(21L, null, 0, null, LocalDate.of(2025, 10, 10));

        when(repository.findAllByKorisnikIdOrderByDatumDesc(korisnikId)).thenReturn(List.of(r1, r2));
        when(mapper.toZaKorisnikaDto(r1)).thenReturn(dto1);
        when(mapper.toZaKorisnikaDto(r2)).thenReturn(dto2);

        List<RecenzijaZaKorisnikDto> rezultat = service.findAllByKorisnikId(korisnikId);

        assertEquals(2, rezultat.size());
        assertEquals(20L, rezultat.get(0).id());
        assertEquals(21L, rezultat.get(1).id());
        verify(repository).findAllByKorisnikIdOrderByDatumDesc(korisnikId);
        verify(mapper).toZaKorisnikaDto(r1);
        verify(mapper).toZaKorisnikaDto(r2);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void findAllByKorisnikId_prazno() {
        Long korisnikId = 2L;
        when(repository.findAllByKorisnikIdOrderByDatumDesc(korisnikId)).thenReturn(List.of());

        List<RecenzijaZaKorisnikDto> rezultat = service.findAllByKorisnikId(korisnikId);

        assertTrue(rezultat.isEmpty());
        verify(repository).findAllByKorisnikIdOrderByDatumDesc(korisnikId);
        verifyNoInteractions(mapper);
    }


    @Test
    void findAllByHotelId() {
        Long hotelId = 7L;

        Recenzija r1 = new Recenzija();
        r1.setId(30L);

        Recenzija r2 = new Recenzija();
        r2.setId(31L);

        RecenzijaZaHotelDto dto1 = new RecenzijaZaHotelDto(30L, null, 5, "Odlično", LocalDate.of(2025, 1, 1));
        RecenzijaZaHotelDto dto2 = new RecenzijaZaHotelDto(31L, null, 4, "Dobro", LocalDate.of(2025, 2, 2));

        when(repository.findByHotelIdWithKorisnik(hotelId)).thenReturn(List.of(r1, r2));
        when(mapper.toZaHotelDto(r1)).thenReturn(dto1);
        when(mapper.toZaHotelDto(r2)).thenReturn(dto2);

        List<RecenzijaZaHotelDto> rezultat = service.findAllByHotelId(hotelId);

        assertEquals(2, rezultat.size());
        assertTrue(rezultat.containsAll(List.of(dto1, dto2)));
        verify(repository).findByHotelIdWithKorisnik(hotelId);
        verify(mapper).toZaHotelDto(r1);
        verify(mapper).toZaHotelDto(r2);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void findAllByHotelId_prazno() {
        Long hotelId = 7L;
        when(repository.findByHotelIdWithKorisnik(hotelId)).thenReturn(List.of());

        List<RecenzijaZaHotelDto> rezultat = service.findAllByHotelId(hotelId);

        assertTrue(rezultat.isEmpty());
        verify(repository).findByHotelIdWithKorisnik(hotelId);
        verifyNoInteractions(mapper);
    }
}
