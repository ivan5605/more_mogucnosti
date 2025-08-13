package hr.moremogucnosti.more_mogucnosti_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class HotelPrikazDto {
    private String naziv;
    private GradDto grad;
    private String adresa;
}
