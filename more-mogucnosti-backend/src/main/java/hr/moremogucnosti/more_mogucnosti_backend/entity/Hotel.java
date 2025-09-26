package hr.moremogucnosti.more_mogucnosti_backend.entity;

import jakarta.persistence.*;
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
@Table(name = "hotel")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hotel")
    private Long id;

    @Column(name = "naziv", nullable = false)
    private String naziv;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grad_id", nullable = false)
    private Grad grad;

    @Column(name = "adresa", nullable = false)
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
