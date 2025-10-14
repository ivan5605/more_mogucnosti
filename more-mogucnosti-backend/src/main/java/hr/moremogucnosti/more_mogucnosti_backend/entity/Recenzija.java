package hr.moremogucnosti.more_mogucnosti_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(
        name = "recenzija",
        indexes = {
                @Index(name = "ix_recenzija_hotel", columnList = "hotel_id"),
                @Index(name = "ix_recenzija_korisnik", columnList = "korisnik_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_recenzija_korisnik_hotel", columnNames = {"korisnik_id", "hotel_id"})
        }
)
public class Recenzija {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recenzija")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "korisnik_id", nullable = false)
    private Korisnik korisnik;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Min(1) @Max(5)
    @Column(name = "ocjena", nullable = false)
    private int ocjena;

    @Size(min = 5, max = 300)
    @NotBlank
    @Column(name = "tekst", nullable = false, length = 300)
    private String tekst;

    @Column(name = "datum", nullable = false)
    private LocalDate datum;

}
