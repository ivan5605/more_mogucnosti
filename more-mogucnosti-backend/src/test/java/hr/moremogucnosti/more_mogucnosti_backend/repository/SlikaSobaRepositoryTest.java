package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.entity.Grad;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Soba;
import hr.moremogucnosti.more_mogucnosti_backend.entity.SobaSlika;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class SlikaSobaRepositoryTest {

    @Autowired
    SlikaSobaRepository repository;

    @Autowired
    TestEntityManager entityManager;

    Grad grad;
    Hotel hotel;
    Soba soba;

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

        soba = new Soba();
        soba.setCijenaNocenja(BigDecimal.valueOf(100));
        soba.setBalkon(true);
        soba.setPetFriendly(true);
        soba.setKapacitet(3);
        soba.setBrojSobe(101);
        soba.setHotel(hotel);
        soba.setAktivno(true);
        entityManager.persist(soba);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void ocistiGlavnu() {
        SobaSlika slika1 = setSlika("p1", true);
        entityManager.persist(slika1);
        SobaSlika slika2 = setSlika("p2", false);
        entityManager.persist(slika2);
        SobaSlika slika3 = setSlika("p3", false);
        entityManager.persist(slika3);

        entityManager.flush();
        entityManager.clear();

        var rezultat = repository.ocistiGlavnu(soba.getId());

        var novaSoba = entityManager.find(Soba.class, soba.getId());

        assertEquals(1, rezultat);
        var glavne = novaSoba.getSlike().stream().map(SobaSlika::isGlavnaSlika).toList();
        assertFalse(glavne.contains(true));
    }

    @Test
    void ocistiGlavnu_bezGlavne() {
        SobaSlika slika1 = setSlika("p1", false);
        entityManager.persist(slika1);
        SobaSlika slika2 = setSlika("p2", false);
        entityManager.persist(slika2);
        SobaSlika slika3 = setSlika("p3", false);
        entityManager.persist(slika3);

        entityManager.flush();
        entityManager.clear();

        var rezultat = repository.ocistiGlavnu(soba.getId());

        var novaSoba = entityManager.find(Soba.class, soba.getId());

        assertEquals(0, rezultat);
        var glavne = novaSoba.getSlike().stream().map(SobaSlika::isGlavnaSlika).toList();
        assertFalse(glavne.contains(true));
    }

    @Test
    void findFirstBySobaIdAndGlavnaSlikaFalseOrderByIdAsc() {
        SobaSlika slika1 = setSlika("p1", true);
        entityManager.persist(slika1);
        SobaSlika slika2 = setSlika("p2", false);
        entityManager.persist(slika2);
        SobaSlika slika3 = setSlika("p3", false);
        entityManager.persist(slika3);

        entityManager.flush();
        entityManager.clear();

        var rezultat = repository.findFirstBySobaIdAndGlavnaSlikaFalseOrderByIdAsc(soba.getId());

        assertTrue(rezultat.isPresent());
        assertEquals(2, rezultat.get().getId());
        assertEquals("p2", rezultat.get().getPutanja());
    }

    @Test
    void findFirstBySobaIdAndGlavnaSlikaFalseOrderByIdAsc_notFound() {
        SobaSlika slika1 = setSlika("p1", true);
        entityManager.persist(slika1);

        entityManager.flush();
        entityManager.clear();

        var rezultat = repository.findFirstBySobaIdAndGlavnaSlikaFalseOrderByIdAsc(soba.getId());

        assertTrue(rezultat.isEmpty());
    }

    private SobaSlika setSlika(String putanja, boolean glavna) {
        SobaSlika sl = new SobaSlika();
        sl.setPutanja(putanja);
        sl.setGlavnaSlika(glavna);
        sl.setSoba(soba);
        return sl;
    }
}