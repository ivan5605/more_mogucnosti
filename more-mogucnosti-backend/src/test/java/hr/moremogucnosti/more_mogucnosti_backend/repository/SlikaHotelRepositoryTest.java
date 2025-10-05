package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.entity.Grad;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;
import hr.moremogucnosti.more_mogucnosti_backend.entity.HotelSlika;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SlikaHotelRepositoryTest {

    @Autowired
    SlikaHotelRepository repository;

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
    void ocistiGlavnu() {
        HotelSlika slika1 = setSlika("p1", true);
        entityManager.persist(slika1);
        HotelSlika slika2 = setSlika("p2", false);
        entityManager.persist(slika2);
        HotelSlika slika3 = setSlika("p3", false);
        entityManager.persist(slika3);

        entityManager.flush();
        entityManager.clear();

        var rezultat = repository.ocistiGlavnu(hotel.getId());

        var noviHotel = entityManager.find(Hotel.class, hotel.getId());

        assertEquals(1, rezultat);
        var glavne = noviHotel.getSlike().stream().map(HotelSlika::isGlavnaSlika).toList();
        assertFalse(glavne.contains(true));
    }

    @Test
    void ocistiGlavnu_bezGlavne() {
        HotelSlika slika1 = setSlika("p1", false);
        entityManager.persist(slika1);
        HotelSlika slika2 = setSlika("p2", false);
        entityManager.persist(slika2);
        HotelSlika slika3 = setSlika("p3", false);
        entityManager.persist(slika3);

        entityManager.flush();
        entityManager.clear();

        var rezultat = repository.ocistiGlavnu(hotel.getId());

        var noviHotel = entityManager.find(Hotel.class, hotel.getId());

        assertEquals(0, rezultat);
        var glavne = noviHotel.getSlike().stream().map(HotelSlika::isGlavnaSlika).toList();
        assertFalse(glavne.contains(true));
    }

    @Test
    void findFirstByHotelIdAndGlavnaSlikaFalseOrderByIdAsc() {
        HotelSlika slika1 = setSlika("p1", true);
        entityManager.persist(slika1);
        HotelSlika slika2 = setSlika("p2", false);
        entityManager.persist(slika2);
        HotelSlika slika3 = setSlika("p3", false);
        entityManager.persist(slika3);

        entityManager.flush();
        entityManager.clear();

        var rezultat = repository.findFirstByHotelIdAndGlavnaSlikaFalseOrderByIdAsc(hotel.getId());

        assertTrue(rezultat.isPresent());
        assertEquals(2, rezultat.get().getId());
        assertEquals("p2", rezultat.get().getPutanja());
    }

    @Test
    void findFirstByHotelIdAndGlavnaSlikaFalseOrderByIdAsc_notFound() {
        HotelSlika slika1 = setSlika("p1", true);
        entityManager.persist(slika1);

        entityManager.flush();
        entityManager.clear();

        var rezultat = repository.findFirstByHotelIdAndGlavnaSlikaFalseOrderByIdAsc(hotel.getId());

        assertTrue(rezultat.isEmpty());
    }

    private HotelSlika setSlika(String putanja, boolean glavna) {
        HotelSlika sl = new HotelSlika();
        sl.setPutanja(putanja);
        sl.setGlavnaSlika(glavna);
        sl.setHotel(hotel);
        return sl;
    }

}