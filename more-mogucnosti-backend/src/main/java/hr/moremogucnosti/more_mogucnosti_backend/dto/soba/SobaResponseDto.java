package hr.moremogucnosti.more_mogucnosti_backend.dto.soba;

import hr.moremogucnosti.more_mogucnosti_backend.dto.SlikaResponseDto;

import java.math.BigDecimal;
import java.util.List;

public record SobaResponseDto(
    Long id,
    int kapacitet,
    BigDecimal cijenaNocenja,
    int brojSobe,
    boolean balkon,
    boolean petFriendly,
    SlikaResponseDto glavnaSlika,
    List<SlikaResponseDto> sporedneSlike
) {}
