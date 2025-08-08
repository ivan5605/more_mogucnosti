package hr.moremogucnosti.more_mogucnosti_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class SlikaDto {
    private String putanja;
    //vraćam samo to, putanju korsitim za prikaz, glavnaSlika za provjeru jel glavna slika
}
