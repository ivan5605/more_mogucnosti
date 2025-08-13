package hr.moremogucnosti.more_mogucnosti_backend.mapper;

import hr.moremogucnosti.more_mogucnosti_backend.dto.HotelPrikazDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.HotelResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Slika;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@AllArgsConstructor

@Component
public class HotelMapper {

    private final SlikaMapper slikaMapper;
    private final GradMapper gradMapper;

    public HotelResponseDto toHotelResponseDto(Hotel hotel){
        if (hotel==null){
            throw new ResourceNotFoundException("Nema hotela za mapiranje u DTO objekt");
        }
        HotelResponseDto hotelDto = new HotelResponseDto();
        hotelDto.setId(hotel.getId());
        hotelDto.setNaziv(hotel.getNaziv());
        hotelDto.setGrad(gradMapper.toGradDto(hotel.getGrad()));
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

    public HotelPrikazDto toPrikazDto(Hotel hotel){
        if (hotel==null){
            throw new ResourceNotFoundException("Nema hotela za mapiranje u prikazDTO objekt");
        }
        HotelPrikazDto hotelPrikazDto = new HotelPrikazDto();
        hotelPrikazDto.setNaziv(hotel.getNaziv());
        hotelPrikazDto.setGrad(gradMapper.toGradDto(hotel.getGrad()));
        hotelPrikazDto.setAdresa(hotel.getAdresa());
        return hotelPrikazDto;
    }

//    public Hotel fromHotelDto(HotelDto hotelDto) {
//        if (hotelDto==null){
//            throw new ResourceNotFoundException("Nema hotela za mapiranje u entity objekt");
//        }
//        Hotel hotel = new Hotel();
//        hotel.setId(hotelDto.getId());
//        hotel.setNaziv(hotelDto.getNaziv());
//        hotel.setGrad(gradMapper.fromGradDto(hotelDto.getGrad()));
//        hotel.setParking(hotelDto.isParking());
//        hotel.setBazen(hotelDto.isBazen());
//        hotel.setWifi(hotelDto.isWifi());
//        hotel.setAdresa(hotelDto.getAdresa());
//        hotel.set
//        return hotel;
//    }
}
