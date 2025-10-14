package hr.moremogucnosti.more_mogucnosti_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "hotel",
        indexes = {
                @Index(name = "ix_hotel_grad", columnList = "grad_id")
        }
)
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hotel")
    private Long id;

    @NotBlank
    @Size(max = 75)
    @Column(name = "naziv", nullable = false, length = 75)
    private String naziv;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grad_id", nullable = false, foreignKey = @ForeignKey(name = "fk_hotel_grad"))
    private Grad grad;

    @NotBlank
    @Size(max = 100)
    @Column(name = "adresa", nullable = false, length = 100)
    private String adresa;

    @Column(name = "parking", nullable = false)
    private boolean parking;

    @Column(name = "wifi", nullable = false)
    private boolean wifi;

    @Column(name = "bazen", nullable = false)
    private boolean bazen;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<HotelSlika> slike;

    @Column(name = "aktivno")
    private boolean aktivno = false;

    //lista Soba???

    //@OneToMany jedan objekat ima više povezanih - 1 hotel ima više slika/soba
    //tu anotaciju stavljam na stranu veze gdi ocu imati kolekciju (listu) povezanih objekata

}
