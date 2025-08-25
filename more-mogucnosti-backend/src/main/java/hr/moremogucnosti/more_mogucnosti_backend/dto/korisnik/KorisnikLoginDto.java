package hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record KorisnikLoginDto (
    @NotBlank(message = "Unesite email adresu!")
    @Email(message = "Neispravna email adresa!")
    String email,

    @NotBlank(message = "Unesite lozinku!")
    String lozinka
) {}
