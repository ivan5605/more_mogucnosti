package hr.moremogucnosti.more_mogucnosti_backend.service.impl;

import hr.moremogucnosti.more_mogucnosti_backend.dto.SobaDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Soba;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.SobaMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.SobaRepository;
import hr.moremogucnosti.more_mogucnosti_backend.service.HotelService;
import hr.moremogucnosti.more_mogucnosti_backend.service.SobaService;
import lombok.AllArgsConstructor;
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
    public SobaDto getSoba(Long idSoba) {
        Soba soba = sobaRepository.findById(idSoba)
                .orElseThrow(() -> new ResourceNotFoundException("Soba sa ID-jem " + idSoba + " ne postoji!"));
        return sobaMapper.mapToDto(soba);
    }

    @Override
    public List<SobaDto> getSobeHotela(Long hotelId) {
        hotelService.getHotel(hotelId);
        List<Soba> sobeHotela = sobaRepository.findByHotelIdWithSlike(hotelId);
        if (sobeHotela.isEmpty()){
            throw new ResourceNotFoundException("Hotel sa ID-jem " + hotelId + " nema sobe za rezervaciju!");
        }

        List<SobaDto> sobeHotelaDto = sobeHotela.stream().
                map(sobaMapper::mapToDto).collect(Collectors.toList());
        return sobeHotelaDto;
    }

    @Override
    public List<SobaDto> getRandomSobeHotela(Long hotelId) {
        List<SobaDto> sobe = sobaRepository.find2RandomSobeHotela(hotelId)
                .stream()
                .map(sobaMapper::mapToDto)
                .collect(Collectors.toList());
        return sobe;
    }
}
