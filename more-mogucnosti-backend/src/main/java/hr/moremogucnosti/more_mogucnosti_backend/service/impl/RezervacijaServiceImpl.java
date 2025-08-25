package hr.moremogucnosti.more_mogucnosti_backend.service.impl;

import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaDetailsDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Rezervacija;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Soba;
import hr.moremogucnosti.more_mogucnosti_backend.exception.BadRequestException;
import hr.moremogucnosti.more_mogucnosti_backend.exception.DuplicateException;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.KorisnikMapper;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.RezervacijaMapper;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.SobaMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.KorisnikRepository;
import hr.moremogucnosti.more_mogucnosti_backend.repository.RezervacijaRepository;
import hr.moremogucnosti.more_mogucnosti_backend.service.KorisnikService;
import hr.moremogucnosti.more_mogucnosti_backend.service.RezervacijaService;
import hr.moremogucnosti.more_mogucnosti_backend.service.SobaService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class RezervacijaServiceImpl implements RezervacijaService {

    private final RezervacijaRepository rezervacijaRepository;
    private final RezervacijaMapper rezervacijaMapper;
    private final KorisnikService korisnikService;
    private final KorisnikMapper korisnikMapper;
    private final SobaService sobaService;
    private final SobaMapper sobaMapper;
    private final KorisnikRepository korisnikRepository;

    @Override
    public RezervacijaDetailsDto createRezervacija(RezervacijaCreateDto rezervacijaCreateDto, User user) {
        Soba soba = sobaService.loadEntity(rezervacijaCreateDto.sobaId());
        Korisnik korsnik = korisnikRepository.findByEmail(user.getUsername()).
                orElseThrow(() -> new ResourceNotFoundException("Korisnik sa email-om " + user.getUsername() + " ne postoji!"));

        if (rezervacijaCreateDto.brojOsoba() > soba.getKapacitet()){
            throw new BadRequestException("Broj osoba ne smije biti veći od kapaciteta sobe (" + soba.getKapacitet() + ")");
        }

        //List<RezervacijaDetailsDto> rezervacijeSobe = getRezervacijeSobe(soba.getId());
        //List<Rezervacija> rezervacije = rezervacijaRepository.findAllBySobaId(soba.getId());

        Rezervacija rezervacija = rezervacijaMapper.fromCreateDto(rezervacijaCreateDto, soba, korsnik);
//        for (Rezervacija rezervacijaSobe : rezervacije){
//            if (!(rezervacija.getDatumKraj().isBefore(rezervacijaSobe.getDatumPocetak()) || rezervacija.getDatumPocetak().isAfter(rezervacijaSobe.getDatumKraj()))){
//                throw new DuplicateException("Soba je već rezervirana u tom rasponu datuma!");
//            }
//        }

        if (rezervacijaRepository.existsOverlappingRezervacija(soba.getId(), rezervacija.getDatumPocetak(), rezervacija.getDatumKraj())){
            throw new DuplicateException("Soba je već rezervirana u tom rasponu datuma!");
        }

        Rezervacija savedRezervacija = rezervacijaRepository.save(rezervacija);
        return rezervacijaMapper.toResponseDto(savedRezervacija, sobaMapper.toResponseDto(soba), korisnikMapper.toViewDto(korsnik));
    }

    @Override
    public List<RezervacijaDetailsDto> findAllByIdSoba(Long idSoba) {
        List<RezervacijaDetailsDto> rezervacijeSobe = rezervacijaRepository.findBySobaId(idSoba)
                .stream()
                .map(rezervacija -> rezervacijaMapper.toResponseDto(rezervacija,
                        sobaMapper.toResponseDto(rezervacija.getSoba()),
                        korisnikMapper.toViewDto(rezervacija.getKorisnik())))
                .collect(Collectors.toList());
        return rezervacijeSobe;
    }
}
