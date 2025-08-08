package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.entity.HotelSlika;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Slika;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SlikaRepository extends JpaRepository<Slika, Long> {
    List<HotelSlika> findSlikaByHotelId(Long id);
}
