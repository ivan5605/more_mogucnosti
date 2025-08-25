package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.entity.Rezervacija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RezervacijaRepository extends JpaRepository<Rezervacija, Long> {
    List<Rezervacija> findBySobaId(Long sobaId);
    List<Rezervacija> findAllBySobaId(Long sobaId);

    @Query("""
            SELECT COUNT(r) > 0 FROM Rezervacija r
            WHERE r.soba.id = :sobaId
            AND NOT (r.datumKraj < :noviPocetak OR r.datumPocetak > :noviKraj)
            """)
    boolean existsOverlappingRezervacija(@Param("sobaId") Long sobaId,
                                         @Param("noviPocetak") LocalDate noviPocetak,
                                         @Param("noviKraj") LocalDate noviKraj);
}
