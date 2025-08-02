package hr.moremogucnosti.more_mogucnosti_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class HotelSlikaDto {
    private String putanja;
    private boolean glavnaSlika;
    //vraćam samo to, putanju korsitim za prikaz, glavnaSlika za provjeru jel glavna slika
}
