package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.entity.Uloga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UlogaRepository extends JpaRepository<Uloga, Long> {
    Optional<Uloga> findByNazivUloga(String naziv);
}
