package hr.moremogucnosti.more_mogucnosti_backend.mapper;

import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikBasicDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import org.springframework.stereotype.Component;

@Component

public class KorisnikMapper {

    public KorisnikViewDto toViewDto(Korisnik korisnik){
        if (korisnik==null){
            throw new IllegalArgumentException("Nema korisnika za mapiranje u DTO objekt");
        }
        KorisnikViewDto korisnikDto = new KorisnikViewDto(
                korisnik.getId(), korisnik.getIme(), korisnik.getPrezime(), korisnik.getEmail()
        );
        return korisnikDto;
    }

    public KorisnikBasicDto toBasicDto(Korisnik korisnik){
        if (korisnik==null){
            throw new IllegalArgumentException("Nema korisnika za mapiranje u DTO objekt");
        }
        return new KorisnikBasicDto(
                korisnik.getId(), korisnik.getIme(), korisnik.getPrezime()
        );
    }
}
