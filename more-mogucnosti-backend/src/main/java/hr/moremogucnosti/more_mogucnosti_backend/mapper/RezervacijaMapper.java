package hr.moremogucnosti.more_mogucnosti_backend.mapper;

import hr.moremogucnosti.more_mogucnosti_backend.dto.*;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Rezervacija;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Soba;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class RezervacijaMapper {
    public Rezervacija fromRezervacijaCreateDto(RezervacijaCreateDto rezervacijaCreateDto, Soba soba, Korisnik korisnik){
        if (rezervacijaCreateDto==null){
            throw new ResourceNotFoundException("Nema rezervacije za mapiranje u entity objekt");
        }
        Rezervacija rezervacija = new Rezervacija();
        rezervacija.setSoba(soba);
        rezervacija.setKorisnik(korisnik);
        rezervacija.setBrojOsoba(rezervacijaCreateDto.getBrojOsoba());
        rezervacija.setDatumPocetak(rezervacijaCreateDto.getDatumPocetak());
        rezervacija.setDatumKraj(rezervacijaCreateDto.getDatumKraj());
        return rezervacija;
    }

    public RezervacijaResponseDto toRezervacijaResponseDto(Rezervacija rezervacija, SobaDto sobaDto, KorisnikDto korisnikDto){
        if (rezervacija==null){
            throw new ResourceNotFoundException("Nema rezervacije za mapiranje u responseDto objekt");
        }
        RezervacijaResponseDto rezervacijaResponseDto = new RezervacijaResponseDto();
        rezervacijaResponseDto.setIdRezervacija(rezervacija.getIdRezervacija());
        rezervacijaResponseDto.setSobaDto(sobaDto);
        rezervacijaResponseDto.setKorisnikDto(korisnikDto);
        rezervacijaResponseDto.setBrojOsoba(rezervacija.getBrojOsoba());
        rezervacijaResponseDto.setDatumPocetak(rezervacija.getDatumPocetak());
        rezervacijaResponseDto.setDatumKraj(rezervacija.getDatumKraj());
        return rezervacijaResponseDto;
    }
}
