package hr.moremogucnosti.more_mogucnosti_backend.service.impl;

import hr.moremogucnosti.more_mogucnosti_backend.dto.GradDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Grad;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.GradMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.GradRepository;
import hr.moremogucnosti.more_mogucnosti_backend.service.GradService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class GradServiceImpl implements GradService {

    private final GradRepository gradRepository;
    private final GradMapper gradMapper;

    @Override
    public GradDto getGrad(Long id) {
        Grad grad = gradRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grad sa ID-jem " + id + " ne postoji!"));
        return gradMapper.toGradDto(grad);
    }
}
