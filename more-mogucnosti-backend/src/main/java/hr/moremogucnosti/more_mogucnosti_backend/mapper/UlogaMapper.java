package hr.moremogucnosti.more_mogucnosti_backend.mapper;

import hr.moremogucnosti.more_mogucnosti_backend.dto.UlogaDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Uloga;
import org.springframework.stereotype.Component;

@Component
public class UlogaMapper {
    public UlogaDto mapToUlogaDto (Uloga uloga){
        if (uloga==null){
            return null;
        }
        UlogaDto ulogaDto = new UlogaDto();
        ulogaDto.setIdUloga(uloga.getIdUloga());
        ulogaDto.setNazivUloga(uloga.getNazivUloga());
        return ulogaDto;
    }
}
