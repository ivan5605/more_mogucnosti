package hr.moremogucnosti.more_mogucnosti_backend.mapper;

import hr.moremogucnosti.more_mogucnosti_backend.dto.HotelDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;
import org.springframework.stereotype.Component;

@Component
public class HotelMapper {
    public HotelDto toHotelDto(Hotel hotel){
        if (hotel==null){
            return null;
        }
        HotelDto hotelDto = new HotelDto();
        hotelDto.setId(hotel.getId());
        hotelDto.setNaziv(hotel.getNaziv());
        //hotelDto.setGrad(hotelDto.getGrad()); to ide u serviceImpl
        hotelDto.setAdresa(hotel.getAdresa());
        hotelDto.setParking(hotel.isParking());
        hotelDto.setBazen(hotel.isBazen());
        hotelDto.setWifi(hotel.isWifi());
        return hotelDto;
    }
}
