package hr.moremogucnosti.more_mogucnosti_backend.mapper;

import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaHotelResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaSobaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.HotelViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaBasicDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Slika;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Soba;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component

@AllArgsConstructor
public class SlikaMapper {

    public SlikaHotelResponseDto toHotelResponseDto(Slika slika, Hotel hotel){
        if (slika == null){
            return null;
        }
        SlikaHotelResponseDto slikaDto = new SlikaHotelResponseDto(
                slika.getId(),
                slika.getPutanja(),
                slika.isGlavnaSlika(),
                new HotelViewDto(
                        hotel.getId(),
                        hotel.getNaziv()
                )
        );
        return slikaDto;
    }

    public SlikaResponseDto toResponseDto(Slika slika) {
        if (slika == null){
            return null;
        }
        SlikaResponseDto slikaDto = new SlikaResponseDto(
                slika.getId(),
                slika.getPutanja(),
                slika.isGlavnaSlika()
        );
        return slikaDto;
    }

    public SlikaSobaResponseDto toSobaResponseDto(Slika slika, Soba soba) {
        if (slika == null) {
            return null;
        }
        SlikaSobaResponseDto slikaDto = new SlikaSobaResponseDto(
                slika.getId(),
                slika.getPutanja(),
                slika.isGlavnaSlika(),
                new SobaBasicDto(
                        soba.getId(),
                        soba.getBrojSobe()
                )
        );
        return slikaDto;
    }

    //method overloading - metode s istim imenom ali razlika u parametrima
}
