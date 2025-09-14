package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.dto.recenzija.RecenzijaHotelStatusDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Recenzija;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecenzijaRepository extends JpaRepository<Recenzija, Long> {

//    public interface RecenzijaKorisnikView {
//        Long getId();
//        Integer getOcjena();
//        String getTekst();
//        LocalDate getDatum();
//        HotelView getHotel();
//
//        interface HotelView {
//            Long getId();
//            String getNaziv();
//        }
//    }

    @Query("""
            SELECT r from Recenzija r
            left join fetch r.korisnik k
            where r.hotel.id = :hotelId
            order by r.datum desc 
            """)
    List<Recenzija> findByHotelIdWithKorisnik(@Param("hotelId") Long hotelId);

    //Boolean existsByKorisnikIdAndHotelId(Long korisnikId, Long hotelId); ionak mi treba recenzija ako postoji

    Optional<Recenzija> findByKorisnikIdAndHotelId(Long korisnikId, Long hotelId);

    @Query("""
            SELECT count(r) as brojRecenzija,
                AVG(r.ocjena) as prosjekRecenzija
            from Recenzija r
            where r.hotel.id = :hotelId
            """)
    RecenzijaHotelStatusDto recenzijeInfoByHotelId(@Param("hotelId") Long hotelId);

//    @EntityGraph(attributePaths = {"hotel"})
//    List<RecenzijaKorisnikView> findByKorisnikEmailOrderByDatumDesc(String email);

    @Query("""
            select r from Recenzija r
            join fetch r.hotel h
            where r.korisnik.id = :id
            order by r.datum desc
            """)
    List<Recenzija> findWithHotelByKorisnikId(@Param("id") Long id);

    @EntityGraph(attributePaths = {"hotel"})
    List<Recenzija> findAllByKorisnikIdOrderByDatumDesc(Long id);

    int deleteByIdAndKorisnik_Id(Long id, Long korisnikId);
}
