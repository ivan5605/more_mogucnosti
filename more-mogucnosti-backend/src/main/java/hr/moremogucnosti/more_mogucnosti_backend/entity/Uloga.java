package hr.moremogucnosti.more_mogucnosti_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(
        name = "uloga",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_uloga_naziv", columnNames = "naziv_uloga")
        }
)
public class Uloga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_uloga")
    private Long idUloga;

    @NotBlank
    @Size(max = 50)
    @Column (nullable = false, name = "naziv_uloga", length = 50)
    private String nazivUloga;

}
