package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaDatumDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaDetailsDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Rezervacija;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Soba;
import hr.moremogucnosti.more_mogucnosti_backend.exception.DuplicateException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.RezervacijaMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.RezervacijaRepository;
import hr.moremogucnosti.more_mogucnosti_backend.security.AppUserPrincipal;
import hr.moremogucnosti.more_mogucnosti_backend.service.impl.RezervacijaServiceImpl;
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
class RezervacijaServiceTest {

    @Mock
    RezervacijaRepository repository;

    @Mock
    RezervacijaMapper mapper;

    @Mock
    SobaService sobaService;

    @Mock
    KorisnikService korisnikService;

    @InjectMocks
    RezervacijaServiceImpl service;

    @Test
    void findAll() {
    }

    @Test
    void createRezervacija() {
        // arrange
        var dto = new RezervacijaCreateDto(
                10L,
                2,
                LocalDate.of(2025, 10, 10),
                LocalDate.of(2025, 10, 12)
        );

        var principal = new AppUserPrincipal(1L, "user@email.com", "password", List.of());

        Soba soba = new Soba();
        soba.setId(1L);
        soba.setKapacitet(3);

        Korisnik korisnik = new Korisnik();
        korisnik.setId(1L);

        Rezervacija rez = new Rezervacija();
        rez.setSoba(soba);
        rez.setKorisnik(korisnik);
        rez.setDatumPocetak(dto.datumPocetak());
        rez.setDatumKraj(dto.datumKraj());

        Rezervacija saved = new Rezervacija();
        saved.setId(1L);
        saved.setSoba(soba);
        saved.setKorisnik(korisnik);
        saved.setDatumPocetak(dto.datumPocetak());
        saved.setDatumKraj(dto.datumKraj());

        RezervacijaDetailsDto expected = new RezervacijaDetailsDto(
                1L,
                null,
                null,
                dto.brojOsoba(),
                dto.datumPocetak(),
                dto.datumKraj()
        );

        when(sobaService.loadEntity(dto.sobaId())).thenReturn(soba);
        when(korisnikService.loadEntity(principal.getId())).thenReturn(korisnik);
        when(mapper.fromCreateDto(dto, soba, korisnik)).thenReturn(rez);
        when(repository.postojiPreklapanje(soba.getId(), dto.datumPocetak(), dto.datumKraj())).thenReturn(false);
        when(repository.save(rez)).thenReturn(saved);
        when(mapper.toDetailsDto(saved)).thenReturn(expected);

        // act
        var rezultat = service.createRezervacija(dto, principal);

        // assert
        assertEquals(1L, rezultat.id());
        verify(sobaService).loadEntity(dto.sobaId());
        verify(korisnikService).loadEntity(principal.getId());
        verify(repository).postojiPreklapanje(soba.getId(), dto.datumPocetak(), dto.datumKraj());
        verify(repository).save(rez);
        verify(mapper).fromCreateDto(dto, soba, korisnik);
        verify(mapper).toDetailsDto(saved);
        verifyNoMoreInteractions(repository, mapper, sobaService, korisnikService);
    }


    @Test
    void createRezervacija_throwsDuplicate() {
        var dto = new RezervacijaCreateDto(10L, 2,
                LocalDate.of(2025, 10, 10), LocalDate.of(2025, 10, 12));
        var principal = new AppUserPrincipal(1L, "email", "password", List.of());

        Soba soba = new Soba();
        soba.setId(10L);
        soba.setKapacitet(3);

        Korisnik korisnik = new Korisnik();

        Rezervacija rez = new Rezervacija();
        rez.setSoba(soba); rez.setKorisnik(korisnik);
        rez.setDatumPocetak(dto.datumPocetak()); rez.setDatumKraj(dto.datumKraj());

        when(sobaService.loadEntity(dto.sobaId())).thenReturn(soba);
        when(korisnikService.loadEntity(principal.getId())).thenReturn(korisnik);
        when(mapper.fromCreateDto(dto, soba, korisnik)).thenReturn(rez);
        when(repository.postojiPreklapanje(soba.getId(), dto.datumPocetak(), dto.datumKraj())).thenReturn(true);

        assertThrows(DuplicateException.class, () -> service.createRezervacija(dto, principal));

        verify(repository).postojiPreklapanje(soba.getId(), dto.datumPocetak(), dto.datumKraj());
        verify(repository, never()).save(any());
        verify(mapper).fromCreateDto(dto, soba, korisnik);
    }

    @Test
    void findAllZauzetiDatumi() {
        Long sobaId = 10L;

        var r1 = new Rezervacija();
        r1.setDatumPocetak(LocalDate.of(2025, 10, 10));
        r1.setDatumKraj(LocalDate.of(2025, 10, 12));

        var r2 = new Rezervacija();
        r2.setDatumPocetak(LocalDate.of(2025, 11, 1));
        r2.setDatumKraj(LocalDate.of(2025, 11, 5));

        var d1 = new RezervacijaDatumDto(1L, r1.getDatumPocetak(), r1.getDatumKraj());
        var d2 = new RezervacijaDatumDto(2L, r2.getDatumPocetak(), r2.getDatumKraj());

        when(repository.findAllBySobaId(sobaId)).thenReturn(List.of(r1, r2));
        when(mapper.toDatumDto(r1)).thenReturn(d1);
        when(mapper.toDatumDto(r2)).thenReturn(d2);

        var rezultat = service.findAllZauzetiDatumi(sobaId);

        assertEquals(2, rezultat.size());
        assertTrue(rezultat.containsAll(List.of(d1, d2)));

        verify(repository).findAllBySobaId(sobaId);
        verify(mapper).toDatumDto(r1);
        verify(mapper).toDatumDto(r2);
        verifyNoMoreInteractions(repository, mapper);
    }
}