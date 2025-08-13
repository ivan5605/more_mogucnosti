package hr.moremogucnosti.more_mogucnosti_backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Hotel hotel;
    //FetchType.EAGER - standard, povezani entitet se odmah učitava - s.getHotel() - već je učitan iz baze
    //FetchType.LAZY - entitet se ne učitava odmah, tek dok zatražim - s.getHotel() - tek sad šalje upit
    //bolje performanse, manje memorije, korisno dok imam velike baze

    //JsonIgnore - kada želim potpuno izostaviti neko polje iz JSON-a, nije potrebno da se to polje šalje klijentu
    //jednostavan i brz fix za probleme s ponavljanjem, ali gubim podatke u JSON-u

    //moze i sa @JsonManagedReference i @JsonBackReference
    //@JsonManagedReference - stavim na parent stranu veze - znaci tu ce Jackson poslati podatke u JSON, to polje bude uključeno u JSON
    //@JsonBackReference - stavim ovo na child stranu veze - ovo polje se neće slati u JSON
    //time sprečavam da JSON ide u beskonačnu petlju (jer child ne šalje natrag parent)
}
