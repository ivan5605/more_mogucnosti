package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface KorisnikRepository extends JpaRepository<Korisnik, Long> {
    boolean existsByEmail(String email);

    @Query("select k from Korisnik k join fetch k.uloga where k.email = :email")
    Optional<Korisnik> findByEmail(@Param("email") String email);

}
