package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RecenzijaRepositoryTest {

    @Autowired
    RecenzijaRepository repository;

    @Autowired
    TestEntityManager entityManager;

    Uloga uloga;
    Korisnik korisnik1;
    Grad grad;
    Hotel hotel1;
    Hotel hotel2;

    @BeforeEach
    void ucitaj() {
        uloga = new Uloga();
        uloga.setNazivUloga("USER");
        entityManager.persist(uloga);

        grad = new Grad();
        grad.setImeGrad("Zagreb");
        grad.setSlug("zagreb");
        entityManager.persist(grad);

        hotel1 = new Hotel();
        hotel1.setNaziv("Hotel1");
        hotel1.setWifi(true);
        hotel1.setParking(true);
        hotel1.setBazen(true);
        hotel1.setAdresa("Adresa1");
        hotel1.setAktivno(true);
        hotel1.setGrad(grad);
        entityManager.persist(hotel1);

        hotel2 = new Hotel();
        hotel2.setNaziv("Hotel2");
        hotel2.setWifi(true);
        hotel2.setParking(true);
        hotel2.setBazen(true);
        hotel2.setAdresa("Adresa2");
        hotel2.setAktivno(true);
        hotel2.setGrad(grad);
        entityManager.persist(hotel2);

        korisnik1 = new Korisnik();
        korisnik1.setIme("Ivo");
        korisnik1.setPrezime("Ivić");
        korisnik1.setEmail("ivo@gmail.com");
        korisnik1.setAktivan(true);
        korisnik1.setUloga(uloga);
        korisnik1.setLozinka("lozinka");
        entityManager.persist(korisnik1);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findByHotelIdWithKorisnik() {
        Recenzija rc1 = new Recenzija();
        rc1.setKorisnik(korisnik1);
        rc1.setHotel(hotel1);
        rc1.setOcjena(5);
        rc1.setTekst("Super");
        rc1.setDatum(LocalDate.now());
        entityManager.persist(rc1);

        Korisnik korisnik2 = new Korisnik();
        korisnik2.setIme("Ana");
        korisnik2.setPrezime("Anić");
        korisnik2.setEmail("ana@gmail.com");
        korisnik2.setAktivan(true);
        korisnik2.setUloga(uloga);
        korisnik2.setLozinka("lozinka");
        entityManager.persist(korisnik2);

        Recenzija rc2 = new Recenzija();
        rc2.setKorisnik(korisnik2);
        rc2.setHotel(hotel1);
        rc2.setOcjena(3);
        rc2.setTekst("Okej je sve");
        rc2.setDatum(LocalDate.now());
        entityManager.persist(rc2);

        entityManager.flush();
        entityManager.clear();

        List<Recenzija> rezultat = repository.findByHotelIdWithKorisnik(hotel1.getId());

        assertFalse(rezultat.isEmpty());
        assertEquals(2, rezultat.size());

        var emailovi = rezultat.stream().map(r -> r.getKorisnik().getEmail()).toList();
        assertTrue(emailovi.containsAll(List.of("ana@gmail.com", "ivo@gmail.com")));
    }

    @Test
    void findByHotelIdWithKorisnik_notFound() {
        List<Recenzija> rezultat =  repository.findByHotelIdWithKorisnik(99L);

        assertTrue(rezultat.isEmpty());
    }

    @Test
    void findWithHotelByKorisnikId() {
        Recenzija rc1 = new Recenzija();
        rc1.setKorisnik(korisnik1);
        rc1.setHotel(hotel1);
        rc1.setOcjena(5);
        rc1.setTekst("Super");
        rc1.setDatum(LocalDate.now());
        entityManager.persist(rc1);

        Recenzija rc2 = new Recenzija();
        rc2.setKorisnik(korisnik1);
        rc2.setHotel(hotel2);
        rc2.setOcjena(3);
        rc2.setTekst("Okej je sve");
        rc2.setDatum(LocalDate.now());
        entityManager.persist(rc2);

        entityManager.flush();
        entityManager.clear();

        var rezultat = repository.findWithHotelByKorisnikId(korisnik1.getId());

        assertFalse(rezultat.isEmpty());
        assertEquals(2, rezultat.size());

        var hoteli = rezultat.stream().map(r -> r.getHotel().getNaziv()).toList();
        assertTrue(hoteli.containsAll(List.of("Hotel1", "Hotel2")));
    }

    @Test
    void findWithHotelByKorisnikId_notFound() {
        var rezultat = repository.findWithHotelByKorisnikId(99L);

        assertTrue(rezultat.isEmpty());
    }

    @Test
    void findAllByKorisnikIdOrderByDatumDesc() {
        Recenzija rc1 = new Recenzija();
        rc1.setKorisnik(korisnik1);
        rc1.setHotel(hotel1);
        rc1.setOcjena(5);
        rc1.setTekst("Super");
        rc1.setDatum(LocalDate.of(2025, 10, 10));
        entityManager.persist(rc1);

        Recenzija rc2 = new Recenzija();
        rc2.setKorisnik(korisnik1);
        rc2.setHotel(hotel2);
        rc2.setOcjena(3);
        rc2.setTekst("Okej je sve");
        rc2.setDatum(LocalDate.of(2025, 11, 11));
        entityManager.persist(rc2);

        entityManager.flush();
        entityManager.clear();

        var rezultat = repository.findAllByKorisnikIdOrderByDatumDesc(korisnik1.getId());

        assertFalse(rezultat.isEmpty());
        assertEquals(2, rezultat.size());

        assertEquals(LocalDate.of(2025, 11, 11), rezultat.get(0).getDatum());
        assertEquals(LocalDate.of(2025, 10, 10), rezultat.get(1).getDatum());

        var hoteli = rezultat.stream().map(r -> r.getHotel().getNaziv()).toList();
        assertTrue(hoteli.containsAll(List.of("Hotel1", "Hotel2")));
    }

    @Test
    void findAllByKorisnikIdOrderByDatumDesc_notFound() {
        var rezultat = repository.findAllByKorisnikIdOrderByDatumDesc(99L);

        assertTrue(rezultat.isEmpty());
    }

    @Test
    void deleteByIdAndKorisnik_Id() {
        Recenzija rc1 = new Recenzija();
        rc1.setKorisnik(korisnik1);
        rc1.setHotel(hotel1);
        rc1.setOcjena(5);
        rc1.setTekst("Super");
        rc1.setDatum(LocalDate.now());
        entityManager.persist(rc1);

        entityManager.flush();
        entityManager.clear();

        var rezultat = repository.deleteByIdAndKorisnik_Id(rc1.getId(), korisnik1.getId());

        assertEquals(1, rezultat);
        assertFalse(repository.findById(rc1.getId()).isPresent());
    }

    @Test
    void deleteByIdAndKorisnikId_KriviKorisnikId() {
        Recenzija rc1 = new Recenzija();
        rc1.setKorisnik(korisnik1);
        rc1.setHotel(hotel1);
        rc1.setOcjena(5);
        rc1.setTekst("Super");
        rc1.setDatum(LocalDate.now());
        entityManager.persist(rc1);

        entityManager.flush();
        entityManager.clear();

        var rezultat = repository.deleteByIdAndKorisnik_Id(rc1.getId(), 99L);

        assertEquals(0, rezultat);
        assertTrue(repository.findById(rc1.getId()).isPresent());
    }

    @Test
    void deleteByIdAndKorisnikId_KrivaRecId() {
        var rezultat = repository.deleteByIdAndKorisnik_Id(99L, korisnik1.getId());

        assertEquals(0, rezultat);
    }
}