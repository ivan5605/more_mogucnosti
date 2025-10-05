package hr.moremogucnosti.more_mogucnosti_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikBasicDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.HotelViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.recenzija.*;
import hr.moremogucnosti.more_mogucnosti_backend.exception.GlobalExceptionHandler;
import hr.moremogucnosti.more_mogucnosti_backend.security.AppUserPrincipal;
import hr.moremogucnosti.more_mogucnosti_backend.security.JwtFilter;
import hr.moremogucnosti.more_mogucnosti_backend.service.RecenzijaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RecenzijaController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class RecenzijaControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean RecenzijaService recenzijaService;
    @MockitoBean JwtFilter jwtFilter;

    private Authentication auth(Long id, String email) {
        AppUserPrincipal p = new AppUserPrincipal(id, email, "pass", List.of());
        return new UsernamePasswordAuthenticationToken(p, p.getPassword(), p.getAuthorities());
    }

    @Test
    void getRecenzijeHotela() throws Exception {
        RecenzijaZaHotelDto r1 = new RecenzijaZaHotelDto(
                1L, new KorisnikBasicDto(2L, "Ivo", "Ivić"),
                5, "Super", LocalDate.now()
        );
        RecenzijaZaHotelDto r2 = new RecenzijaZaHotelDto(
                2L, new KorisnikBasicDto(3L, "Ana", "Anić"),
                4, "Dobro", LocalDate.now().minusDays(1)
        );

        Mockito.when(recenzijaService.findAllByHotelId(10L)).thenReturn(List.of(r1, r2));

        mvc.perform(get("/api/v1/recenzija/hotel/{id}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].ocjena", is(5)))
                .andExpect(jsonPath("$[1].korisnik.ime", is("Ana")));
    }

    @Test
    void getRecenzijeInfo() throws Exception {
        RecenzijaHotelStatusDto info = new RecenzijaHotelStatusDto(7L, 4.3);
        Mockito.when(recenzijaService.findRecenzijeStatus(10L)).thenReturn(info);

        mvc.perform(get("/api/v1/recenzija/hotel/info/{id}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brojRecenzija", is(7)))
                .andExpect(jsonPath("$.prosjekRecenzija", is(4.3)));
    }

    @Test
    void upsertRecenzija() throws Exception {
        Long hotelId = 5L;
        RecenzijaCreateDto body = new RecenzijaCreateDto(5, "Odlično");

        RecenzijaDetailsDto out = new RecenzijaDetailsDto(
                100L,
                new KorisnikViewDto(1L, "Ivo", "Ivić", "ivo@gmail.com"),
                new HotelViewDto(hotelId, "Hotel"),
                5,
                "Odlično",
                LocalDate.now()
        );

        Mockito.when(recenzijaService.editOrCreateRecenzija(eq(1L), eq(hotelId), eq(body))).thenReturn(out);

        mvc.perform(put("/api/v1/recenzija/moja/hotel/{id}", hotelId)
                        .with(authentication(auth(1L, "ivo@gmail.com")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.ocjena", is(5)))
                .andExpect(jsonPath("$.hotelViewDto.naziv", is("Hotel")));
    }

    @Test
    void getRecenzijePrijavljenogKorisnika() throws Exception {
        RecenzijaZaKorisnikDto r1 = new RecenzijaZaKorisnikDto(
                11L, new HotelViewDto(3L, "Hotel1"), 4, "Ok", LocalDate.now()
        );
        RecenzijaZaKorisnikDto r2 = new RecenzijaZaKorisnikDto(
                12L, new HotelViewDto(4L, "Hotel2"), 5, "Super", LocalDate.now().minusDays(2)
        );

        Mockito.when(recenzijaService.findAllWithHotelById(1L)).thenReturn(List.of(r1, r2));

        mvc.perform(get("/api/v1/recenzija/korisnik")
                        .with(authentication(auth(1L, "ivo@gmail.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].hotel.naziv", is("Hotel")))
                .andExpect(jsonPath("$[1].ocjena", is(5)));
    }

    @Test
    void deleteRecenzija() throws Exception {
        Mockito.doNothing().when(recenzijaService).deleteById(50L, 1L);

        mvc.perform(delete("/api/v1/recenzija/delete/{id}", 50L)
                        .with(authentication(auth(1L, "ivo@gmail.com"))))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Recenzija uspješno obrisana!")));

        Mockito.verify(recenzijaService).deleteById(50L, 1L);
    }

    @Test
    void upsertRecenzija_emptyBody_badRequest() throws Exception {
        mvc.perform(put("/api/v1/recenzija/moja/hotel/{id}", 5L)
                        .with(authentication(auth(1L, "ivo@gmail.com")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Request body je obavezan!")));
    }
}
