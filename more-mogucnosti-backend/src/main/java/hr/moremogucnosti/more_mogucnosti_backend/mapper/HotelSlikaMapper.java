package hr.moremogucnosti.more_mogucnosti_backend.mapper;

import hr.moremogucnosti.more_mogucnosti_backend.dto.HotelSlikaDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.HotelSlika;
import org.springframework.stereotype.Component;

@Component
public class HotelSlikaMapper {
    public HotelSlikaDto mapToDto (HotelSlika hotelSlika){
        if (hotelSlika==null){
            return null;
        }
        HotelSlikaDto hotelSlikaDto = new HotelSlikaDto();
        hotelSlikaDto.setPutanja(hotelSlika.getPutanja());
        hotelSlikaDto.setGlavnaSlika(hotelSlika.isGlavnaSlika());
        return hotelSlikaDto;
    }
}
