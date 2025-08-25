package hr.moremogucnosti.more_mogucnosti_backend.service.impl;

import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaDetailsDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Soba;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.SobaMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.SobaRepository;
import hr.moremogucnosti.more_mogucnosti_backend.service.HotelService;
import hr.moremogucnosti.more_mogucnosti_backend.service.SobaService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SobaServiceImpl implements SobaService {

    private final SobaRepository sobaRepository;
    private final SobaMapper sobaMapper;
    private final HotelService hotelService;

    @Override
    public SobaResponseDto findById(Long idSoba) {
        Soba soba = sobaRepository.findByIdWithSlike(idSoba)
                .orElseThrow(() -> new ResourceNotFoundException("Soba sa ID-jem " + idSoba + " ne postoji!"));
        return sobaMapper.toResponseDto(soba);
    }

    @Override
    public List<SobaResponseDto> findAllByIdHotel(Long hotelId) {
        //hotelService.getDto(hotelId); provjera jel ima hotela ima bolji način?
        List<Soba> sobeHotela = sobaRepository.findByHotelIdWithSlike(hotelId);
        if (sobeHotela.isEmpty()){
            throw new ResourceNotFoundException("Hotel sa ID-jem " + hotelId + " nema sobe za rezervaciju!");
        }

        List<SobaResponseDto> sobeHotelaDto = sobeHotela.stream().
                map(sobaMapper::toResponseDto).collect(Collectors.toList());
        return sobeHotelaDto;
    }

    @Override
    public List<SobaResponseDto> findRandomByIdHotel(Long hotelId) {
        List<SobaResponseDto> sobe = sobaRepository.find2RandomSobeHotelaWithSlike(hotelId, PageRequest.of(0, 2))
                .stream()
                .map(sobaMapper::toResponseDto)
                .collect(Collectors.toList());
        return sobe;
    }

    @Override
    public SobaDetailsDto findDetailsById(Long idSoba) {
        Soba soba = sobaRepository.getSobaByIdWithHotelAndSlike(idSoba)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel sa ID-jem " +  idSoba + " ne postoji!"));
        return sobaMapper.toDetailsDto(soba);
    }

    @Override
    public Soba loadEntity(Long idSoba) {
        Soba soba = sobaRepository.findById(idSoba)
                .orElseThrow(() -> new ResourceNotFoundException("Soba sa ID-jem " + idSoba + " ne postoji!"));
        return soba;
    }
}
