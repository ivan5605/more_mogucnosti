package hr.moremogucnosti.more_mogucnosti_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (
        name = "rezervacija",
        indexes = {
                @Index(name = "ix_rezervacija_korisnik", columnList = "korisnik_id"),
                @Index(name = "ix_rezervacija_soba", columnList = "soba_id")
        }
)
public class Rezervacija {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rezervacija")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "korisnik_id", nullable = false, foreignKey = @ForeignKey(name = "fk_rezervacija_korisnik"))
    private Korisnik korisnik;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "soba_id", nullable = false, foreignKey = @ForeignKey(name = "fk_rezervacija_soba"))
    private Soba soba;

    //mnogi objekti pripadaju jednom
    //na strani entiteta gdi je @ManyToOne nalazi se strani ključ
    //više rezervacija pripada jednom korisniku/sobi

    @Positive
    @Column(name = "broj_osoba", nullable = false)
    private int brojOsoba;

    @NotNull
    @Column(name = "datum_pocetak", nullable = false)
    private LocalDate datumPocetak;

    @NotNull
    @Column(name = "datum_kraj", nullable = false)
    private LocalDate datumKraj;

}
