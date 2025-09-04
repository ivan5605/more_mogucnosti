package hr.moremogucnosti.more_mogucnosti_backend.service.impl;

import hr.moremogucnosti.more_mogucnosti_backend.dto.UlogaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Uloga;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.UlogaMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.UlogaRepository;
import hr.moremogucnosti.more_mogucnosti_backend.service.UlogaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)

public class UlogaServiceImpl implements UlogaService {

    private final UlogaRepository ulogaRepository;
    private final UlogaMapper ulogaMapper;

    @Override
    public UlogaResponseDto findById(Long id) {
        Uloga uloga = ulogaRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Uloga sa ID-jem " + id + " ne postoji!"));
        return ulogaMapper.toResponseDto(uloga);
    }

    @Override
    public Uloga loadEntity(String naziv) {
        Uloga uloga = ulogaRepository.findByNazivUloga(naziv)
                .orElseThrow(() -> new ResourceNotFoundException("Uloga pod nazivom " + naziv + " ne postoji!"));
        return uloga;
    }
}
