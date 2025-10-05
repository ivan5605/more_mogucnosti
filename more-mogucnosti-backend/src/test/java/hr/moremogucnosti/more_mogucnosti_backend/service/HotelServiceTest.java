package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.GradResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.HotelDetailsDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.HotelPreviewDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Grad;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;
import hr.moremogucnosti.more_mogucnosti_backend.entity.HotelSlika;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.HotelMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.HotelRepository;
import hr.moremogucnosti.more_mogucnosti_backend.service.impl.HotelServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    HotelRepository repository;

    @Mock
    HotelMapper hotelMapper;

    @InjectMocks
    HotelServiceImpl service;

    Grad grad;
    Hotel hotel;

    @BeforeEach
    void ucitaj() {
        grad = new Grad();
        grad.setImeGrad("Zagreb");
        grad.setSlug("zagreb");

        hotel = new Hotel();
        hotel.setId(1L);
        hotel.setNaziv("Hotel");
        hotel.setWifi(true);
        hotel.setParking(true);
        hotel.setBazen(true);
        hotel.setAdresa("Adresa");
        hotel.setAktivno(true);
        hotel.setGrad(grad);

        HotelSlika s1 = new HotelSlika();
        s1.setId(1L); s1.setHotel(hotel); s1.setGlavnaSlika(true);  s1.setPutanja("p1");

        HotelSlika s2 = new HotelSlika();
        s2.setId(2L); s2.setHotel(hotel); s2.setGlavnaSlika(false); s2.setPutanja("p2");

        HotelSlika s3 = new HotelSlika();
        s3.setId(3L); s3.setHotel(hotel); s3.setGlavnaSlika(false); s3.setPutanja("p3");

        hotel.setSlike(List.of(s1, s2, s3));
    }

    @Test
    void findDetailById() {
        // arrange
        when(repository.findByHotelIdWithSlike(hotel.getId())).thenReturn(Optional.of(hotel));

        var gradDto = new GradResponseDto(grad.getIdGrad(), grad.getImeGrad());
        var glavna = new SlikaResponseDto(1L, "p1", true); // prilagodi ctor ako je drukčiji
        var sporedne = List.of(
                new SlikaResponseDto(2L, "p2", false),
                new SlikaResponseDto(3L, "p3", false)
        );

        HotelDetailsDto rezultat = new HotelDetailsDto(
                hotel.getId(),
                hotel.getNaziv(),
                gradDto,
                hotel.getAdresa(),
                hotel.isParking(),
                hotel.isWifi(),
                hotel.isBazen(),
                glavna,
                sporedne,
                hotel.isAktivno()
        );

        when(hotelMapper.toDetailsDto(hotel)).thenReturn(rezultat);

        // act
        HotelDetailsDto praviRez = service.findDetailById(hotel.getId());

        // assert
        assertThat(praviRez).isEqualTo(rezultat);
        verify(repository).findByHotelIdWithSlike(hotel.getId());
        verify(hotelMapper).toDetailsDto(hotel);
        verifyNoMoreInteractions(repository, hotelMapper);
    }

    @Test
    void findDetailById_notFound() {
        Long id = 999L;
        when(repository.findByHotelIdWithSlike(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findDetailById(id));

        verify(repository).findByHotelIdWithSlike(id);
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void findAll () {
        Hotel h2 = setHotel(2L, "Hotel2");

        GradResponseDto gradResponseDto = new GradResponseDto(grad.getIdGrad(), grad.getImeGrad());
        var glavna = new SlikaResponseDto(1L, "p1", true);

        HotelPreviewDto hpd1 = new HotelPreviewDto(
                hotel.getId(),
                hotel.getNaziv(),
                gradResponseDto,
                hotel.getAdresa(),
                hotel.isParking(),
                hotel.isWifi(),
                hotel.isBazen(),
                glavna,
                hotel.isAktivno()
        );

        HotelPreviewDto hpd2 = new HotelPreviewDto(
                h2.getId(),
                h2.getNaziv(),
                gradResponseDto,
                h2.getAdresa(),
                h2.isParking(),
                h2.isWifi(),
                h2.isBazen(),
                glavna,
                h2.isAktivno()
        );

        when(repository.findAllWithSlike()).thenReturn(List.of(hotel, h2));
        when(hotelMapper.toPreviewDto(hotel)).thenReturn(hpd1);
        when(hotelMapper.toPreviewDto(h2)).thenReturn(hpd2);

        List<HotelPreviewDto> rezultat = service.findAll();

        assertEquals(2, rezultat.size());
        var nazivi = rezultat.stream().map(HotelPreviewDto::naziv).toList();
        assertTrue(nazivi.containsAll(List.of("Hotel", "Hotel2")));
        verify(repository).findAllWithSlike();
        verify(hotelMapper).toPreviewDto(hotel);
        verify(hotelMapper).toPreviewDto(h2);
        verifyNoMoreInteractions(repository, hotelMapper);
    }

    @Test
    void findAll_notFound() {
        when(repository.findAllWithSlike()).thenReturn(List.of());

        List<HotelPreviewDto> rezultat = service.findAll();

        assertEquals(0, rezultat.size());
        verify(repository).findAllWithSlike();
        verifyNoInteractions(hotelMapper);
    }

    @Test
    void findRandom() {
        // arrange
        Hotel h2 = setHotel(2L, "Hotel2");
        Hotel h3 = setHotel(3L, "Hotel3");

        var gradDto = new GradResponseDto(grad.getIdGrad(), grad.getImeGrad());
        var glavna = new SlikaResponseDto(1L, "p1", true);

        HotelPreviewDto d1 = new HotelPreviewDto(
                hotel.getId(), hotel.getNaziv(), gradDto, hotel.getAdresa(),
                hotel.isParking(), hotel.isWifi(), hotel.isBazen(), glavna, hotel.isAktivno()
        );
        HotelPreviewDto d2 = new HotelPreviewDto(
                h2.getId(), h2.getNaziv(), gradDto, h2.getAdresa(),
                h2.isParking(), h2.isWifi(), h2.isBazen(), glavna, h2.isAktivno()
        );
        HotelPreviewDto d3 = new HotelPreviewDto(
                h3.getId(), h3.getNaziv(), gradDto, h3.getAdresa(),
                h3.isParking(), h3.isWifi(), h3.isBazen(), glavna, h3.isAktivno()
        );

        when(repository.findRandomHotelIds(any(PageRequest.class)))
                .thenReturn(List.of(hotel.getId(), h2.getId(), h3.getId()));
        when(repository.find3RandomHotelsWithSlike(List.of(hotel.getId(), h2.getId(), h3.getId())))
                .thenReturn(List.of(hotel, h2, h3));

        when(hotelMapper.toPreviewDto(hotel)).thenReturn(d1);
        when(hotelMapper.toPreviewDto(h2)).thenReturn(d2);
        when(hotelMapper.toPreviewDto(h3)).thenReturn(d3);

        // act
        var rezultat = service.findRandom();

        // assert
        assertEquals(3, rezultat.size());
        var nazivi = rezultat.stream().map(HotelPreviewDto::naziv).toList();
        assertTrue(nazivi.containsAll(List.of("Hotel", "Hotel2", "Hotel3")));

        verify(repository).findRandomHotelIds(PageRequest.of(0, 3));
        verify(repository).find3RandomHotelsWithSlike(List.of(hotel.getId(), h2.getId(), h3.getId()));
        verify(hotelMapper).toPreviewDto(hotel);
        verify(hotelMapper).toPreviewDto(h2);
        verify(hotelMapper).toPreviewDto(h3);
        verifyNoMoreInteractions(repository, hotelMapper);
    }

    @Test
    void findRandom_notFound() {
        when(repository.findRandomHotelIds(PageRequest.of(0, 3))).thenReturn(List.of());
        when(repository.find3RandomHotelsWithSlike(List.of())).thenReturn(List.of());

        var rezultat = service.findRandom();

        assertTrue(rezultat.isEmpty());
        verify(repository).findRandomHotelIds(PageRequest.of(0, 3));
        verify(repository).find3RandomHotelsWithSlike(List.of());
        verifyNoInteractions(hotelMapper);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void loadEntity() {
        when(repository.findById(hotel.getId())).thenReturn(Optional.of(hotel));

        Hotel rezultat = service.loadEntity(hotel.getId());

        assertNotNull(rezultat);
        assertEquals(hotel.getId(), rezultat.getId());
        assertEquals("Hotel", rezultat.getNaziv());

        verify(repository).findById(hotel.getId());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void loadEntity_notFound_throws() {
        Long id = 99L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.loadEntity(id));

        verify(repository).findById(id);
        verifyNoMoreInteractions(repository);
    }

    private Hotel setHotel(Long id, String naziv) {
        Hotel h = new Hotel();
        h.setGrad(grad);
        h.setId(id);
        h.setParking(true);
        h.setWifi(true);
        h.setBazen(true);
        h.setAdresa("Adresa" + id);
        h.setNaziv(naziv);
        h.setAktivno(true);
        return h;
    }
}
