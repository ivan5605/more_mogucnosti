package hr.moremogucnosti.more_mogucnosti_backend.service.impl;

import hr.moremogucnosti.more_mogucnosti_backend.dto.SlikaDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.HotelSlika;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.SlikaMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.SlikaRepository;
import hr.moremogucnosti.more_mogucnosti_backend.service.SlikaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class SlikaServiceImpl implements SlikaService {

    private final SlikaRepository hotelSlikaRepository;
    private final SlikaMapper slikaMapper;

    @Override
    public SlikaDto getGlavnaSlikaHotel(Long hotelId) {
        /*List<HotelSlika> hotelSlike = hotelSlikaRepository.findSlikaByHotelId(hotelId);
        HotelSlika hotelSlikaGlavna = null;
        for (HotelSlika hotelSlika : hotelSlike){
            if (hotelSlika.isGlavnaSlika()){
                hotelSlikaGlavna =  hotelSlika;
            }
        }
        return hotelSlikaMapper.mapToDto(hotelSlikaGlavna);*/

        HotelSlika hotelSlika = hotelSlikaRepository.findSlikaByHotelId(hotelId).stream()
                .filter(hotelSlika1 -> hotelSlika1.isGlavnaSlika()) //ili .filter(HotelSlika::isGlavnaSlika)
                .findFirst()
                .orElse(null);
        return slikaMapper.mapToDto(hotelSlika);
        //stream pretvara u tok podataka pa se nad njim moru raditi operacije: filter, map, sorted, collect, findFirst...
        //za lakše rukovanje, operacije, modernije od for loop
    }

    @Override
    public List<SlikaDto> getOstaleSlikeHotel(Long hotelId) {
        List<HotelSlika> slike = hotelSlikaRepository.findSlikaByHotelId(hotelId);

        List<SlikaDto> ostale = slike.stream()
                .filter(hotelSlika -> Boolean.FALSE.equals(hotelSlika.isGlavnaSlika()))
                .map(slikaMapper::mapToDto)
                .collect(Collectors.toList());

        return ostale;
    }
}
