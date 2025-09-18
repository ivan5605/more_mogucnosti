package hr.moremogucnosti.more_mogucnosti_backend.mapper;

import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaHotelResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Slika;
import org.springframework.stereotype.Component;

@Component
public class SlikaMapper {

    private final HotelMapper hotelMapper;

    /*public SlikaDto mapToDto (HotelSlika hotelSlika){
        if (hotelSlika ==null){
            return null;
        }
        SlikaDto slikaDto = new SlikaDto();
        slikaDto.setPutanja(hotelSlika.getPutanja());
        return slikaDto;
    }

    public SlikaDto mapToDto (SobaSlika sobaSlika){
        if (sobaSlika ==null){
            return null;
        }
        SlikaDto slikaDto = new SlikaDto();
        slikaDto.setPutanja(sobaSlika.getPutanja());
        return slikaDto;
    }*/

    public SlikaHotelResponseDto toHotelResponseDto(Slika slika, Hotel hotel){
        if (slika == null){
            return null;
        }
        SlikaHotelResponseDto slikaDto = new SlikaHotelResponseDto(
                slika.getId(),
                slika.getPutanja(),
                slika.isGlavnaSlika(),
                hotelMapper.toViewDto(hotel)
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

    //method overloading - metode s istim imenom ali razlika u parametrima
}
