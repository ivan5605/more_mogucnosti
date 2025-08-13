package hr.moremogucnosti.more_mogucnosti_backend.service.impl;

import hr.moremogucnosti.more_mogucnosti_backend.dto.RezervacijaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.RezervacijaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Rezervacija;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Soba;
import hr.moremogucnosti.more_mogucnosti_backend.exception.BadRequestException;
import hr.moremogucnosti.more_mogucnosti_backend.exception.DuplicateException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.KorisnikMapper;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.RezervacijaMapper;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.SobaMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.RezervacijaRepository;
import hr.moremogucnosti.more_mogucnosti_backend.service.KorisnikService;
import hr.moremogucnosti.more_mogucnosti_backend.service.RezervacijaService;
import hr.moremogucnosti.more_mogucnosti_backend.service.SobaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class RezervacijaServiceImpl implements RezervacijaService {

    private final RezervacijaRepository rezervacijaRepository;
    private final RezervacijaMapper rezervacijaMapper;
    private final KorisnikService korisnikService;
    private final KorisnikMapper korisnikMapper;
    private final SobaService sobaService;
    private final SobaMapper sobaMapper;

    @Override
    public RezervacijaResponseDto createRezervacija(RezervacijaCreateDto rezervacijaCreateDto) {
        Soba soba = sobaService.getEntity(rezervacijaCreateDto.getSobaId());
        Korisnik korsnik = korisnikService.getEntity(rezervacijaCreateDto.getKorisnikId());

        if (rezervacijaCreateDto.getBrojOsoba() > soba.getKapacitet()){
            throw new BadRequestException("Broj osoba ne smije biti veći od kapaciteta sobe (" + soba.getKapacitet() + ")");
        }

        //List<RezervacijaResponseDto> rezervacijeSobe = getRezervacijeSobe(soba.getId());
        //List<Rezervacija> rezervacije = rezervacijaRepository.findAllBySobaId(soba.getId());

        Rezervacija rezervacija = rezervacijaMapper.fromRezervacijaCreateDto(rezervacijaCreateDto, soba, korsnik);
//        for (Rezervacija rezervacijaSobe : rezervacije){
//            if (!(rezervacija.getDatumKraj().isBefore(rezervacijaSobe.getDatumPocetak()) || rezervacija.getDatumPocetak().isAfter(rezervacijaSobe.getDatumKraj()))){
//                throw new DuplicateException("Soba je već rezervirana u tom rasponu datuma!");
//            }
//        }

        if (rezervacijaRepository.existsOverlappingRezervacija(soba.getId(), rezervacija.getDatumPocetak(), rezervacija.getDatumKraj())){
            throw new DuplicateException("Soba je već rezervirana u tom rasponu datuma!");
        }

        Rezervacija savedRezervacija = rezervacijaRepository.save(rezervacija);
        return rezervacijaMapper.toRezervacijaResponseDto(savedRezervacija, sobaMapper.mapToDto(soba), korisnikMapper.toKorisnikDto(korsnik));
    }

    @Override
    public List<RezervacijaResponseDto> getRezervacijeSobe(Long idSoba) {
        List<RezervacijaResponseDto> rezervacijeSobe = rezervacijaRepository.findBySobaId(idSoba);
        return rezervacijeSobe;
    }
}
