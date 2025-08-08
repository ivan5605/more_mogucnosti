package hr.moremogucnosti.more_mogucnosti_backend.mapper;

import hr.moremogucnosti.more_mogucnosti_backend.dto.SlikaDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.SobaDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Slika;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Soba;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor

@Component
public class SobaMapper {

    private final SlikaMapper slikaMapper;

    public SobaDto mapToDto(Soba soba){
        if (soba==null){
            throw new ResourceNotFoundException("Nema sobe za mapiranje u DTO objekt");
        }
        SobaDto sobaDto = new SobaDto();
        sobaDto.setKapacitet(soba.getKapacitet());
        sobaDto.setCijenaNocenja(soba.getCijenaNocenja());
        sobaDto.setBrojSobe(soba.getBrojSobe());
        sobaDto.setBalkon(soba.isBalkon());
        sobaDto.setPetFriendly(soba.isPetFriendly());

        sobaDto.setGlavnaSlika(soba.getSlike()
                .stream()
                .filter(Slika::isGlavnaSlika)
                .map(slikaMapper::mapToDto)
                .findFirst()
                .orElse(null));

        List<SlikaDto> slikeDto = soba.getSlike()
                .stream()
                .filter(slika -> !slika.isGlavnaSlika())
                .map(slikaMapper::mapToDto)
                .collect(Collectors.toList());

        sobaDto.setSporedneSlike(slikeDto);
        return sobaDto;
    }
}
