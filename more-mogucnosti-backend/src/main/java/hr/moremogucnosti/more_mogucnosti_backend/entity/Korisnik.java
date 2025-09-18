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
@Table(name = "korisnik")
public class Korisnik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_korisnik")
    private Long id;

    @Column (nullable = false)
    private String ime;

    @Column (nullable = false)
    private String prezime;

    @Column (nullable = false, unique = true)
    private String email;

    @Column (nullable = false)
    private String lozinka;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uloga_id", nullable = false)
    private Uloga uloga;

    @OneToMany(mappedBy = "korisnik", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("datumPocetak DESC")
    private List<Rezervacija> rezervacije;

    @OneToMany(mappedBy = "korisnik", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("datum DESC")
    private List<Recenzija> recenzije;

    @Column(name = "aktivan", nullable = false)
    private boolean aktivan;
}
