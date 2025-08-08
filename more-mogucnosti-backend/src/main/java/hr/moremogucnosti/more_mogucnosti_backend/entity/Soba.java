package hr.moremogucnosti.more_mogucnosti_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "soba")
public class Soba {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSoba")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Column(name = "kapacitet", nullable = false)
    private int kapacitet;

    @Column(name = "cijena_nocenja", nullable = false)
    private BigDecimal cijenaNocenja;

    @Column(name = "broj_sobe", nullable = false)
    private int brojSobe;

    @Column(name = "balkon", nullable = false)
    private boolean balkon;

    @Column(name = "pet_friendly")
    private boolean petFriendly = true;

    @OneToMany(mappedBy = "soba", cascade = CascadeType.ALL, fetch = FetchType.EAGER,orphanRemoval = true)
    private List<SobaSlika> slike;
    //mappedBy = "soba" - ja nisam vlasnik veze, druga strana (SobaSlika) drži strani ključ
    //soba je ime varijable u entitetu SobaSlika koji ima strani ključ
    //izbjegavamo stvaranje dodatne JOIN TABLICE, jer JPA zna da SobaSlika već ima strani ključ soba_id

    //cascade = CascadeType.ALL - dok nekaj napravi z sobom to se dogodi i z slikama
    //izbrišem sobu - izbrišu se i sve slike te sobe!

    //fetch = FetchType.LAZY - slike se ne budu učitale odmah dok dohvatim Soba
    //ucitaju se samo dok ih eksplicitno pozoveš, npr. soba.getSlike()

    //orphanRemoval znači da će JPA automatski obrisati objekt iz baze podataka ako ga maknem iz liste slike u entitetu Soba
}
