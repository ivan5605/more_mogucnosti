package hr.moremogucnosti.more_mogucnosti_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
        name = "grad",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_grad_slug", columnNames = "slug")
        }
)
public class Grad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grad")
    private Long idGrad;

    @NotBlank
    @Column(name = "ime_grad", nullable = false)
    private String imeGrad;

    @NotBlank
    @Column(name = "slug", nullable = false)
    //@NaturalId ?
    private String slug;

}
