package hr.moremogucnosti.more_mogucnosti_backend.mapper;

import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaDetailsDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Slika;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Soba;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@AllArgsConstructor

@Component
public class SobaMapper {

    private final SlikaMapper slikaMapper;
    private final HotelMapper hotelMapper;

    public SobaResponseDto toResponseDto(Soba soba){
        if (soba==null){
            throw new IllegalArgumentException("Nema sobe za mapiranje u DTO objekt");
        }
        SobaResponseDto sobaDto = new SobaResponseDto(
                soba.getId(),
                soba.getKapacitet(),
                soba.getCijenaNocenja(),
                soba.getBrojSobe(),
                soba.isBalkon(),
                soba.isPetFriendly(),

                soba.getSlike()
                        .stream()
                        .filter(Slika::isGlavnaSlika)
                        .map(slikaMapper::toResponseDto)
                        .findFirst()
                        .orElse(null),

                soba.getSlike()
                        .stream()
                        .filter(slika -> !slika.isGlavnaSlika())
                        .map(slikaMapper::toResponseDto)
                        .collect(Collectors.toList()),

                soba.isAktivno()
        );
        return sobaDto;
    }

    public SobaDetailsDto toDetailsDto(Soba soba) {
        if (soba == null){
            throw new IllegalArgumentException("Nema sobe za mapiranje u detailsDTO objekt");
        }

        Hotel hotel = soba.getHotel();

        SobaDetailsDto sobaZaRezervacijuDto = new SobaDetailsDto(
                soba.getId(),
                soba.getKapacitet(),
                soba.getCijenaNocenja(),
                soba.getBrojSobe(),
                soba.isBalkon(),
                soba.isPetFriendly(),

                soba.getSlike()
                        .stream()
                        .filter(Slika::isGlavnaSlika)
                        .map(slikaMapper::toResponseDto)
                        .findFirst()
                        .orElse(null),

                soba.getSlike()
                        .stream()
                        .filter(slika -> !slika.isGlavnaSlika())
                        .map(slikaMapper::toResponseDto)
                        .collect(Collectors.toList()),

                (hotelMapper.toResponseDto(hotel)),

                soba.isAktivno()

        );
        return sobaZaRezervacijuDto;
    }

    public SobaViewDto toViewDto(Soba soba){
        if (soba == null){
            throw new IllegalArgumentException("Nema soba za mapiranje u viewDto objekt!");
        }
        return new SobaViewDto(
                soba.getId(),
                soba.getCijenaNocenja(),
                soba.getBrojSobe(),
                soba.getKapacitet(),
                hotelMapper.toViewDto(soba.getHotel()),
                soba.isAktivno()
        );
    }
}
