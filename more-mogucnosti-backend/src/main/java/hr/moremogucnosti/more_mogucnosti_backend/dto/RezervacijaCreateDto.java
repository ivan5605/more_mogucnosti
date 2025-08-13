package hr.moremogucnosti.more_mogucnosti_backend.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class RezervacijaCreateDto {
    @NotNull(message = "Rezervacija mora imati korisnika!")
    private Long korisnikId;

    @NotNull(message = "Rezervacija mora imati hotel!")
    private Long sobaId;

    @NotNull(message = "Obavezan unos broja osoba!")
    //@Max(value = ) samo za fiksne brojeve!! nemrem staviti da povlači iz baze
    private int brojOsoba;

    @NotNull(message = "Datum pocetka rezervacije je obavezan!")
    @Future(message = "Datum početka mora biti u budućnosti!")
    private LocalDate datumPocetak;

    @NotNull(message = "Datum kraja rezervacije je obavezan!")
    @Future(message = "Datum kraja mora biti u budućnosti!")
    private LocalDate datumKraj;
}
