package hr.moremogucnosti.more_mogucnosti_backend.dto.Slika;

import hr.moremogucnosti.more_mogucnosti_backend.entity.Soba;

public record SlikaSobaResponseDto(
        Long id,
        String putanja,
        boolean glavna,
        Soba soba
) {
}
