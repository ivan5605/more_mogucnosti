package hr.moremogucnosti.more_mogucnosti_backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(
        name = "korisnik",
        uniqueConstraints = {
            @UniqueConstraint(name = "uq_korisnik_email", columnNames = "email")
        },
        indexes = {
            @Index(name = "ix_korisnik_uloga", columnList = "uloga_id")
        })
public class Korisnik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_korisnik")
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column (nullable = false, length = 50)
    private String ime;

    @NotBlank
    @Size(max = 50)
    @Column (nullable = false, length = 50)
    private String prezime;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column (nullable = false, length = 100)
    private String email;

    @NotBlank
    @Column (nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String lozinka;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uloga_id", nullable = false, foreignKey = @ForeignKey(name = "fk_korisnik_uloga"))
    private Uloga uloga;

    @OneToMany(mappedBy = "korisnik", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("datumPocetak DESC")
    @JsonIgnore
    private List<Rezervacija> rezervacije;

    @OneToMany(mappedBy = "korisnik", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("datum DESC")
    @JsonIgnore
    private List<Recenzija> recenzije;

    @Column(name = "aktivan", nullable = false)
    private boolean aktivan = true;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Korisnik korisnik = (Korisnik) o;
        return getId() != null && Objects.equals(getId(), korisnik.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
