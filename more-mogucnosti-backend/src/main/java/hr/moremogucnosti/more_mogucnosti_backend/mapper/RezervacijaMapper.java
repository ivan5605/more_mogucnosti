package hr.moremogucnosti.more_mogucnosti_backend.mapper;

import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaDetailsDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Rezervacija;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Soba;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class RezervacijaMapper {
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

    public RezervacijaDetailsDto toResponseDto(Rezervacija rezervacija, SobaResponseDto sobaDto, KorisnikViewDto korisnikDto){
        if (rezervacija==null){
            throw new ResourceNotFoundException("Nema rezervacije za mapiranje u responseDto objekt");
        }
        RezervacijaDetailsDto rezervacijaDetailsDto = new RezervacijaDetailsDto(
                rezervacija.getIdRezervacija(),
                korisnikDto,
                sobaDto,
                rezervacija.getBrojOsoba(),
                rezervacija.getDatumPocetak(),
                rezervacija.getDatumKraj()
        );
        return rezervacijaDetailsDto;
    }
}
