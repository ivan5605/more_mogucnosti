package hr.moremogucnosti.more_mogucnosti_backend.dto.soba;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record SobaUpdateDto(
        @NotNull(message = "Unesite kapacitet sobe!")
        @Min(value = 1, message = "Kapacitet mora minimalno biti 1!")
        int kapacitet,

        @NotNull(message = "Unesite cijenu noćenja!")
        BigDecimal cijenaNocenja,

        @NotNull(message = "Unesite info o balkonu - true/false")
        Boolean balkon,

        @NotNull(message = "Je li soba pet friendly? - true/false")
        Boolean petFriendly
) {
}
