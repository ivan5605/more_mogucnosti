package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikAdminDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest //upali samo JPA i H2
    //svaki test se izvrsi u transakciji i na kraju rollback (odbaci sve promjene)
class KorisnikRepositoryTest {

    @Autowired
    KorisnikRepository repository;

    @Autowired //dependency injection
    TestEntityManager testEntityManager; //za upravljanje podacima u testovima

    Uloga uloga;
    Grad grad;
    Hotel hotel;
    Soba soba;

    @BeforeEach
    void setUp() {
        uloga = new Uloga();
        uloga.setNazivUloga("USER");
        testEntityManager.persist(uloga);

        grad = new Grad();
        grad.setImeGrad("Zagreb");
        grad.setSlug("zagreb");
        testEntityManager.persist(grad);

        hotel = new Hotel();
        hotel.setNaziv("Hotel");
        hotel.setGrad(grad);
        hotel.setWifi(true);
        hotel.setParking(true);
        hotel.setBazen(true);
        hotel.setAdresa("Adresa");
        hotel.setAktivno(true);
        testEntityManager.persist(hotel);

        soba = new Soba();
        soba.setHotel(hotel);
        soba.setBrojSobe(101);
        soba.setBalkon(true);
        soba.setPetFriendly(true);
        soba.setKapacitet(3);
        soba.setAktivno(true);
        soba.setCijenaNocenja(java.math.BigDecimal.valueOf(79));
        testEntityManager.persist(soba);

        testEntityManager.flush();   // pošalji u H2
        testEntityManager.clear();
    }

    @Test
    void findByEmail_whenExists() {
        //arrange
        Korisnik k = setKorisnik("Ivo", "Ivić", "ivo@gmail.com");
        testEntityManager.persistAndFlush(k);

        //act
        testEntityManager.clear();
        Optional<Korisnik> rezultat = repository.findByEmail("ivo@gmail.com");

        //assert
        assertTrue(rezultat.isPresent());
        assertEquals("ivo@gmail.com", rezultat.get().getEmail());
        assertNotNull(rezultat.get().getId());
        assertEquals("USER", rezultat.get().getUloga().getNazivUloga());
    }

    @Test
    void findByEmail_whenNotExist() {
        //nema arrange ne postavljam nikakve podatke

        //act
        Optional<Korisnik> korisnik = repository.findByEmail("nema@email.com");

        //assert
        assertTrue(korisnik.isEmpty());
    }

    @Test
    void findAll_whenExists() {
        //arrange
        Korisnik k1 = setKorisnik("Ivo", "Ivić", "ivo@gmail.com");
        testEntityManager.persistAndFlush(k1);

        Korisnik k2 = setKorisnik("Pero", "Perić", "pero@gmail.com");
        testEntityManager.persistAndFlush(k2);

        Korisnik k3 = setKorisnik("Ana", "Anić", "ana@gmail.com");
        testEntityManager.persistAndFlush(k3);

        //act
        List<Korisnik> testLista = repository.findAll();

        //assert
        assertFalse(testLista.isEmpty());

        assertEquals(3, testLista.size());

        var emails = testLista.stream().map(Korisnik::getEmail).toList();
        assertTrue(emails.containsAll(List.of("ivo@gmail.com", "pero@gmail.com", "ana@gmail.com")));

        assertTrue(testLista.stream().allMatch(k -> k.getUloga().getNazivUloga().equals("USER")));
    }

    @Test
    void findAll_whenNoneExist() {
        List<Korisnik> rezultat = repository.findAll();

        assertTrue(rezultat.isEmpty());
    }

    @Test
    void existsByEmail_whenExists() {
        //arrange
        Korisnik k = setKorisnik("Ivo", "Ivić", "ivo@gmail.com");
        testEntityManager.persistAndFlush(k);

        //act
        testEntityManager.clear();
        boolean exist = repository.existsByEmail("ivo@gmail.com");

        //assert
        assertTrue(exist);
    }

