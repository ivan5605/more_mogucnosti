package hr.moremogucnosti.more_mogucnosti_backend.mapper;

import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaDatumDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaDetailsDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaZaKorisnikDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Rezervacija;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Soba;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Component
@AllArgsConstructor
public class RezervacijaMapper {

    private final SobaMapper sobaMapper;
    private final KorisnikMapper korisnikMapper;
    private final HotelMapper hotelMapper;

    public Rezervacija fromCreateDto(RezervacijaCreateDto rezervacijaCreateDto, Soba soba, Korisnik korisnik){
        if (rezervacijaCreateDto==null){
            throw new ResourceNotFoundException("Nema rezervacije za mapiranje u entity objekt");
        }
        Rezervacija rezervacija = new Rezervacija();
        rezervacija.setSoba(soba);
        rezervacija.setKorisnik(korisnik);
        rezervacija.setBrojOsoba(rezervacijaCreateDto.brojOsoba());
        rezervacija.setDatumPocetak(rezervacijaCreateDto.datumPocetak());
        rezervacija.setDatumKraj(rezervacijaCreateDto.datumKraj());
        return rezervacija;
    }

    public RezervacijaDetailsDto toDetailsDto(Rezervacija rezervacija) {
        if (rezervacija==null){
            throw new ResourceNotFoundException("Nema rezervacije za mapiranje u responseDto objekt");
        }
        RezervacijaDetailsDto rezervacijaDetailsDto = new RezervacijaDetailsDto(
                rezervacija.getId(),
                korisnikMapper.toBasicDto(rezervacija.getKorisnik()),
                sobaMapper.toViewDto(rezervacija.getSoba()),
                rezervacija.getBrojOsoba(),
                rezervacija.getDatumPocetak(),
                rezervacija.getDatumKraj()
        );
        return rezervacijaDetailsDto;
    }

    public RezervacijaZaKorisnikDto toZaKorisnikaDto(Rezervacija rezervacija) {
        if (rezervacija == null){
            throw new ResourceNotFoundException("Nema rezervacije za mapiranje u zaKorisnikaDto objekt");
        }

        Long brojNocenja = Math.max(1, ChronoUnit.DAYS.between(rezervacija.getDatumPocetak(), rezervacija.getDatumKraj()));
        BigDecimal ukupnaCijena = rezervacija.getSoba().getCijenaNocenja().multiply(BigDecimal.valueOf(brojNocenja));

        return new RezervacijaZaKorisnikDto(
                rezervacija.getId(),
                sobaMapper.toViewDto(rezervacija.getSoba()),
                rezervacija.getBrojOsoba(),
                rezervacija.getDatumPocetak(),
                rezervacija.getDatumKraj(),
                brojNocenja,
                ukupnaCijena
        );
    }

    public RezervacijaDatumDto toDatumDto(Rezervacija rezervacija) {
        if (rezervacija == null){
            throw new ResourceNotFoundException("Nema rezervacije za mapiranje u datumDto objekt");
        }
        return new RezervacijaDatumDto(
                rezervacija.getId(),
                rezervacija.getDatumPocetak(),
                rezervacija.getDatumKraj()
        );
    }
}
