package hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik;

public record MeResponse(
        KorisnikDetailsDto korisnik,
        long expiresAt) {
}
