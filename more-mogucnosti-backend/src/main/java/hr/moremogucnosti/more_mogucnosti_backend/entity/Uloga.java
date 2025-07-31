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
@Table(name = "uloga")
public class Uloga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_uloga")
    private Long idUloga;

    @Column (nullable = false, unique = true, name = "naziv_uloga")
    private String nazivUloga;
}
