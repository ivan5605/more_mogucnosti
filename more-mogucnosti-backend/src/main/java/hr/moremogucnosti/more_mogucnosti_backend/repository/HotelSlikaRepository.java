package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.entity.HotelSlika;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelSlikaRepository extends JpaRepository<HotelSlika, Long> {
    List<HotelSlika> findSlikaByHotelId(Long id);
}
