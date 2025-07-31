package hr.moremogucnosti.more_mogucnosti_backend.mapper;

import hr.moremogucnosti.more_mogucnosti_backend.dto.GradDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Grad;
import org.springframework.stereotype.Component;

@Component
public class GradMapper {
    public GradDto toGradDto(Grad grad){
        if (grad==null){
            return null;
        }
        GradDto gradDto = new GradDto();
        gradDto.setIme_grad(grad.getIme_grad());
        return gradDto;
    }
}
