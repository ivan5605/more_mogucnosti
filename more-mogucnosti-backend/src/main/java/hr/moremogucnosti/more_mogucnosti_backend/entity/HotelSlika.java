package hr.moremogucnosti.more_mogucnosti_backend.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "hotel_slika")
public class HotelSlika extends Slika{
    @ManyToOne(fetch = FetchType.LAZY) //fetch - kada se poodaci iz povezanih entiteta učitavaju
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;
    //FetchType.EAGER - standard, povezani entitet se odmah učitava - s.getHotel() - već je učitan iz baze
    //FetchType.LAZY - entitet se ne učitava odmah, tek dok zatražim - s.getHotel() - tek sad šalje upit
    //bolje performanse, manje memorije, korisno dok imam velike baze
}
