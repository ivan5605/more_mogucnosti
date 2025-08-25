package hr.moremogucnosti.more_mogucnosti_backend.mapper;

import hr.moremogucnosti.more_mogucnosti_backend.dto.SlikaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Slika;
import org.springframework.stereotype.Component;

@Component
public class SlikaMapper {
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

    public SlikaResponseDto toResponseDto(Slika slika){
        if (slika == null){
            return null;
        }
        SlikaResponseDto slikaDto = new SlikaResponseDto(
                slika.getPutanja()
        );
        return slikaDto;
    }

    //method overloading - metode s istim imenom ali razlika u parametrima
}
