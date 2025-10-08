package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikAdminDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KorisnikRepository extends JpaRepository<Korisnik, Long> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    //@Query("select k from Korisnik k join fetch k.uloga where k.email = :email")
    @EntityGraph(attributePaths = "uloga")
    Optional<Korisnik> findByEmail(String email);

    @Query("select k from Korisnik k join fetch k.uloga where k.id = :id")
    Optional<Korisnik> findByIdWUloga(@Param("id") Long id);

    @Query("""
            select new hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikAdminDto (
                k.id, k.ime, k.prezime, k.email,
                (select count (rz) from Rezervacija rz where rz.korisnik = k),
                (select count (rc) from Recenzija rc where rc.korisnik = k),
                k.aktivan
            )
            from Korisnik k where k.uloga.nazivUloga = 'USER'
            """)
    List<KorisnikAdminDto> findKorisnikWithCount();
}
