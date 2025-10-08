package hr.moremogucnosti.more_mogucnosti_backend.service.impl;

import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.*;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Soba;
import hr.moremogucnosti.more_mogucnosti_backend.exception.DuplicateException;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.SobaMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.HotelRepository;
import hr.moremogucnosti.more_mogucnosti_backend.repository.RezervacijaRepository;
import hr.moremogucnosti.more_mogucnosti_backend.repository.SobaRepository;
import hr.moremogucnosti.more_mogucnosti_backend.service.SobaService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class SobaServiceImpl implements SobaService {

    private final SobaRepository sobaRepository;
    private final SobaMapper sobaMapper;
    private final HotelRepository hotelRepository;
    private final RezervacijaRepository rezervacijaRepository;

    @Override
    public SobaResponseDto findById(Long idSoba) {
        Soba soba = sobaRepository.findByIdWithSlike(idSoba)
                .orElseThrow(() -> new ResourceNotFoundException("Soba sa ID-jem " + idSoba + " ne postoji!"));
        return sobaMapper.toResponseDto(soba);
    }

    @Override
    public List<SobaResponseDto> findAllByIdHotel(Long hotelId) {
        //hotelService.getDto(hotelId); provjera jel ima hotela ima bolji način?
        List<Soba> sobeHotela = sobaRepository.findByHotelIdWithSlike(hotelId);
        if (sobeHotela.isEmpty()){
            throw new ResourceNotFoundException("Hotel sa ID-jem " + hotelId + " nema sobe za rezervaciju!");
        }

        List<SobaResponseDto> sobeHotelaDto = sobeHotela.stream().
                map(sobaMapper::toResponseDto).collect(Collectors.toList());
        return sobeHotelaDto;
    }

    @Override
    public SobaDetailsDto findDetailsById(Long idSoba) {
        Soba soba = sobaRepository.findSobaByIdWithHotelAndSlike(idSoba)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel sa ID-jem " +  idSoba + " ne postoji!"));
        return sobaMapper.toDetailsDto(soba);
    }

    @Override
    public Soba loadEntity(Long idSoba) {
        Soba soba = sobaRepository.findById(idSoba)
                .orElseThrow(() -> new ResourceNotFoundException("Soba sa ID-jem " + idSoba + " ne postoji!"));
        return soba;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public SobaViewDto createSoba(Long idHotel, SobaCreateDto sobaDto) {
        Hotel hotel = hotelRepository.findById(idHotel)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel ne postoji."));

        boolean duplikat = sobaRepository.existsByHotelIdAndBrojSobe(hotel.getId(), sobaDto.brojSobe());

        if (duplikat){
            throw new DuplicateException("Soba broj " + sobaDto.brojSobe() + " već postoji u ovom hotelu!");
        }

        Soba soba = new Soba();
        soba.setBrojSobe(sobaDto.brojSobe());
        soba.setHotel(hotel);
        soba.setCijenaNocenja(sobaDto.cijenaNocenja());
        soba.setBalkon(sobaDto.balkon());
        soba.setPetFriendly(sobaDto.petFriendly());
        soba.setKapacitet(sobaDto.kapacitet());

        Soba saved = sobaRepository.save(soba);

        return sobaMapper.toViewDto(saved);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void softDeleteSoba(Long idSoba) {
        Soba soba = sobaRepository.findById(idSoba)
                .orElseThrow(() -> new ResourceNotFoundException("Soba ne postoji."));

        if (rezervacijaRepository.existsBySobaIdAndDatumPocetakAfter(idSoba, LocalDate.now())) {
            throw new DuplicateException("Prvo otkažite nadolazeće rezervacije, pa izbrišite sobu!");
        }

        soba.setAktivno(false);
        sobaRepository.save(soba);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public SobaResponseDto updateSoba(Long idSoba, SobaUpdateDto sobaDto) {
        Soba soba = sobaRepository.findById(idSoba)
                .orElseThrow(() -> new ResourceNotFoundException("Soba ne postoji."));

        soba.setCijenaNocenja(sobaDto.cijenaNocenja());
        soba.setBalkon(sobaDto.balkon());
        soba.setPetFriendly(sobaDto.petFriendly());
        soba.setKapacitet(sobaDto.kapacitet());

        Soba saved = sobaRepository.save(soba);

        return sobaMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void aktivirajSoba(Long idSoba) {
        Soba soba = sobaRepository.findById(idSoba)
                .orElseThrow(() -> new ResourceNotFoundException("Soba ne postoji."));

        soba.setAktivno(true);
        sobaRepository.save(soba);
    }
}
