package hr.moremogucnosti.more_mogucnosti_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.HotelViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikBasicDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaDatumDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaDetailsDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaZaKorisnikDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.exception.GlobalExceptionHandler;
import hr.moremogucnosti.more_mogucnosti_backend.security.AppUserPrincipal;
import hr.moremogucnosti.more_mogucnosti_backend.security.JwtFilter;
import hr.moremogucnosti.more_mogucnosti_backend.service.RezervacijaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RezervacijaController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class RezervacijaControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    RezervacijaService rezervacijaService;

    @MockitoBean
    JwtFilter jwtFilter;

    private AppUserPrincipal principal(Long id, String email) {
        return new AppUserPrincipal(id, email, "pass", java.util.List.of());
    }

    @Test
    void createRezervacija() throws Exception {
        RezervacijaCreateDto body = new RezervacijaCreateDto(
                10L, 2,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3)
        );

        KorisnikBasicDto korisnik = new KorisnikBasicDto(1L, "Ivo", "Ivić");
        SobaViewDto soba = new SobaViewDto(
                7L, BigDecimal.valueOf(120), 201, 3,
                new HotelViewDto(5L, "Hotel"), true
        );
        RezervacijaDetailsDto out = new RezervacijaDetailsDto(
                55L, korisnik, soba, 2,
                body.datumPocetak(), body.datumKraj()
        );

        Mockito.when(rezervacijaService.createRezervacija(eq(body), any(AppUserPrincipal.class)))
                .thenReturn(out);

        mvc.perform(post("/api/v1/rezervacija/create")
                        .with(user(principal(1L, "ivo@gmail.com")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(55)))
                .andExpect(jsonPath("$.korisnik.ime", is("Ivo")))
                .andExpect(jsonPath("$.soba.id", is(7)));
    }

    @Test
    void createRezervacija_emptyBody_badRequest() throws Exception {
        mvc.perform(post("/api/v1/rezervacija/create")
                        .with(user(principal(1L, "ivo@gmail.com")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Request body je obavezan!")));
    }

    @Test
    void getRezervacijeKorisnika() throws Exception {
        SobaViewDto soba = new SobaViewDto(
                7L, BigDecimal.valueOf(100), 101, 2,
                new HotelViewDto(3L, "Hotel"), true
        );
        RezervacijaZaKorisnikDto r1 = new RezervacijaZaKorisnikDto(
                1L, soba, 2,
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(5),
                3L, BigDecimal.valueOf(300)
        );
        RezervacijaZaKorisnikDto r2 = new RezervacijaZaKorisnikDto(
                2L, soba, 1,
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(12),
                2L, BigDecimal.valueOf(200)
        );

        Mockito.when(rezervacijaService.findAll(1L)).thenReturn(List.of(r1, r2));

        mvc.perform(get("/api/v1/rezervacija/korisnik")
                        .with(user(principal(1L, "ivo@gmail.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].brojNocenja", is(2)));
    }

    @Test
    void getZauzetiDatumi() throws Exception {
        List<RezervacijaDatumDto> datumi = List.of(
                new RezervacijaDatumDto(1L, LocalDate.now().plusDays(3), LocalDate.now().plusDays(6)),
                new RezervacijaDatumDto(2L, LocalDate.now().plusDays(8), LocalDate.now().plusDays(9))
        );

        Mockito.when(rezervacijaService.findAllZauzetiDatumi(7L)).thenReturn(datumi);

        mvc.perform(get("/api/v1/rezervacija/datumi/{id}", 7L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void deleteRezervacija_noContent() throws Exception {
        Mockito.doNothing().when(rezervacijaService).deleteRezervacija(1L, 33L);

        mvc.perform(delete("/api/v1/rezervacija/delete/{id}", 33L)
                        .with(user(principal(1L, "ivo@gmail.com"))))
                .andExpect(status().isNoContent());

        Mockito.verify(rezervacijaService).deleteRezervacija(1L, 33L);
    }
}
