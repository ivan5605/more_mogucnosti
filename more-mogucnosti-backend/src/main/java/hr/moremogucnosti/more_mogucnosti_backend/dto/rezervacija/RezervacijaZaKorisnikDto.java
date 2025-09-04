package hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija;

import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaViewDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RezervacijaZaKorisnikDto(
        Long id,
        SobaViewDto soba,
        int brojOsoba,
        LocalDate datumPocetak,
        LocalDate datumKraj,
        Long brojNocenja,
        BigDecimal ukupnaCijena
) {
}
