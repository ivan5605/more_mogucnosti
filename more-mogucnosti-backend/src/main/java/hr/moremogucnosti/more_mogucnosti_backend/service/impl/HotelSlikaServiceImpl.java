package hr.moremogucnosti.more_mogucnosti_backend.service.impl;

import hr.moremogucnosti.more_mogucnosti_backend.dto.HotelSlikaDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.HotelSlika;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.HotelSlikaMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.HotelSlikaRepository;
import hr.moremogucnosti.more_mogucnosti_backend.service.HotelSlikaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class HotelSlikaServiceImpl implements HotelSlikaService {

    private final HotelSlikaRepository hotelSlikaRepository;
    private final HotelSlikaMapper hotelSlikaMapper;

    @Override
    public HotelSlikaDto getGlavnaSlika(Long hotelId) {
        /*List<HotelSlika> hotelSlike = hotelSlikaRepository.findSlikaByHotelId(hotelId);
        HotelSlika hotelSlikaGlavna = null;
        for (HotelSlika hotelSlika : hotelSlike){
            if (hotelSlika.isGlavnaSlika()){
                hotelSlikaGlavna =  hotelSlika;
            }
        }
        return hotelSlikaMapper.mapToDto(hotelSlikaGlavna);*/

        HotelSlika hotelSlika = hotelSlikaRepository.findSlikaByHotelId(hotelId).stream()
                .filter(slika -> slika.isGlavnaSlika()) //ili .filter(HotelSlika::isGlavnaSlika)
                .findFirst()
                .orElse(null);
        return hotelSlikaMapper.mapToDto(hotelSlika);
        //stream pretvara u tok podataka pa se nad njim moru raditi operacije: filter, map, sorted, collect, findFirst...
        //za lakše rukovanje, operacije, modernije od for loop
    }
}
