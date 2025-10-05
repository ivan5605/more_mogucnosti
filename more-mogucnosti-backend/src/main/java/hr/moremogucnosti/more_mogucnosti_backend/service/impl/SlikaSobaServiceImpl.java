package hr.moremogucnosti.more_mogucnosti_backend.service.impl;

import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaSobaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Soba;
import hr.moremogucnosti.more_mogucnosti_backend.entity.SobaSlika;
import hr.moremogucnosti.more_mogucnosti_backend.exception.DuplicateException;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.mapper.SlikaMapper;
import hr.moremogucnosti.more_mogucnosti_backend.repository.SlikaSobaRepository;
import hr.moremogucnosti.more_mogucnosti_backend.repository.SobaRepository;
import hr.moremogucnosti.more_mogucnosti_backend.service.SlikaSobaService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@AllArgsConstructor
@Transactional(readOnly = true)

public class SlikaSobaServiceImpl implements SlikaSobaService {

    private static final Logger logger = LoggerFactory.getLogger(SlikaSobaServiceImpl.class);

    private final SlikaSobaRepository repository;
    private final SlikaMapper mapper;
    private final SobaRepository sobaRepository;

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public SlikaSobaResponseDto addSlikaSoba(Long sobaId, SlikaCreateDto createDto) {
        Soba soba = sobaRepository.findById(sobaId)
                .orElseThrow(() -> new ResourceNotFoundException("Soba ne postoji."));

        if (createDto.glavna()){
            repository.ocistiGlavnu(sobaId);
        }

        SobaSlika slika = new SobaSlika();

        slika.setPutanja(createDto.putanja().trim());
        slika.setSoba(soba);
        slika.setGlavnaSlika(createDto.glavna());

        try {
            SobaSlika spremljena = repository.save(slika);
            repository.flush();
            return mapper.toSobaResponseDto(spremljena, soba);
        } catch (DataIntegrityViolationException e){
            throw new DuplicateException("Ova soba već ima glavnu sliku.");
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteSlikaSoba(Long idSlika) {
        SobaSlika slika = repository.findById(idSlika)
                .orElseThrow(() -> new ResourceNotFoundException("Slika ne postoji."));

        Long sobaId = slika.getSoba().getId();

        if (slika.isGlavnaSlika()) {
            Optional<SobaSlika> nova = repository.findFirstBySobaIdAndGlavnaSlikaFalseOrderByIdAsc(sobaId);

            if (nova.isPresent()) {
                repository.ocistiGlavnu(sobaId);
                SobaSlika s = nova.get();
                s.setGlavnaSlika(true);
                repository.saveAndFlush(s);
            }
        }

        repository.delete(slika);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public SlikaResponseDto setGlavna(Long idSlika) {
        SobaSlika slika = repository.findById(idSlika)
                .orElseThrow(() -> new ResourceNotFoundException("Slika ne postoji."));

        Soba soba = slika.getSoba();
        Long sobaId = soba.getId();
        System.out.println("sID" + sobaId);

        if (slika.isGlavnaSlika()) {
            return mapper.toResponseDto(slika);
        }

        repository.ocistiGlavnu(sobaId);

        try {
            slika.setGlavnaSlika(true);
            repository.save(slika);
            repository.flush();
            logger.info("Uspješno postavljena glavna slika id={} za sobu id={}", idSlika, sobaId);
        } catch (DataIntegrityViolationException e){
            throw new DuplicateException("Ova soba već ima glavnu sliku");
        } catch (Exception ex) {
            logger.error("Neočekivana greška u setGlavna za slikaId={}", idSlika, ex);
            throw ex;
        }

        SlikaResponseDto dto;
        try {
            dto = mapper.toResponseDto(slika);
            logger.info("mapper upsjesan za slikaId={}", idSlika);
        } catch (Exception ex) {
            logger.error("Mapper bacio izuzetak nakon saveAndFlush — ovo će uzrokovati rollback: ", ex);
            // možeš odlučiti što napraviti — ovdje ponovno bacamo da vidiš stacktrace
            throw ex;
        }

        return dto;
    }
}
