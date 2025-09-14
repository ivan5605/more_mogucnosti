package hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik;

public record KorisnikAdminDto(
        Long id,
        String ime,
        String prezime,
        String email,
        Long brojRezervacija,
        Long brojRecenzija
) {
}
