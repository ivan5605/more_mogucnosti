package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface KorisnikRepository extends JpaRepository<Korisnik, Long> {
    boolean existsByEmail(String email);

    @Query("select k from Korisnik k join fetch k.uloga where k.email = :email")
    Optional<Korisnik> findByEmailWUloga(@Param("email") String email);

    @EntityGraph(attributePaths = {"uloga", "rezervacije", "recenzije"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Korisnik> findByEmail(@Param("email") String email);
    //anotacija veli -> dok ucitavas ovog korisnika, ucitaj i rezervacije i recenzije
    //default je LOAD mode -> pa imamo: 1 upit za korisnika, 1 upit za recenzije, 1 upit za rezervacije

    //LOAD mode - radi više upita, jedan SELECT za svaki
    //FETCH mode - odmah radi JOIN FETCH na kolekcijama, sve u jednom upitu
}
