package hr.moremogucnosti.more_mogucnosti_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class KorisnikRegistracijaDto {

    @NotBlank(message = "Unesite ime!")
    private String ime;

    @NotBlank(message = "Unesite prezime!")
    private String prezime;

    @NotBlank(message = "Unesite email!")
    @Email(message = "Neispravna email adresa!")
    private String email;

    @NotBlank(message = "Unesite lozinku!")
    @Size(min = 8, message = "Lozinka mora imati najmanje 8 znakova!")
    @Pattern(regexp = ".*[A-Z].*", message = "Lozinka mora sadržavati barem jedno veliko slovo!")
    @Pattern(regexp = ".*\\d.*", message = "Lozinka mora sadržavati barem jedan broj!")
    private String lozinka;

    @NotBlank(message = "Ponovno unesite lozinku za potvrdu!")
    private String lozinkaPotvrda;
}
