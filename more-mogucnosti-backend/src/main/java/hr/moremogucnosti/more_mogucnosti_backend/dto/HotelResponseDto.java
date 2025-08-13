package hr.moremogucnosti.more_mogucnosti_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class HotelResponseDto {
    private Long id;
    private String naziv;
    private GradDto grad;
    private String adresa;
    private boolean parking;
    private boolean wifi;
    private boolean bazen;
    private SlikaDto glavnaSlika;
    private List<SlikaDto> sporedneSlike;
}
