package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    @Query(value = "SELECT * FROM hotel ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<Hotel> find3RandomHotels();

    @Query("SELECT DISTINCT h FROM Hotel h LEFT JOIN FETCH h.slike WHERE h.id = :idHotel")
    Optional<Hotel> findByHotelIdWithSlike(@Param("idHotel") Long id);
}
