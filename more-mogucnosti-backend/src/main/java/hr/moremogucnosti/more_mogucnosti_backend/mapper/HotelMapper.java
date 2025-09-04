package hr.moremogucnosti.more_mogucnosti_backend.mapper;

import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.HotelPreviewDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.HotelViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.HotelDetailsDto;
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

    public HotelDetailsDto toDetailsDto(Hotel hotel){
        if (hotel==null){
            throw new ResourceNotFoundException("Nema hotela za mapiranje u DTO objekt");
        }
        HotelDetailsDto hotelDto = new HotelDetailsDto(
                hotel.getId(),
                hotel.getNaziv(),
                gradMapper.toResponseDto(hotel.getGrad()),
                hotel.getAdresa(),
                hotel.isParking(),
                hotel.isBazen(),
                hotel.isWifi(),

                hotel.getSlike()
                        .stream()
                        .filter(Slika::isGlavnaSlika)
                        .findFirst()
                        .map(slikaMapper::toResponseDto)
                        .orElse(null),

                hotel.getSlike()
                        .stream()
                        .filter(slika -> !slika.isGlavnaSlika())
                        .map(slikaMapper::toResponseDto)
                        .collect(Collectors.toList())
        );
        return hotelDto;
    }

    public HotelViewDto toViewDto(Hotel hotel){
        if (hotel==null){
            throw new ResourceNotFoundException("Nema hotela za mapiranje u prikazDTO objekt");
        }
        HotelViewDto hotelPrikazDto = new HotelViewDto(
                hotel.getId(),
                hotel.getNaziv()
        );
        return hotelPrikazDto;
    }

    public HotelPreviewDto toPreviewDto(Hotel hotel){
        if (hotel==null){
            throw new ResourceNotFoundException("Nema hotela za mapiranje u previewDTO objekt");
        }
        return new HotelPreviewDto(
                hotel.getId(),
                hotel.getNaziv(),
                gradMapper.toResponseDto(hotel.getGrad()),
                hotel.getAdresa(),
                hotel.isParking(),
                hotel.isBazen(),
                hotel.isWifi(),

                hotel.getSlike()
                        .stream()
                        .filter(Slika::isGlavnaSlika)
                        .findFirst()
                        .map(slikaMapper::toResponseDto)
                        .orElse(null)
        );
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
