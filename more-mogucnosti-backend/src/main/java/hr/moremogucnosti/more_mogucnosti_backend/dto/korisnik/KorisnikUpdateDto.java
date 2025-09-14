package hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record KorisnikUpdateDto(
        @NotBlank(message = "Unesite ime!")
        String ime,

        @NotBlank(message = "Unesite prezime!")
        String prezime,

        @NotBlank(message = "Unesite email!")
        @Email(message = "Neispravna email adresa!")
        //@UniqueEmail
        String email
) {
}
