package hr.moremogucnosti.more_mogucnosti_backend.dto.hotel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HotelCreateDto(
        @NotBlank(message = "Unesite naziv hotela!")
        String naziv,

        @NotBlank(message = "Unesite grad!")
        String gradNaziv,

        @NotBlank(message = "Unesite adresu hotela!")
        String adresa,

        @NotNull(message = "Unesite info o parkingu - true/false")
        Boolean parking,

        @NotNull(message = "Unesite info o wifi-u - true/false")
        Boolean wifi,

        @NotNull(message = "Unesite info o bazenu - true/false")
        Boolean bazen
) {
}
