package hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RezervacijaCreateDto (
    @NotNull(message = "Rezervacija mora imati hotel!")
    Long sobaId,

    @NotNull(message = "Obavezan unos broja osoba!")
    //@Max(value = ) samo za fiksne brojeve!! nemrem staviti da povlači iz baze
    int brojOsoba,

    @NotNull(message = "Datum pocetka rezervacije je obavezan!")
    @FutureOrPresent(message = "Datum početka ne smije biti prije današnjeg dana!")
    LocalDate datumPocetak,

    @NotNull(message = "Datum kraja rezervacije je obavezan!")
    @Future(message = "Datum kraja mora biti u budućnosti!")
    LocalDate datumKraj
) {}
