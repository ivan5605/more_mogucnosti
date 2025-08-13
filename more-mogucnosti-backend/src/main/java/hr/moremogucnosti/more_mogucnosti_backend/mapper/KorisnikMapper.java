package hr.moremogucnosti.more_mogucnosti_backend.mapper;

import hr.moremogucnosti.more_mogucnosti_backend.dto.KorisnikDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.KorisnikRegistracijaDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class KorisnikMapper {

    public Korisnik fromKorisnikRegistracijaDto(KorisnikRegistracijaDto korisnikRegistracijaDto){
        if (korisnikRegistracijaDto==null){
            throw new ResourceNotFoundException("Nema korisnika za mapiranje u entity objekt");
        }
        Korisnik korisnik = new Korisnik();
        korisnik.setIme(korisnikRegistracijaDto.getIme());
        korisnik.setPrezime(korisnikRegistracijaDto.getPrezime());
        korisnik.setEmail(korisnikRegistracijaDto.getEmail());
        korisnik.setLozinka(korisnikRegistracijaDto.getLozinka());
        //Mapper treba biti jednostavan, "glup" – ne zna ništa o sigurnosti, enkripciji itd.
        return korisnik;
    }

    public KorisnikDto toKorisnikDto(Korisnik korisnik){
        if (korisnik==null){
            throw new ResourceNotFoundException("Nema korisnika za mapiranje u DTO objekt");
        }
        KorisnikDto korisnikDto = new KorisnikDto();
        korisnikDto.setIme(korisnik.getIme());
        korisnikDto.setPrezime(korisnik.getPrezime());
        korisnikDto.setEmail(korisnik.getEmail());
        return korisnikDto;
    }
}
