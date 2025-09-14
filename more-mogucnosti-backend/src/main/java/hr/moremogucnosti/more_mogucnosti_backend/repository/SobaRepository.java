package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.entity.Soba;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SobaRepository extends JpaRepository<Soba, Long> {

    @Query("""
            SELECT DISTINCT s 
            FROM Soba s 
            LEFT JOIN FETCH s.slike
            WHERE s.hotel.id = :hotelId
             """)
    List<Soba> findByHotelIdWithSlike(@Param("hotelId") Long id);

    //bez FETCH querry vraca sobe ali povezane slike se ne učitavaju odmah
    //manje podataka vraćeno odmah, ali N+1 prolbema - za N soba ide N puta poseban upit za slike

    //sa fetch odmah vraća sobe sa svim slikama (eager loading), jedan upit vraća sve potrebne podatke
    //prednost ako uvijek trebam slike uz sobe
    //ali može dohvatiti puno podataka i nepotrebno opteretiti memoriju ilio bazu ako slika ima puno

    //Speing odnosno Hibernate je "pametan" i sam prepozna kad se radi o istom entitetu Soba na temelju id_soba
    //automatski napravi deduplikaciju i napuni listu unutar svake Soba instance

    @Query("SELECT DISTINCT s FROM Soba s LEFT JOIN FETCH s.slike WHERE s.id = :id")
    Optional<Soba> findByIdWithSlike(@Param("id") Long id);

    //za findById JpaRepository već ima default implementaciju: Optional<T> findById(ID id); pa ne treba Optional
    //a kao ovo nije defaultna treba...

    //ako vraćaš kolekcije (List) ne treba optional jer prazna lista već sama po sebi označava da nema rezultata
    //ne vraća se null nego Collections.emptyList()

    @Query("""
            SELECT DISTINCT s
            FROM Soba s
            LEFT JOIN FETCH s.slike
            LEFT JOIN FETCH s.hotel h
            LEFT JOIN FETCH h.grad
            WHERE s.id = :id""")
    Optional<Soba> getSobaByIdWithHotelAndSlike(@Param("id") Long id);

}
