package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.entity.SobaSlika;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SlikaSobaRepository extends JpaRepository<SobaSlika, Long> {

    Optional<SobaSlika> findBySobaIdAndGlavnaSlikaTrue(Long sobaId);

    @Modifying(clearAutomatically = true)
    @Query("""
            update SobaSlika s
            set s.glavnaSlika = false
            where s.soba.id = :sobaId and s.glavnaSlika = true""")
    int ocistiGlavnu(@Param("sobaId") Long sobaId);

    Optional<SobaSlika> findFirstBySobaIdAndGlavnaSlikaFalseOrderByIdAsc(Long sobaId);

    Optional<SobaSlika> findByIdAndSobaId(Long id, Long sobaId);
}
