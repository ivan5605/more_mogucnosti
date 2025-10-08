package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.entity.Grad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GradRepository extends JpaRepository<Grad, Long> {

    Optional<Grad> findBySlug(String slug);
}
