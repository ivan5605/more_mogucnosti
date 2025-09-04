package hr.moremogucnosti.more_mogucnosti_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data

@Entity
@Table(name = "recenzija")
public class Recenzija {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recenzija")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "korisnik_id", nullable = false)
    private Korisnik korisnik;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Column(name = "ocjena", nullable = false)
    private int ocjena;

    @Column(name = "tekst", nullable = false)
    private String tekst;

    @Column(name = "datum", nullable = false)
    private LocalDate datum;
}
