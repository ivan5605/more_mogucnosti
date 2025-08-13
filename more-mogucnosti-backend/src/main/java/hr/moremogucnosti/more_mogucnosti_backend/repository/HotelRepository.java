package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    //radim LEFT JOIN jer mi uvijek trebaju slike, radim kao EAGER fetch a ne LAZY fetch
    //znači da odmah dobim hotele s učitanim slikama
    @Query("""
            SELECT DISTINCT h 
            FROM Hotel h 
            LEFT JOIN FETCH h.slike
            LEFT JOIN FETCH h.grad 
            WHERE h.id IN :ids""")
    List<Hotel> find3RandomHotelsWithSlike(@Param("ids") List<Long> ids);

    @Query("SELECT h.id FROM Hotel h ORDER BY function('RAND')")
    List<Long> findRandomHotelIds(Pageable pageable);

    @Query("""
            SELECT DISTINCT h 
            FROM Hotel h 
            LEFT JOIN FETCH h.slike 
            LEFT JOIN FETCH h.grad
            WHERE h.id = :idHotel
            """)
    Optional<Hotel> findByHotelIdWithSlike(@Param("idHotel") Long id);

    @Query("""
            SELECT DISTINCT h 
            FROM Hotel h 
            LEFT JOIN FETCH h.slike
            LEFT JOIN FETCH h.grad
            """)
    List<Hotel> findAllWithSlike();

    //left join fetch - spaja hotele sa slikama i učitava ih odmah (eager) u istom upitu

    //radim i left join za grad - jer ako nema automatski se generira lazy fetch i šalje se dodatan SQL upit

    //distinct - sprečava da dobiješ duplikate hotela u listi (jer svaki hotel se može pojaviti više puta zbog slika)
    //iako bi Hibernate to sam deduplicirao, DISTINCT smanjuje duplikate već na SQL razini
}
