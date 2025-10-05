package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RezervacijaRepositoryTest {

    @Autowired
    RezervacijaRepository repository;

    @Autowired
    TestEntityManager entityManager;

    Uloga uloga;
    Korisnik korisnik;
    Soba soba;
    Grad grad;
    Hotel hotel;

    @BeforeEach
    void ucitaj() {
        uloga = new Uloga();
        uloga.setNazivUloga("USER");
        entityManager.persist(uloga);

        grad = new Grad();
        grad.setImeGrad("Zagreb");
        grad.setSlug("zagreb");
        entityManager.persist(grad);

        hotel = new Hotel();
        hotel.setNaziv("Hotel");
        hotel.setWifi(true);
        hotel.setParking(true);
        hotel.setBazen(true);
        hotel.setAdresa("Adresa");
        hotel.setAktivno(true);
        hotel.setGrad(grad);
        entityManager.persist(hotel);

        soba = new Soba();
        soba.setCijenaNocenja(BigDecimal.valueOf(100));
        soba.setBalkon(true);
        soba.setPetFriendly(true);
        soba.setKapacitet(3);
        soba.setBrojSobe(101);
        soba.setHotel(hotel);
        soba.setAktivno(true);
        entityManager.persist(soba);

        korisnik = new Korisnik();
        korisnik.setIme("Ivo");
        korisnik.setPrezime("Ivić");
        korisnik.setEmail("ivo@gmail.com");
        korisnik.setAktivan(true);
        korisnik.setUloga(uloga);
        korisnik.setLozinka("lozinka");
        entityManager.persist(korisnik);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findAllBySobaId() {
        Rezervacija rz1 = setRezervacija(3, LocalDate.of(2025, 10, 10), LocalDate.of(2025, 11, 11));
        entityManager.persist(rz1);
        Rezervacija rz2 = setRezervacija(3, LocalDate.of(2025, 10, 15), LocalDate.of(2025, 11, 11));
        entityManager.persist(rz2);

        entityManager.flush();
        entityManager.clear();

        var rezultat = repository.findAllBySobaId(soba.getId());

        assertFalse(rezultat.isEmpty());
        assertEquals(2, rezultat.size());
    }

    @Test
    void findAllBySobaId_notFound() {
        var rezultat = repository.findAllBySobaId(99L);

        assertTrue(rezultat.isEmpty());
    }

    @Test
    void existsOverlappingRezervacija_false() {
        Rezervacija rz1 = setRezervacija(3, LocalDate.of(2025, 10, 10), LocalDate.of(2025, 11, 11));
        entityManager.persist(rz1);

        entityManager.flush();
        entityManager.clear();

        var rezultat = repository.existsOverlappingRezervacija(soba.getId(), LocalDate.of(2025, 12, 12), LocalDate.of(2025, 12, 20));

        assertFalse(rezultat);
    }

    @Test
    void existsOverlappingRezervacija_true() {
        Rezervacija rz1 = setRezervacija(3, LocalDate.of(2025, 10, 10), LocalDate.of(2025, 11, 11));
        entityManager.persist(rz1);

        entityManager.flush();
        entityManager.clear();

        var rezultat = repository.existsOverlappingRezervacija(soba.getId(), LocalDate.of(2025, 10, 12), LocalDate.of(2025, 12, 20));

        assertTrue(rezultat);
    }

    @Test
    void findAktivneRezervacijeKorisnika() {
        Rezervacija rz1 = setRezervacija(3, LocalDate.of(2025, 10, 10), LocalDate.of(2025, 11, 11));
        entityManager.persist(rz1);
        Rezervacija rz2 = setRezervacija(3, LocalDate.of(2025, 10, 15), LocalDate.of(2025, 11, 11));
        entityManager.persist(rz2);
        Rezervacija rz3 = setRezervacija(3, LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 3));
        entityManager.persist(rz3);

        entityManager.flush();
        entityManager.clear();

        var rezultat = repository.findAktivneRezervacijeKorisnika(LocalDate.now(), korisnik.getId());

        assertEquals(2, rezultat.size());
    }

    @Test
    void findStareRezervacijeKorisnika() {
        Rezervacija rz1 = setRezervacija(3, LocalDate.of(2025, 10, 10), LocalDate.of(2025, 11, 11));
        entityManager.persist(rz1);
        Rezervacija rz2 = setRezervacija(3, LocalDate.of(2025, 10, 15), LocalDate.of(2025, 11, 11));
        entityManager.persist(rz2);
        Rezervacija rz3 = setRezervacija(3, LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 3));
        entityManager.persist(rz3);

        entityManager.flush();
        entityManager.clear();

        var rezultat = repository.findStareRezervacijeKorisnika(LocalDate.now(), korisnik.getId());

        assertEquals(1, rezultat.size());
    }

    @Test
    void deleteByIdAndKorisnikId() {
        Rezervacija rz1 = setRezervacija(3, LocalDate.of(2025, 10, 10), LocalDate.of(2025, 11, 11));
        entityManager.persist(rz1);

        entityManager.flush();
        entityManager.clear();

        var rezultat = repository.deleteByIdAndKorisnikId(rz1.getId(), korisnik.getId());

        assertEquals(1, rezultat);
        assertTrue(repository.findById(rz1.getId()).isEmpty());
    }

    @Test
    void deleteByIdAndKorisnikId_kriviKorisnikId() {
        Rezervacija rz1 = setRezervacija(3, LocalDate.of(2025, 10, 10), LocalDate.of(2025, 11, 11));
        entityManager.persist(rz1);

        entityManager.flush();
        entityManager.clear();

        var rezultat = repository.deleteByIdAndKorisnikId(rz1.getId(), 99L);

        assertEquals(0, rezultat);
        assertTrue(repository.findById(rz1.getId()).isPresent());
    }

    @Test
    void deleteByIdAndKorisnikId_kriviRezId() {
        var rezultat = repository.deleteByIdAndKorisnikId(99L, korisnik.getId());

        assertEquals(0, rezultat);
    }

    private Rezervacija setRezervacija(int brojOsoba, LocalDate pocetak, LocalDate kraj) {
        Rezervacija rezervacija = new Rezervacija();
        rezervacija.setKorisnik(korisnik);
        rezervacija.setSoba(soba);
        rezervacija.setBrojOsoba(brojOsoba);
        rezervacija.setDatumPocetak(pocetak);
        rezervacija.setDatumKraj(kraj);
        return rezervacija;
    }
}