package hr.moremogucnosti.more_mogucnosti_backend.mapper;

import hr.moremogucnosti.more_mogucnosti_backend.dto.recenzija.RecenzijaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.recenzija.RecenzijaDetailsDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.recenzija.RecenzijaZaHotelDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.recenzija.RecenzijaZaKorisnikDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Recenzija;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor

public class RecenzijaMapper {

    private final KorisnikMapper korisnikMapper;
    private final HotelMapper hotelMapper;

    public Recenzija fromCreateDto (RecenzijaCreateDto recenzijaCreateDto, Korisnik korisnik, Hotel hotel){
        if (recenzijaCreateDto == null){
            throw new IllegalArgumentException("Nema recenzije za mapiranje!"); //iznimka nije idealna za null izlaz, bolje IllegalArgumentException
        }
        Recenzija recenzija = new Recenzija();
        recenzija.setKorisnik(korisnik);
        recenzija.setHotel(hotel);
        recenzija.setOcjena(recenzijaCreateDto.ocjena());
        recenzija.setTekst(recenzijaCreateDto.tekst());
        return recenzija;
    }

    public RecenzijaDetailsDto toDetailsDto (Recenzija recenzija){
        if (recenzija == null){
            throw new IllegalArgumentException("Nema recenzije za mapiranje!");
        }
        return new RecenzijaDetailsDto(
                recenzija.getId(),
                korisnikMapper.toViewDto(recenzija.getKorisnik()),
                hotelMapper.toViewDto(recenzija.getHotel()),
                recenzija.getOcjena(),
                recenzija.getTekst(),
                recenzija.getDatum()
        );
    }

    public RecenzijaZaKorisnikDto toZaKorisnikaDto (Recenzija recenzija){
        if (recenzija == null) {
            throw new IllegalArgumentException("Nema recenzije za mapiranje!");
        }
        return new RecenzijaZaKorisnikDto(
                recenzija.getId(),
                hotelMapper.toViewDto(recenzija.getHotel()),
                recenzija.getOcjena(),
                recenzija.getTekst(),
                recenzija.getDatum()
        );
    }

    public RecenzijaZaHotelDto toZaHotelDto (Recenzija recenzija) {
        if (recenzija == null) {
            throw new IllegalArgumentException("Nema recenzije za mapiranje!");
        }
        return new RecenzijaZaHotelDto(
                recenzija.getId(),
                korisnikMapper.toBasicDto(recenzija.getKorisnik()),
                recenzija.getOcjena(),
                recenzija.getTekst(),
                recenzija.getDatum()
        );
    }
}
