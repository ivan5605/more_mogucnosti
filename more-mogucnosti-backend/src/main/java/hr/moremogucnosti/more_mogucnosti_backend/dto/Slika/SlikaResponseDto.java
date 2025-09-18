package hr.moremogucnosti.more_mogucnosti_backend.dto.Slika;

public record SlikaResponseDto(
        Long id,
        String putanja,
        boolean glavna
    //vraćam samo to, putanju korsitim za prikaz, glavnaSlika za provjeru jel glavna slika
) {}