    @Test
    void existsByEmail_whenNotExist() {
        boolean exist = repository.existsByEmail("nema@gmail.com");

        assertFalse(exist);
    }

    @Test
    void existsByEmailAndIdNot() {
        //arrange
        Korisnik k = setKorisnik("Ivo", "Ivić", "ivo@gmail.com");
        testEntityManager.persistAndFlush(k);

        //act
        testEntityManager.clear();
        boolean exist = repository.existsByEmailAndIdNot("ivo@gmail.com", 2L);

        assertTrue(exist);
    }

    @Test
    void findKorisnikWithCount() {
        //arrange
        Korisnik k1 = setKorisnik("Ivo", "Ivić", "ivo@gmail.com");
        testEntityManager.persistAndFlush(k1);

        Korisnik k2 = setKorisnik("Pero", "Perić", "pero@gmail.com");
        testEntityManager.persistAndFlush(k2);

        Recenzija rc1 = new Recenzija();
        rc1.setKorisnik(k1); rc1.setTekst("Super!"); rc1.setDatum(LocalDate.now()); rc1.setHotel(hotel); rc1.setOcjena(5);
        testEntityManager.persistAndFlush(rc1);
        Recenzija rc2 = new Recenzija();
        rc2.setKorisnik(k1); rc2.setTekst("Okej!"); rc2.setDatum(LocalDate.now()); rc2.setHotel(hotel); rc2.setOcjena(3);
        testEntityManager.persistAndFlush(rc2);
        Recenzija rc3 = new Recenzija();
        rc3.setKorisnik(k1); rc3.setTekst("Loše..."); rc3.setDatum(LocalDate.now()); rc3.setHotel(hotel); rc3.setOcjena(1);
        testEntityManager.persistAndFlush(rc3);

        Rezervacija rz1 = new Rezervacija();
        rz1.setKorisnik(k1); rz1.setDatumPocetak(LocalDate.now()); rz1.setDatumKraj(LocalDate.of(2025, 10, 11));
        rz1.setSoba(soba); rz1.setBrojOsoba(2);
        testEntityManager.persistAndFlush(rz1);
        Rezervacija rz2 = new Rezervacija();
        rz2.setKorisnik(k1); rz2.setDatumPocetak(LocalDate.now()); rz2.setDatumKraj(LocalDate.of(2025, 10, 9));
        rz2.setSoba(soba); rz2.setBrojOsoba(1);
        testEntityManager.persistAndFlush(rz2);

        Rezervacija rz3 = new Rezervacija();
        rz3.setKorisnik(k2); rz3.setDatumPocetak(LocalDate.now()); rz3.setDatumKraj(LocalDate.of(2025, 10, 11));
        rz3.setSoba(soba); rz3.setBrojOsoba(2);
        testEntityManager.persistAndFlush(rz3);
        Rezervacija rz4 = new Rezervacija();
        rz4.setKorisnik(k2); rz4.setDatumPocetak(LocalDate.now()); rz4.setDatumKraj(LocalDate.of(2025, 10, 9));
        rz4.setSoba(soba); rz4.setBrojOsoba(1);
        testEntityManager.persistAndFlush(rz4);

        //act
        testEntityManager.clear();
        List<KorisnikAdminDto> korisnici = repository.findKorisnikWithCount();

        //assert
        //var email = korisnici.stream().map(KorisnikAdminDto::email, k -> k).toList();
        assertFalse(korisnici.isEmpty());
        assertEquals(2, korisnici.size());
        assertEquals(3, korisnici.get(0).brojRecenzija());
        assertEquals(2, korisnici.get(1).brojRezervacija());
    }

    private Korisnik setKorisnik (String ime, String prezime, String email) {
        Korisnik k = new Korisnik();
        k.setIme(ime);
        k.setPrezime(prezime);
        k.setEmail(email);
        k.setLozinka("Lozinka123");
        k.setAktivan(true);
        k.setUloga(uloga);
        return k;
    }


}