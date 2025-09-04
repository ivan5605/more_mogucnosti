package hr.moremogucnosti.more_mogucnosti_backend.dto.recenzija;

import jakarta.validation.constraints.NotNull;

public record RecenzijaCreateDto (
        @NotNull(message = "Odaberite ocjenu!")
        int ocjena,

        String tekst
) {}
