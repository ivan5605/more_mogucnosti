package hr.moremogucnosti.more_mogucnosti_backend.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import hr.moremogucnosti.more_mogucnosti_backend.validation.LozinkeMatch;
import hr.moremogucnosti.more_mogucnosti_backend.validation.UniqueEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(
        description = "Zahtjev za registraciju korisnika",
        example = """
    {
      "ime": "Marko",
      "prezime": "Horvat",
      "email": "marko@primjer.com",
      "lozinka": "Lozinka123",
      "lozinkaPotvrda": "Lozinka123"
    }
    """
)

@LozinkeMatch(
        lozinka = "lozinka",
        lozinkaPotvrda = "lozinkaPotvrda",
        message = "Lozinke se ne podudaraju."
)
public record AuthRegistracijaRequest(
        @Schema(description = "Ime korisnika", example = "Marko")
        @NotBlank(message = "Unesite ime!")
        String ime,

        @Schema(description = "Prezime korisnika", example = "Horvat")
        @NotBlank(message = "Unesite prezime!")
        String prezime,

        @Schema(description = "Email adresa", example = "marko@primjer.com")
        @NotBlank(message = "Unesite email!")
        @Email(
                message = "Neispravna email adresa!",
                regexp = "^[^\\s@]+@[^\\s@]+\\.[A-Za-z]{2,}$"
        )
        @UniqueEmail
        String email,

        @Schema(description = "Lozinka (min. 8 znakova, barem 1 veliko slovo i 1 broj)", example = "Lozinka123")
        @NotBlank(message = "Unesite lozinku!")
        @Size(min = 8, message = "Lozinka mora imati najmanje 8 znakova!")
        @Pattern(regexp = ".*[A-Z].*", message = "Lozinka mora sadržavati barem jedno veliko slovo!")
        @Pattern(regexp = ".*\\d.*", message = "Lozinka mora sadržavati barem jedan broj!")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //moze se slati u requestu, ali ne u JSON responsu
        String lozinka,

        @Schema(description = "Potvrda lozinke", example = "Lozinka123")
        @NotBlank(message = "Ponovno unesite lozinku za potvrdu!")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String lozinkaPotvrda
        ) {
}

