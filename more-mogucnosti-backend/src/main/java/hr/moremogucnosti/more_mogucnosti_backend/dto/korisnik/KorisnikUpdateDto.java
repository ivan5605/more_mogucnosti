package hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record KorisnikUpdateDto(
        @Size(max = 50)
        @NotBlank(message = "Unesite ime!")
        String ime,

        @Size(max = 50)
        @NotBlank(message = "Unesite prezime!")
        String prezime,

        @NotBlank(message = "Unesite email!")
        @Email(message = "Neispravna email adresa!")
        //@UniqueEmail
        String email
) {
}
