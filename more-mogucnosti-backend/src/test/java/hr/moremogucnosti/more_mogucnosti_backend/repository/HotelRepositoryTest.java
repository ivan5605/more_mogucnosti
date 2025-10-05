package hr.moremogucnosti.more_mogucnosti_backend.repository;

import hr.moremogucnosti.more_mogucnosti_backend.entity.Grad;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;
import hr.moremogucnosti.more_mogucnosti_backend.entity.HotelSlika;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class HotelRepositoryTest {

    @Autowired
    HotelRepository repository;

    @Autowired
    TestEntityManager entityManager;

    Grad grad;
    Hotel hotel1;
    Hotel hotel2;
    Hotel hotel3;

    @BeforeEach
    void setUp() {
        grad = new Grad();
        grad.setImeGrad("Zagreb");
        grad.setSlug("zagreb");
        entityManager.persist(grad);

        hotel1 = new Hotel();
        hotel1.setNaziv("Hotel1");
        hotel1.setGrad(grad);
        hotel1.setWifi(true);
        hotel1.setParking(true);
        hotel1.setBazen(true);
        hotel1.setAdresa("Adresa1");
        hotel1.setAktivno(true);
        hotel1.setSlike(new ArrayList<>());

        HotelSlika s1 = new HotelSlika();
        s1.setPutanja("p1");
        s1.setGlavnaSlika(true);
        s1.setHotel(hotel1);
        hotel1.getSlike().add(s1);

        HotelSlika s2 = new HotelSlika();
        s2.setPutanja("p2");
        s2.setGlavnaSlika(false);
        s2.setHotel(hotel1);
        hotel1.getSlike().add(s2);

        HotelSlika s3 = new HotelSlika();
        s3.setPutanja("p3");
        s3.setGlavnaSlika(false);
        s3.setHotel(hotel1);
        hotel1.getSlike().add(s3);

        entityManager.persist(hotel1);

        hotel2 = new Hotel();
        hotel2.setNaziv("Hotel2");
        hotel2.setGrad(grad);
        hotel2.setWifi(true);
        hotel2.setParking(true);
        hotel2.setBazen(true);
        hotel2.setAdresa("Adresa2");
        hotel2.setAktivno(true);
        entityManager.persist(hotel2);

        hotel3 = new Hotel();
        hotel3.setNaziv("Hotel 3");
        hotel3.setGrad(grad);
        hotel3.setWifi(false);
        hotel3.setParking(false);
        hotel3.setBazen(false);
        hotel3.setAdresa("Adresa 3");
        hotel3.setAktivno(true);
        entityManager.persist(hotel3);
    }

    @Test
    void findByHotelIdWithSlike() {
        entityManager.flush();
        entityManager.clear();

        Hotel rezultat = repository.findByHotelIdWithSlike(hotel1.getId())
                .orElseThrow(() -> new AssertionError("Hotel nije pronadjen"));

        var slike = rezultat.getSlike();

        assertEquals(3, slike.size());

        assertTrue(slike.stream().anyMatch(HotelSlika::isGlavnaSlika));

        var putanje = slike.stream().map(HotelSlika::getPutanja).toList();
        assertTrue(putanje.containsAll(List.of("p1", "p2", "p3")));

        assertTrue(slike.stream().allMatch(s -> s.getHotel().getId().equals(rezultat.getId())));
    }

    @Test
    void findByHotelIdWithSlike_notFound() {
        entityManager.flush();
        entityManager.clear();

        Optional<Hotel> rezultat = repository.findByHotelIdWithSlike(99L);

        assertTrue(rezultat.isEmpty(), "Ocekujem prazan hotel");
    }

    @Test
    void findAllWithSlike() {
        entityManager.flush();
        entityManager.clear();

        List<Hotel> hoteli = repository.findAllWithSlike();
        assertEquals(3, hoteli.size());

        Hotel h1 = hoteli.stream().filter(h -> h.getId().equals(hotel1.getId()))
                .findFirst().orElseThrow(() -> new AssertionError("hotel1 nije u listi"));
        assertEquals(3, h1.getSlike().size());
        assertNotNull(h1.getGrad());

        Hotel h2 = hoteli.stream().filter(h -> h.getId().equals(hotel2.getId()))
                .findFirst().orElseThrow(() -> new AssertionError("hotel2 nije u listi"));
        assertNotNull(h2.getSlike());
        assertEquals(0, h2.getSlike().size());
        assertNotNull(h2.getGrad());

        Hotel h3 = hoteli.stream().filter(h -> h.getId().equals(hotel3.getId()))
                .findFirst().orElseThrow(() -> new AssertionError("hotel3 nije u listi"));
        assertNotNull(h3.getSlike());
        assertEquals(0, h3.getSlike().size());
        assertNotNull(h3.getGrad());
    }

    @Test
    void findRandomHotelIds() {
        entityManager.flush();
        entityManager.clear();

        var ids = repository.findRandomHotelIds(PageRequest.of(0, 3));

        assertEquals(3, ids.size());

        List<Long> expected = List.of(hotel1.getId(), hotel2.getId(), hotel3.getId());
        assertTrue(expected.containsAll(ids));
    }

    @Test
    void find3RandomHotelsWithSlike() {
        entityManager.flush();
        entityManager.clear();

        var ids = repository.findRandomHotelIds(PageRequest.of(0, 3));
        assertEquals(3, ids.size());

        var hoteli = repository.find3RandomHotelsWithSlike(ids);

        assertEquals(ids.size(), hoteli.size());

        for (Hotel h : hoteli) {
            assertNotNull(h.getGrad());
            if (h.getId().equals(hotel1.getId())) {
                assertEquals(3, h.getSlike().size());
            }
        }
    }
}
