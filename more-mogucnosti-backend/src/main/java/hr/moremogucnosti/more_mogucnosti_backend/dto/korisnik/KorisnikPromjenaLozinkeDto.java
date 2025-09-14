package hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record KorisnikPromjenaLozinkeDto(
        @NotBlank(message = "Unesite staru lozinku!")
        String staraLozinka,

        @NotBlank(message = "Unesite novu lozinku!")
        @Size(min = 8, message = "Lozinka mora imati najmanje 8 znakova!")
        @Pattern(regexp = ".*[A-Z].*", message = "Lozinka mora sadržavati barem jedno veliko slovo!")
        @Pattern(regexp = ".*\\d.*", message = "Lozinka mora sadržavati barem jedan broj!")
        String novaLozinka,

        @NotBlank(message = "Ponovno unesite lozinku za potvrdu nove lozinke!")
        String novaLozinkaPotvrda
) {
}
