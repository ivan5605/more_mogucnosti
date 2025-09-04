package hr.moremogucnosti.more_mogucnosti_backend.mapper;

import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikBasicDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikRegistracijaDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor

public class KorisnikMapper {

    public Korisnik fromRegistracijaDto(KorisnikRegistracijaDto korisnikRegistracijaDto){
        if (korisnikRegistracijaDto==null){
            throw new ResourceNotFoundException("Nema korisnika za mapiranje u entity objekt");
        }
        Korisnik korisnik = new Korisnik();
        korisnik.setIme(korisnikRegistracijaDto.ime());
        korisnik.setPrezime(korisnikRegistracijaDto.prezime());
        korisnik.setEmail(korisnikRegistracijaDto.email());
        korisnik.setLozinka(korisnikRegistracijaDto.lozinka());
        //Mapper treba biti jednostavan, "glup" – ne zna ništa o sigurnosti, enkripciji itd.
        return korisnik;
    }

    public KorisnikViewDto toViewDto(Korisnik korisnik){
        if (korisnik==null){
            throw new ResourceNotFoundException("Nema korisnika za mapiranje u DTO objekt");
        }
        KorisnikViewDto korisnikDto = new KorisnikViewDto(
                korisnik.getId(), korisnik.getIme(), korisnik.getPrezime(), korisnik.getEmail()
        );
        return korisnikDto;
    }

    public KorisnikBasicDto toBasicDto(Korisnik korisnik){
        if (korisnik==null){
            throw new ResourceNotFoundException("Nema korisnika za mapiranje u DTO objekt");
        }
        return new KorisnikBasicDto(
                korisnik.getIme(), korisnik.getPrezime()
        );
    }

//    public KorisnikDetailsDto toDetailsDto(Korisnik korisnik){
//        if (korisnik==null){
//            throw new ResourceNotFoundException("Nema korisnika za mapiranje u DTO objekt");
//        }
//        return new KorisnikDetailsDto(
//                korisnik.getId(),
//                korisnik.getIme(),
//                korisnik.getPrezime(),
//                korisnik.getEmail(),
//                recenzijaMapper.toZaKorisnikaDto(korisnik.getRezervacije())
//        )
//    }
}
