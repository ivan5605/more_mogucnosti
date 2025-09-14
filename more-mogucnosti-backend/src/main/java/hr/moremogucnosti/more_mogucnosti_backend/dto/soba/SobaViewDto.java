package hr.moremogucnosti.more_mogucnosti_backend.dto.soba;

import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.HotelViewDto;

import java.math.BigDecimal;

public record SobaViewDto(
        Long id,
        BigDecimal cijenaNocenja,
        int brojSobe,
        int kapacitet,
        HotelViewDto hotel
) {
}
