package hr.moremogucnosti.more_mogucnosti_backend.mapper;

import hr.moremogucnosti.more_mogucnosti_backend.dto.KorisnikDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.KorisnikRegistracijaDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import org.springframework.stereotype.Component;

@Component
public class KorisnikMapper {

    public Korisnik fromKorisnikRegistracijaDto(KorisnikRegistracijaDto korisnikRegistracijaDto){
        if (korisnikRegistracijaDto==null){
            return null;
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
            return null;
        }
        KorisnikDto korisnikDto = new KorisnikDto();
        korisnikDto.setIme(korisnik.getIme());
        korisnikDto.setPrezime(korisnik.getPrezime());
        korisnikDto.setEmail(korisnik.getEmail());
        korisnikDto.setNazivUloga(korisnik.getUloga().getNazivUloga());
        return korisnikDto;
    }
}
