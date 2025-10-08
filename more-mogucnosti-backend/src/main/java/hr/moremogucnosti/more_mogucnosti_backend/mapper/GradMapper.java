package hr.moremogucnosti.more_mogucnosti_backend.mapper;

import hr.moremogucnosti.more_mogucnosti_backend.dto.GradResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Grad;
import org.springframework.stereotype.Component;

@Component
public class GradMapper {
    public GradResponseDto toResponseDto(Grad grad){
        if (grad==null){
            throw new IllegalArgumentException("Nema grada za mapiranje u DTO objekt");
        }
        GradResponseDto gradDto = new GradResponseDto(
                grad.getIdGrad(),
                grad.getImeGrad()
        );
        return gradDto;
    }
}
