package hr.moremogucnosti.more_mogucnosti_backend.service.impl;

import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.*;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Rezervacija;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Soba;
import hr.moremogucnosti.more_mogucnosti_backend.exception.BadRequestException;
import hr.moremogucnosti.more_mogucnosti_backend.exception.DuplicateException;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.HotelMapper;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.KorisnikMapper;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.RezervacijaMapper;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.SobaMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.RezervacijaRepository;
import hr.moremogucnosti.more_mogucnosti_backend.security.AppUserPrincipal;
import hr.moremogucnosti.more_mogucnosti_backend.service.KorisnikService;
import hr.moremogucnosti.more_mogucnosti_backend.service.RezervacijaService;
import hr.moremogucnosti.more_mogucnosti_backend.service.SobaService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)

public class RezervacijaServiceImpl implements RezervacijaService {

    private final RezervacijaRepository rezervacijaRepository;
    private final RezervacijaMapper rezervacijaMapper;
    private final KorisnikService korisnikService;
    private final KorisnikMapper korisnikMapper;
    private final SobaService sobaService;
    private final SobaMapper sobaMapper;
    private final HotelMapper hotelMapper;

    @Override
    @Transactional
    public RezervacijaDetailsDto createRezervacija(RezervacijaCreateDto rezervacijaCreateDto, AppUserPrincipal user) {
        Soba soba = sobaService.loadEntity(rezervacijaCreateDto.sobaId());
        Korisnik korsnik = korisnikService.loadEntity(user.getId());

        if (rezervacijaCreateDto.brojOsoba() > soba.getKapacitet()){
            throw new BadRequestException("Broj osoba ne smije biti veći od kapaciteta sobe (" + soba.getKapacitet() + ")");
        }

        Rezervacija rezervacija = rezervacijaMapper.fromCreateDto(rezervacijaCreateDto, soba, korsnik);

        if (rezervacijaRepository.existsOverlappingRezervacija(soba.getId(), rezervacija.getDatumPocetak(), rezervacija.getDatumKraj())){
            throw new DuplicateException("Soba je već rezervirana u tom rasponu datuma!");
        }

        Rezervacija savedRezervacija = rezervacijaRepository.save(rezervacija);
        return rezervacijaMapper.toDetailsDto(savedRezervacija);
    }

    @Override
    public List<RezervacijaDatumDto> findAllZauzetiDatumi(Long idSoba) {
        List<RezervacijaDatumDto> zauzetiDatumi = rezervacijaRepository.findAllBySobaId(idSoba)
                .stream()
                .map(rezervacijaMapper::toDatumDto)
                .collect(Collectors.toList());
        return zauzetiDatumi;
    }

    @Override
    public List<RezervacijaZaKorisnikDto> findAll(Long userId) {
        LocalDate danas = LocalDate.now();
        List<RezervacijaZaKorisnikDto> rezervacije = rezervacijaRepository.findWithSobaHotelByKorisnikId(userId, danas)
                .stream()
                .map(rezervacijaMapper::toZaKorisnikaDto)
                .collect(Collectors.toList());
        return rezervacije;
    }

    @Override
    public List<RezervacijaDetailsDto> findAllAktivneKorisnika(Long idKorisnik) {
        LocalDate danas = LocalDate.now();
        List<RezervacijaDetailsDto> aktivne = rezervacijaRepository.findAktivneRezervacijeKorisnika(danas, idKorisnik)
                .stream()
                .map(rezervacijaMapper::toDetailsDto)
                .toList();
        return aktivne;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<RezervacijaDetailsDto> findAllStareKorisnika(Long idKorisnik) {
        LocalDate danas = LocalDate.now();
        List<RezervacijaDetailsDto> stare = rezervacijaRepository.findStareRezervacijeKorisnika(danas, idKorisnik)
                .stream()
                .map(rezervacijaMapper::toDetailsDto)
                .toList();
        return stare;
    }

    @Override
    @Transactional
    public void deleteRezervacija(Long userId, Long rezervacijaId) {
//        Rezervacija rezervacija = rezervacijaRepository.findByIdAndKorisnikId(rezervacijaId, userId)
//                        .orElseThrow(() -> new ResourceNotFoundException("Rezervacija ne postoji!"));
//
//        rezervacijaRepository.deleteById(rezervacijaId);

        //ovo bolje jer saljem samo jedan DB upit

        int izbrisano = rezervacijaRepository.deleteByIdAndKorisnikId(rezervacijaId, userId);
        if (izbrisano == 0){
            throw new ResourceNotFoundException("Rezervacija ne postoji.");
        }
    }

    @Override
    @Transactional
    public RezervacijaDetailsDto updateRezervacija(Long userId, Long rezervacijaId, RezervacijaUpdateDto novaRezervacija) {
        Rezervacija rezervacija = rezervacijaRepository.findByIdAndKorisnikId(rezervacijaId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Rezervacija ne postoji."));

        rezervacija.setBrojOsoba(novaRezervacija.brojOsoba());
        rezervacija.setDatumPocetak(novaRezervacija.datumPocetak());
        rezervacija.setDatumKraj(novaRezervacija.datumKraj());
        Rezervacija spremljena = rezervacijaRepository.save(rezervacija);

        return rezervacijaMapper.toDetailsDto(spremljena);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void adminDelete(Long idRezervacija) {
        Rezervacija rezervacija = rezervacijaRepository.findById(idRezervacija)
                .orElseThrow(() -> new ResourceNotFoundException("Rezervacija ne postoji."));

        rezervacijaRepository.deleteById(idRezervacija);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public RezervacijaDetailsDto adminUpdate(Long idRezervacija, RezervacijaUpdateDto novaRezervacija) {
        Rezervacija rezervacija = rezervacijaRepository.findById(idRezervacija)
                .orElseThrow(() -> new ResourceNotFoundException("Rezervacija ne postoji."));

        rezervacija.setBrojOsoba(novaRezervacija.brojOsoba());
        rezervacija.setDatumPocetak(novaRezervacija.datumPocetak());
        rezervacija.setDatumKraj(novaRezervacija.datumKraj());
        Rezervacija spremljena = rezervacijaRepository.save(rezervacija);

        return rezervacijaMapper.toDetailsDto(spremljena);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<RezervacijaDetailsDto> findAllByHotelId(Long idHotel) {
        List<RezervacijaDetailsDto> rezervacije = rezervacijaRepository.findAllBySoba_Hotel_Id(idHotel)
                .stream()
                .map(rezervacijaMapper::toDetailsDto)
                .toList();
        return rezervacije;
    }
}
