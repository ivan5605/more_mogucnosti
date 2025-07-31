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
@Table(name = "korisnik")
public class Korisnik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_korisnik")
    private Long idKorisnik;

    @Column (nullable = false)
    private String ime;

    @Column (nullable = false)
    private String prezime;

    @Column (nullable = false, unique = true)
    private String email;

    @Column (nullable = false)
    private String lozinka;

    @ManyToOne
    @JoinColumn(name = "uloga_id", nullable = false)
    private Uloga uloga;
}
