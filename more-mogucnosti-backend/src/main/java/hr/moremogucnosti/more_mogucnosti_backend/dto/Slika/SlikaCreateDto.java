package hr.moremogucnosti.more_mogucnosti_backend.dto.Slika;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SlikaCreateDto(
        @NotBlank(message = "Unesite putanju slike!")
        String putanja,

        @NotNull(message = "Je li slika glavna ili sporedna?")
        Boolean glavna
) {
}
