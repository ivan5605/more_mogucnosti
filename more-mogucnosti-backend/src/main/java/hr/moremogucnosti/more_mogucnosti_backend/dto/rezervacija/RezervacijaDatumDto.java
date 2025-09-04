package hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija;

import java.time.LocalDate;

public record RezervacijaDatumDto(
        Long idRezervacija,
        LocalDate datumPocetak,
        LocalDate datumKraj
) {
}
