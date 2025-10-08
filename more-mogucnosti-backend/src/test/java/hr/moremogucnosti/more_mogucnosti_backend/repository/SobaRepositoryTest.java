package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SobaRepositoryTest {

    @Autowired
    SobaRepository repository;

    @Autowired
    TestEntityManager entityManager;

    Grad grad;
    Hotel hotel;

    @BeforeEach
    void ucitaj() {
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

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findByHotelIdWithSlike() {
        Soba s1 = setSoba(100, 3, BigDecimal.valueOf(100));
        entityManager.persist(s1);
        Soba s2 = setSoba(101, 2, BigDecimal.valueOf(200));
        entityManager.persist(s2);
        Soba s3 = setSoba(102, 1, BigDecimal.valueOf(300));
        entityManager.persist(s3);

        dodajSlika(s1, "p1", true);
        dodajSlika(s1, "p2", false);
        dodajSlika(s2, "p3", true);

        entityManager.flush();
        entityManager.clear();

        var rezultat = repository.findByHotelIdWithSlike(hotel.getId());

        assertFalse(rezultat.isEmpty());

        var brojeviSoba = rezultat.stream().map(Soba::getBrojSobe).toList();
        assertTrue(brojeviSoba.containsAll(List.of(100, 101, 102)));

        var slike = rezultat.get(0).getSlike();
        var putanje = slike.stream().map(SobaSlika::getPutanja).toList();
        assertTrue(putanje.containsAll(List.of("p1", "p2")));
    }

    @Test
    void findByIdWithSlike() {
        // arrange
        Soba s1 = setSoba(100, 4, BigDecimal.valueOf(500));
        entityManager.persist(s1);
        dodajSlika(s1, "p1", true);
        dodajSlika(s1, "p2", false);
        entityManager.flush();
        entityManager.clear();

        // act
        var rezultat = repository.findByIdWithSlike(s1.getId())
                .orElseThrow(() -> new AssertionError("Soba nije pronadjena"));

        // assert
        assertEquals(100, rezultat.getBrojSobe());
        assertEquals(2, rezultat.getSlike().size());
        var putanje = rezultat.getSlike().stream().map(SobaSlika::getPutanja).toList();
        assertTrue(putanje.containsAll(List.of("pa", "pb")));
    }

    @Test
    void findByIdWithSlike_notFound() {
        var rezultat = repository.findByIdWithSlike(99L);
        assertTrue(rezultat.isEmpty());
    }

    @Test
    void findSobaByIdWithHotelAndSlike() {
        // arrange
        Soba s1 = setSoba(100, 2, BigDecimal.valueOf(200));
        entityManager.persist(s1);
        dodajSlika(s1, "p1", true);
        entityManager.flush();
        entityManager.clear();

        // act
        Soba r = repository.findSobaByIdWithHotelAndSlike(s1.getId())
                .orElseThrow(() -> new AssertionError("Soba nije pronadjena"));

        // assert
        assertEquals(100, r.getBrojSobe());
        assertNotNull(r.getHotel());
        assertNotNull(r.getHotel().getGrad());
        assertEquals("Zagreb", r.getHotel().getGrad().getImeGrad());
        assertEquals(1, r.getSlike().size());
        assertEquals("p1", r.getSlike().get(0).getPutanja());
    }

    @Test
    void findSobaByIdWithHotelAndSlike_notFound() {
        var rezultat = repository.findSobaByIdWithHotelAndSlike(99L);
        assertTrue(rezultat.isEmpty());
    }

    @Test
    void existsByHotelIdAndBrojSobe_true() {
        // arrange
        Soba s = setSoba(100, 2, BigDecimal.valueOf(101));
        entityManager.persist(s);
        entityManager.flush();
        entityManager.clear();

        // act + assert
        assertTrue(repository.existsByHotelIdAndBrojSobe(hotel.getId(), 100));
    }

    private Soba setSoba(int brojSobe, int kapacitet, BigDecimal cijenaNocenja) {
        Soba soba = new Soba();
        soba.setHotel(hotel);
        soba.setBalkon(true);
        soba.setPetFriendly(false);
        soba.setAktivno(true);
        soba.setBrojSobe(brojSobe);
        soba.setKapacitet(kapacitet);
        soba.setCijenaNocenja(cijenaNocenja);

        return soba;
    }

    private void dodajSlika(Soba soba, String putanja, boolean glavna) {
        SobaSlika sl = new SobaSlika();
        sl.setPutanja(putanja);
        sl.setGlavnaSlika(glavna);
        sl.setSoba(soba);
        entityManager.persist(sl);
    }
}