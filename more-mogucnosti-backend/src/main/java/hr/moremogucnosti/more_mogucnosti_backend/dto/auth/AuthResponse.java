package hr.moremogucnosti.more_mogucnosti_backend.dto.auth;

public record AuthResponse(
        String token,
        String email,
        String ime,
        String uloga) {
}
