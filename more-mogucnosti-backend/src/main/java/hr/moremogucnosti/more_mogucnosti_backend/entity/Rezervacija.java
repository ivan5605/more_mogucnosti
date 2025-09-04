package hr.moremogucnosti.more_mogucnosti_backend.entity;

import jakarta.persistence.*;
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
@Table (name = "rezervacija")
public class Rezervacija {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rezervacija")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "korisnik_id", nullable = false)
    private Korisnik korisnik;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "soba_id", nullable = false)
    private Soba soba;

    //mnogi objekti pripadaju jednom
    //na strani entiteta gdi je @ManyToOne nalazi se strani ključ
    //više rezervacija pripada jednom korisniku/hotelu

    @Column(name = "broj_osoba", nullable = false)
    private int brojOsoba;

    @Column(name = "datum_pocetak", nullable = false)
    private LocalDate datumPocetak;

    @Column(name = "datum_kraj", nullable = false)
    private LocalDate datumKraj;
}
