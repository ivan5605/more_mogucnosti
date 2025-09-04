package hr.moremogucnosti.more_mogucnosti_backend.service.impl;

import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaDatumDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaDetailsDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaZaKorisnikDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Rezervacija;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Soba;
import hr.moremogucnosti.more_mogucnosti_backend.exception.BadRequestException;
import hr.moremogucnosti.more_mogucnosti_backend.exception.DuplicateException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.HotelMapper;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.KorisnikMapper;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.RezervacijaMapper;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.SobaMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.RezervacijaRepository;
import hr.moremogucnosti.more_mogucnosti_backend.service.KorisnikService;
import hr.moremogucnosti.more_mogucnosti_backend.service.RezervacijaService;
import hr.moremogucnosti.more_mogucnosti_backend.service.SobaService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public RezervacijaDetailsDto createRezervacija(RezervacijaCreateDto rezervacijaCreateDto, User user) {
        Soba soba = sobaService.loadEntity(rezervacijaCreateDto.sobaId());
        Korisnik korsnik = korisnikService.loadEntityByEmail(user.getUsername());

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
    public List<RezervacijaZaKorisnikDto> findAll(User user) {
        List<RezervacijaZaKorisnikDto> rezervacije = rezervacijaRepository.findWithSobaHotelByKorisnikEmail(user.getUsername())
                .stream()
                .map(rezervacijaMapper::toZaKorisnikaDto)
                .collect(Collectors.toList());
        return rezervacije;
    }
}
