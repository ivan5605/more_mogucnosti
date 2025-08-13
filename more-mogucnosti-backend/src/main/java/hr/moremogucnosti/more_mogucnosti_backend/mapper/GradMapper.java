package hr.moremogucnosti.more_mogucnosti_backend.mapper;

import hr.moremogucnosti.more_mogucnosti_backend.dto.GradDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Grad;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class GradMapper {
    public GradDto toGradDto(Grad grad){
        if (grad==null){
            throw new ResourceNotFoundException("Nema grada za mapiranje u DTO objekt");
        }
        GradDto gradDto = new GradDto();
        gradDto.setImeGrad(grad.getImeGrad());
        return gradDto;
    }

    public Grad fromGradDto(GradDto gradDto){
        if (gradDto == null){
            throw new ResourceNotFoundException("Nema grada za mapiranje u entity objekt");
        }
        Grad grad = new Grad();
        return grad;
    }
}
