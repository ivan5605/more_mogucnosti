package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaSobaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Soba;
import hr.moremogucnosti.more_mogucnosti_backend.entity.SobaSlika;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.SlikaMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.SlikaSobaRepository;
import hr.moremogucnosti.more_mogucnosti_backend.repository.SobaRepository;
import hr.moremogucnosti.more_mogucnosti_backend.service.impl.SlikaSobaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SlikaSobaServiceTest {

    @Mock
    SlikaSobaRepository repository;

    @Mock
    SobaRepository sobaRepository;

    @Mock
    SlikaMapper mapper;

    @InjectMocks
    SlikaSobaServiceImpl service;


    @Test
    void addSlikaSoba_glavna_ocistiGlavnu() {
        Long sobaId = 10L;
        SlikaCreateDto createDto = new SlikaCreateDto("p1", true);

        Soba soba = new Soba();
        soba.setId(sobaId);

        SobaSlika spremljena = new SobaSlika();
        spremljena.setId(5L);
        spremljena.setSoba(soba);
        spremljena.setPutanja("p1");
        spremljena.setGlavnaSlika(true);

        SlikaSobaResponseDto ocekivano = new SlikaSobaResponseDto(5L, "p1", true, null);

        when(sobaRepository.findById(sobaId)).thenReturn(Optional.of(soba));
        when(repository.save(any(SobaSlika.class))).thenReturn(spremljena);
        when(mapper.toSobaResponseDto(spremljena, soba)).thenReturn(ocekivano);

        SlikaSobaResponseDto rezultat = service.addSlikaSoba(sobaId, createDto);

        assertEquals(5L, rezultat.id());
        verify(repository).ocistiGlavnu(sobaId);
        verify(sobaRepository).findById(sobaId);
        verify(repository).save(any(SobaSlika.class));
        verify(repository).flush();
        verify(mapper).toSobaResponseDto(spremljena, soba);
        verifyNoMoreInteractions(repository, sobaRepository, mapper);
    }

    @Test
    void addSlikaSoba_glavna_false() {
        Long sobaId = 11L;
        SlikaCreateDto createDto = new SlikaCreateDto("p", false);

        Soba soba = new Soba();
        soba.setId(sobaId);

        SobaSlika spremljena = new SobaSlika();
        spremljena.setId(7L);
        spremljena.setSoba(soba);
        spremljena.setPutanja("p");
        spremljena.setGlavnaSlika(false);

        SlikaSobaResponseDto dto = new SlikaSobaResponseDto(7L, "p", false, null);

        when(sobaRepository.findById(sobaId)).thenReturn(Optional.of(soba));
        when(repository.save(any(SobaSlika.class))).thenReturn(spremljena);
        when(mapper.toSobaResponseDto(spremljena, soba)).thenReturn(dto);

        SlikaSobaResponseDto rezultat = service.addSlikaSoba(sobaId, createDto);

        assertEquals(7L, rezultat.id());
        verify(repository, never()).ocistiGlavnu(anyLong());
        verify(sobaRepository).findById(sobaId);
        verify(repository).save(any(SobaSlika.class));
        verify(repository).flush();
        verify(mapper).toSobaResponseDto(spremljena, soba);
        verifyNoMoreInteractions(repository, sobaRepository, mapper);
    }

    @Test
    void addSlikaSoba_soba_notFound() {
        Long sobaId = 99L;
        SlikaCreateDto createDto = new SlikaCreateDto("p", true);

        when(sobaRepository.findById(sobaId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.addSlikaSoba(sobaId, createDto));

        verify(sobaRepository).findById(sobaId);
        verifyNoMoreInteractions(sobaRepository);
        verifyNoInteractions(repository, mapper);
    }


    @Test
    void deleteSlikaSoba_glavna() {
        Long slikaId = 20L;
        Long sobaId = 2L;

        Soba soba = new Soba();
        soba.setId(sobaId);

        SobaSlika glavna = new SobaSlika();
        glavna.setId(slikaId);
        glavna.setSoba(soba);
        glavna.setGlavnaSlika(true);

        SobaSlika zamjena = new SobaSlika();
        zamjena.setId(21L);
        zamjena.setSoba(soba);
        zamjena.setGlavnaSlika(false);

        when(repository.findById(slikaId)).thenReturn(Optional.of(glavna));
        when(repository.findFirstBySobaIdAndGlavnaSlikaFalseOrderByIdAsc(sobaId)).thenReturn(Optional.of(zamjena));

        service.deleteSlikaSoba(slikaId);

        verify(repository).findById(slikaId);
        verify(repository).findFirstBySobaIdAndGlavnaSlikaFalseOrderByIdAsc(sobaId);
        verify(repository).ocistiGlavnu(sobaId);
        verify(repository).saveAndFlush(zamjena);
        verify(repository).delete(glavna);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper, sobaRepository);
    }

    @Test
    void deleteSlikaSoba_notGlavna() {
        Long slikaId = 30L;
        Soba soba = new Soba(); soba.setId(3L);

        SobaSlika sporedna = new SobaSlika();
        sporedna.setId(slikaId);
        sporedna.setSoba(soba);
        sporedna.setGlavnaSlika(false);

        when(repository.findById(slikaId)).thenReturn(Optional.of(sporedna));

        service.deleteSlikaSoba(slikaId);

        verify(repository).findById(slikaId);
        verify(repository, never()).findFirstBySobaIdAndGlavnaSlikaFalseOrderByIdAsc(anyLong());
        verify(repository, never()).ocistiGlavnu(anyLong());
        verify(repository, never()).saveAndFlush(any(SobaSlika.class));
        verify(repository).delete(sporedna);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteSlikaSoba_notFound() {
        Long slikaId = 99L;
        when(repository.findById(slikaId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.deleteSlikaSoba(slikaId));

        verify(repository).findById(slikaId);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper, sobaRepository);
    }


    @Test
    void setGlavna_glavna() {
        Long slikaId = 40L;
        Soba soba = new Soba(); soba.setId(4L);

        SobaSlika slika = new SobaSlika();
        slika.setId(slikaId);
        slika.setSoba(soba);
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
        Long sobaId = 5L;

        Soba soba = new Soba(); soba.setId(sobaId);

        SobaSlika slika = new SobaSlika();
        slika.setId(slikaId);
        slika.setSoba(soba);
        slika.setGlavnaSlika(false);

        SlikaResponseDto dto = new SlikaResponseDto(slikaId, "p", true);

        when(repository.findById(slikaId)).thenReturn(Optional.of(slika));
        when(mapper.toResponseDto(slika)).thenReturn(dto);

        SlikaResponseDto rezultat = service.setGlavna(slikaId);

        assertTrue(slika.isGlavnaSlika());
        assertEquals(slikaId, rezultat.id());
        verify(repository).findById(slikaId);
        verify(repository).ocistiGlavnu(sobaId);
        verify(repository).save(slika);
        verify(repository).flush();
        verify(mapper).toResponseDto(slika);
        verifyNoMoreInteractions(repository, mapper);
    }


    @Test
    void setGlavna_notFound() {
        Long slikaId = 99L;
        when(repository.findById(slikaId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.setGlavna(slikaId));

        verify(repository).findById(slikaId);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper, sobaRepository);
    }
}
