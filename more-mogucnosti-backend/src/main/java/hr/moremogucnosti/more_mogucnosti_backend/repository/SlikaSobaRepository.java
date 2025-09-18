package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.entity.SobaSlika;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlikaSobaRepository extends JpaRepository<SobaSlika, Long> {
}
