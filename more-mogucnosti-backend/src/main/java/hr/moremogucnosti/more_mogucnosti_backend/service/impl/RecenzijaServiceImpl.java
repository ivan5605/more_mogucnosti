package hr.moremogucnosti.more_mogucnosti_backend.service.impl;

import hr.moremogucnosti.more_mogucnosti_backend.dto.recenzija.*;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Korisnik;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Recenzija;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.HotelMapper;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.KorisnikMapper;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.RecenzijaMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.RecenzijaRepository;
import hr.moremogucnosti.more_mogucnosti_backend.security.AppUserDetailsService;
import hr.moremogucnosti.more_mogucnosti_backend.service.HotelService;
import hr.moremogucnosti.more_mogucnosti_backend.service.KorisnikService;
import hr.moremogucnosti.more_mogucnosti_backend.service.RecenzijaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor //bolje staviti @RewquiredArgsConstructor? prouci, spring koristi taj za constructor injection
@Transactional(readOnly = true) //nema dirty checking-a
public class RecenzijaServiceImpl implements RecenzijaService {

    private final HotelService hotelService;
    private final RecenzijaMapper mapper;
    private final RecenzijaRepository repository;
    private final KorisnikService korisnikService;

    private final KorisnikMapper korisnikMapper;
    private final HotelMapper hotelMapper;

    private final AppUserDetailsService userDetailsService;

//    @Override
//    @Transactional //svi save() i select() u jednoj transakciji, ako negdi pukne - sve se poništava, lazy relacije rade, ILI SVE ILI NIŠTA nema pola pola, pregazi readOnly = true

    @Override
    public List<RecenzijaZaHotelDto> findAllByHotelId(Long idHotel) {
        List<RecenzijaZaHotelDto> recenzijeHotela = repository.findByHotelIdWithKorisnik(idHotel)
                .stream()
                .map(mapper::toZaHotelDto)
                .collect(Collectors.toList());

        return recenzijeHotela;
    }

    @Override
    @Transactional
    public RecenzijaDetailsDto editOrCreateRecenzija(Long userId, Long idHotel, RecenzijaCreateDto recenzijaCreateDto) {
        Korisnik korisnik = korisnikService.loadEntity(userId);
        Hotel hotel = hotelService.loadEntity(idHotel);

        Optional<Recenzija> rec = repository.findByKorisnikIdAndHotelId(korisnik.getId(), hotel.getId());

        Recenzija novaRec = rec.orElseGet(() -> { //uzmi postojecu ili napravi novu, ako rec ima vrijednost vrati tu vrijednost i ne izvrsava lambda blok
           Recenzija recenzija = new Recenzija();
           recenzija.setKorisnik(korisnik);
           recenzija.setHotel(hotel);
           return recenzija;
        });

        novaRec.setOcjena(recenzijaCreateDto.ocjena());
        novaRec.setDatum(LocalDate.now());
        novaRec.setTekst(recenzijaCreateDto.tekst());

        Recenzija saveRecenzija = repository.save(novaRec);
        return mapper.toDetailsDto(saveRecenzija);
    }

    @Override
    public RecenzijaHotelStatusDto findRecenzijeStatus(Long idHotel) {
        RecenzijaHotelStatusDto recenzijaHotelStatusDto = repository.recenzijeInfoByHotelId(idHotel);
        return recenzijaHotelStatusDto;
    }

    @Override
    public List<RecenzijaZaKorisnikDto> findAllWithHotelById(Long userId) {
        List<RecenzijaZaKorisnikDto> recenzije = repository.findWithHotelByKorisnikId(userId)
                .stream()
                .map(mapper::toZaKorisnikaDto)
                .collect(Collectors.toList());
        return recenzije;
    }

    @Override
    public List<RecenzijaZaKorisnikDto> findAllByKorisnikId(Long id) {
        List<RecenzijaZaKorisnikDto> recenzije = repository.findAllByKorisnikIdOrderByDatumDesc(id)
                .stream()
                .map(mapper::toZaKorisnikaDto)
                .toList();
        return recenzije;
    }

    @Override
    @Transactional
    public void deleteById(Long idRecenzija, Long userId) {
        int izbrisano = repository.deleteByIdAndKorisnik_Id(idRecenzija, userId);
        if (izbrisano == 0){
            throw new ResourceNotFoundException("Recenzija ne postoji.");
        }
    }
}
