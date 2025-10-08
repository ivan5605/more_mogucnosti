package hr.moremogucnosti.more_mogucnosti_backend.dto.recenzija;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RecenzijaCreateDto (
        @Min(1) @Max(5)
        int ocjena,

        @NotBlank @Size(min = 5, max = 300)
        String tekst
) {}
