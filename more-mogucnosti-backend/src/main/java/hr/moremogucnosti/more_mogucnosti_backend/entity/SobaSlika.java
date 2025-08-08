package hr.moremogucnosti.more_mogucnosti_backend.entity;

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
@Table(name = "soba_slika")
public class SobaSlika extends Slika{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "soba_id", nullable = false)
    private Soba soba;

}
