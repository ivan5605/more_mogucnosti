package hr.moremogucnosti.more_mogucnosti_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class HotelDto {
    private Long id_hotel;
    private String naziv;
    private String grad;
    private String adresa;
    private boolean parking;
    private boolean wifi;
    private boolean bazen;
}
