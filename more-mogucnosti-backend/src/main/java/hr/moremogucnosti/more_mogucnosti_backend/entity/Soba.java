package hr.moremogucnosti.more_mogucnosti_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
@Table(name = "soba",
        uniqueConstraints = {
            @UniqueConstraint(name = "uq_soba_hotel_broj", columnNames = {"hotel_id", "broj_sobe"})
        },
        indexes = {
            @Index(name = "ix_soba_hotel", columnList = "hotel_id")
        }
)
public class Soba {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_soba")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hotel_id", nullable = false, foreignKey = @ForeignKey(name = "fk_soba_hotel"))
    @JsonIgnore
    private Hotel hotel;

    @Positive
    @Column(name = "kapacitet", nullable = false)
    private int kapacitet;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    @Column(name = "cijena_nocenja", nullable = false, precision = 10, scale = 2)
    private BigDecimal cijenaNocenja;

    @Positive
    @Column(name = "broj_sobe", nullable = false)
    private int brojSobe;

    @Column(name = "balkon")
    private boolean balkon = false;

    @Column(name = "pet_friendly")
    private boolean petFriendly = true;

    @Column(name = "aktivno")
    private boolean aktivno = false;

    @OneToMany(mappedBy = "soba", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<SobaSlika> slike;
    //mappedBy = "soba" - ja nisam vlasnik veze, druga strana (SobaSlika) drži strani ključ
    //soba je ime varijable u entitetu SobaSlika koji ima strani ključ
    //izbjegavamo stvaranje dodatne JOIN TABLICE, jer JPA zna da SobaSlika već ima strani ključ soba_id

    //cascade = CascadeType.ALL - dok nekaj napravi z sobom to se dogodi i z slikama
    //izbrišem sobu - izbrišu se i sve slike te sobe!

    //fetch = FetchType.LAZY - slike se ne budu učitale odmah dok dohvatim Soba
    //s LAZY imam veću kontrolu jer mi ne trebaju uvijek, nego onda kroz Query to odredim
    //ucitaju se samo dok ih eksplicitno pozoveš, npr. soba.getSlike()

    //orphanRemoval znači da će JPA automatski obrisati objekt iz baze podataka ako ga maknem iz liste slike u entitetu Soba
}
