package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.entity.Grad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradRepository extends JpaRepository<Grad, Long> {
}
