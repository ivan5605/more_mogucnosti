package hr.moremogucnosti.more_mogucnosti_backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthRegistracijaRequest(
        @NotBlank(message = "Unesite ime!")
        String ime,

        @NotBlank(message = "Unesite prezime!")
        String prezime,

        @NotBlank(message = "Unesite email!")
        @Email(
                message = "Neispravna email adresa!",
                regexp = "^[^\\s@]+@[^\\s@]+\\.[A-Za-z]{2,}$"
        )
        String email,

        @NotBlank(message = "Unesite lozinku!")
        @Size(min = 8, message = "Lozinka mora imati najmanje 8 znakova!")
        @Pattern(regexp = ".*[A-Z].*", message = "Lozinka mora sadržavati barem jedno veliko slovo!")
        @Pattern(regexp = ".*\\d.*", message = "Lozinka mora sadržavati barem jedan broj!")
        String lozinka,

        @NotBlank(message = "Ponovno unesite lozinku za potvrdu!") String lozinkaPotvrda
        ) {
}
