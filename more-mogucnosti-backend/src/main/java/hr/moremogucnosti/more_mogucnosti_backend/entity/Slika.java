package hr.moremogucnosti.more_mogucnosti_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

//označavu baznu (nad)klasu koja ne predstavlja svoju tablicu u bazi
//sva polja iz klase če biti naslijeđena i mapirana kao da su u podklasi
//nemres napraviti repository jer nije entitet
@MappedSuperclass
public abstract class Slika {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_slika")
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, name = "putanja", length = 255)
    private String putanja;

    @Column(nullable = false, name = "glavna_slika")
    private boolean glavnaSlika = false;

    //nema @Entity jer se ne mapira direktno u bazu
    //nema @ManyToOne foreign keys jer su oni specifični za podklase
}
