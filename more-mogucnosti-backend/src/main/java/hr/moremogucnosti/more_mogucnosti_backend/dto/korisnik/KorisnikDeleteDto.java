package hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik;

import jakarta.validation.constraints.NotBlank;

public record KorisnikDeleteDto(
        @NotBlank(message = "Unesite lozinku!")
        String lozinka
) {
}
