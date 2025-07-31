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
@Table(name = "hotel")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hotel")
    private Long id_hotel;

    @Column(name = "naziv", nullable = false)
    private String naziv;

    @ManyToOne
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
}
