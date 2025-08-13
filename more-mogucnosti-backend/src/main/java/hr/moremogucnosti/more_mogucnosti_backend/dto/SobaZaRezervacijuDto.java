package hr.moremogucnosti.more_mogucnosti_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class SobaZaRezervacijuDto {
    private int kapacitet;
    private BigDecimal cijenaNocenja;
    private int brojSobe;
    private boolean balkon;
    private boolean petFriendly;
    private SlikaDto glavnaSlika;
    private List<SlikaDto> sporedneSlike;
    private HotelPrikazDto hotel;
}
