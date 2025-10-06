package hr.moremogucnosti.more_mogucnosti_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(
        name = "soba_slika",
        indexes = @Index(name = "ix_sobaSlika_soba", columnList = "soba_id")
)
public class SobaSlika extends Slika{
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "soba_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sobaSlika_soba"))
    @JsonIgnore
    private Soba soba;

}
