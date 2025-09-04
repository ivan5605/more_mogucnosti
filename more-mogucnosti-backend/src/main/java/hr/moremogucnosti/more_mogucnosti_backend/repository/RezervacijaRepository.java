package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.entity.Rezervacija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RezervacijaRepository extends JpaRepository<Rezervacija, Long> {

    List<Rezervacija> findAllBySobaId(Long sobaId);

    @Query("""
        select count(r) > 0
        from Rezervacija r
        where r.soba.id = :sobaId
        and r.datumPocetak < :noviKraj  
        and r.datumKraj    > :noviPocetak 
    """)
    boolean existsOverlappingRezervacija(@Param("sobaId") Long sobaId,
                                         @Param("noviPocetak") LocalDate noviPocetak,
                                         @Param("noviKraj") LocalDate noviKraj);

    @Query("""
            select r from Rezervacija r
            join fetch r.soba s
            join fetch s.hotel h
            where r.korisnik.email = :korisnikEmail
            order by r.datumPocetak desc
            """)
    List<Rezervacija> findWithSobaHotelByKorisnikEmail(@Param("korisnikEmail") String email);
}
