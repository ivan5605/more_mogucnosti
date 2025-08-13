package hr.moremogucnosti.more_mogucnosti_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RezervacijaResponseDto {
    private Long idRezervacija;
    private KorisnikDto korisnikDto;
    private SobaDto sobaDto;
    private int brojOsoba;
    private LocalDate datumPocetak;
    private LocalDate datumKraj;
}
