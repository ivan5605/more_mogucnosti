package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.entity.Soba;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SobaRepository extends JpaRepository<Soba, Long> {
    List<Soba> findByHotelId(Long id);

    @Query("SELECT DISTINCT s FROM Soba s LEFT JOIN FETCH s.slike WHERE s.hotel.id = :hotelId")
    List<Soba> findByHotelIdWithSlike(@Param("hotelId") Long id);

    //bez FETCH querry vraca sobe ali povezane slike se ne učitavaju odmah
    //manje podataka vraćeno odmah, ali N+1 prolbema - za N soba ide N puta poseban upit za slike

    //sa fetch odmah vraća sobe sa svim slikama (eager loading), jedan upit vraća sve potrebne podatke
    //prednost ako uvijek trebam slike uz sobe
    //ali može dohvatiti puno podataka i nepotrebno opteretiti memoriju ilio bazu ako slika ima puno

    //Speing odnosno Hibernate je "pametan" i sam prepozna kad se radi o istom entitetu Soba na temelju id_soba
    //automatski napravi deduplikaciju i napuni listu unutar svake Soba instance

    @Query(value = "SELECT * FROM Soba WHERE hotel_id = :hotelId ORDER BY RAND() LIMIT 2;", nativeQuery = true)
    List<Soba> find2RandomSobeHotela(@Param("hotelId") Long id);
}
