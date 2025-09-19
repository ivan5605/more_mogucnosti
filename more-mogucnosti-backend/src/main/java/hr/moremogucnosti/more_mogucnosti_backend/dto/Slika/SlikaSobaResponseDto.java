package hr.moremogucnosti.more_mogucnosti_backend.dto.Slika;

import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaBasicDto;

public record SlikaSobaResponseDto(
        Long id,
        String putanja,
        boolean glavna,
        SobaBasicDto soba
) {
}
