package hr.moremogucnosti.more_mogucnosti_backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(
        @NotBlank(message = "Unesite email adresu!")
        @Email(message = "Neispravna email adresa!")
        String email,

        @NotBlank(message = "Unesite lozinku!")
        String lozinka
) { }
