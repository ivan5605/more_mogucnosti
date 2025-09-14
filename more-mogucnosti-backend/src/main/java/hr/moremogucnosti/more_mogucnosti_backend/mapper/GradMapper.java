package hr.moremogucnosti.more_mogucnosti_backend.mapper;

import hr.moremogucnosti.more_mogucnosti_backend.dto.GradResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Grad;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class GradMapper {
    public GradResponseDto toResponseDto(Grad grad){
        if (grad==null){
            throw new ResourceNotFoundException("Nema grada za mapiranje u DTO objekt");
        }
        GradResponseDto gradDto = new GradResponseDto(
                grad.getIdGrad(),
                grad.getImeGrad()
        );
        return gradDto;
    }

    public Grad fromResponseDto(GradResponseDto gradDto){
        if (gradDto == null){
            throw new ResourceNotFoundException("Nema grada za mapiranje u entity objekt");
        }
        Grad grad = new Grad();
        grad.setImeGrad(gradDto.imeGrad());
        return grad;
    }
}
