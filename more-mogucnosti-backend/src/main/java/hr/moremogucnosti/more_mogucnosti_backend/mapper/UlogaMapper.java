package hr.moremogucnosti.more_mogucnosti_backend.mapper;

import hr.moremogucnosti.more_mogucnosti_backend.dto.UlogaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Uloga;
import org.springframework.stereotype.Component;

@Component
public class UlogaMapper {
    public UlogaResponseDto toResponseDto(Uloga uloga){
        if (uloga==null){
            return null;
        }
        UlogaResponseDto ulogaDto = new UlogaResponseDto(
                uloga.getIdUloga(),
                uloga.getNazivUloga()
        );
        return ulogaDto;
    }

    public Uloga fromResponseDto(UlogaResponseDto ulogaDto){
        if (ulogaDto==null){
            return null;
        }
        Uloga uloga = new Uloga();
        uloga.setIdUloga(ulogaDto.idUloga());
        uloga.setNazivUloga(ulogaDto.nazivUloga());
        return uloga;
    }
}
