package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.entity.HotelSlika;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SlikaHotelRepository extends JpaRepository<HotelSlika, Long> {

    Optional<HotelSlika> findByHotelIdAndGlavnaSlikaTrue(Long hotelId);

    //oznacava da je update/delete/insert a ne select
    @Modifying
    @Query("""
            update HotelSlika s
            set s.glavnaSlika = false
            where s.hotel.id = :hotelId and s.glavnaSlika = true""")
    int ocistiGlavnu(@Param("hotelId") Long hotelId); //broj pogodjenih redaka
}
