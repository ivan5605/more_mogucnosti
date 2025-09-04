package hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik;

public record KorisnikViewDto(
        Long id,
        String ime,
        String prezime,
        String email
) {}
