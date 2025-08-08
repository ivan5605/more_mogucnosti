package hr.moremogucnosti.more_mogucnosti_backend.mapper;

import hr.moremogucnosti.more_mogucnosti_backend.dto.HotelDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Slika;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@AllArgsConstructor

@Component
public class HotelMapper {

    private final SlikaMapper slikaMapper;

    public HotelDto toHotelDto(Hotel hotel){
        if (hotel==null){
            return null;
        }
        HotelDto hotelDto = new HotelDto();
        hotelDto.setId(hotel.getId());
        hotelDto.setNaziv(hotel.getNaziv());
        hotelDto.setGrad(hotel.getGrad().getImeGrad());
        hotelDto.setAdresa(hotel.getAdresa());
        hotelDto.setParking(hotel.isParking());
        hotelDto.setBazen(hotel.isBazen());
        hotelDto.setWifi(hotel.isWifi());

        hotelDto.setGlavnaSlika(hotel.getSlike()
                .stream()
                .filter(Slika::isGlavnaSlika)
                .findFirst()
                .map(slikaMapper::mapToDto)
                .orElse(null));

        hotelDto.setSporedneSlike(hotel.getSlike()
                .stream()
                .filter(slika -> !slika.isGlavnaSlika())
                .map(slikaMapper::mapToDto)
                .collect(Collectors.toList()));

        return hotelDto;
    }
}
