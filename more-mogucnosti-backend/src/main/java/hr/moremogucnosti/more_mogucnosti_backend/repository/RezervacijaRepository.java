package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.entity.Rezervacija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RezervacijaRepository extends JpaRepository<Rezervacija, Long> {

    List<Rezervacija> findAllBySobaId(Long sobaId);

    List<Rezervacija> findAllBySoba_Hotel_Id(Long hotelId);

    @Query("""
        select count(r) > 0
        from Rezervacija r
        where r.soba.id = :sobaId
        and r.datumPocetak < :noviKraj  
        and r.datumKraj    > :noviPocetak 
    """)
    boolean postojiPreklapanje(@Param("sobaId") Long sobaId,
                               @Param("noviPocetak") LocalDate noviPocetak,
                               @Param("noviKraj") LocalDate noviKraj);

    @Query("""
            select r from Rezervacija r
            join fetch r.soba s
            join fetch s.hotel h
            where r.korisnik.id = :id AND r.datumKraj >= :datum
            order by r.datumPocetak asc
            """)
    List<Rezervacija> findWithSobaHotelByKorisnikId(@Param("id") Long id, @Param("datum") LocalDate danas);

    @Query("""
            select r from Rezervacija r
            join fetch r.soba s
            join fetch s.hotel h
            where r.datumKraj > :danasnjiDatum AND r.korisnik.id = :korisnikId
            """)
    List<Rezervacija> findAktivneRezervacijeKorisnika(@Param("danasnjiDatum") LocalDate danas, @Param("korisnikId") Long korisnikId);


    @Query("""
            select r from Rezervacija r
            join fetch r.soba s
            join fetch s.hotel h
            where r.datumKraj < :danasnjiDatum AND r.korisnik.id = :korisnikId
            """)
    List<Rezervacija> findStareRezervacijeKorisnika(@Param("danasnjiDatum") LocalDate danas, @Param("korisnikId") Long korisnikId);

    Optional<Rezervacija> findByIdAndKorisnikId(Long id, Long korisnikId);

    int deleteByIdAndKorisnikId(Long id, Long korisnikId);

    boolean existsBySobaIdAndDatumPocetakAfter(Long sobaId, LocalDate danas);
}
