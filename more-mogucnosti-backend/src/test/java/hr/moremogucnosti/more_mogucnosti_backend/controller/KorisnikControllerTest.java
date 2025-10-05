package hr.moremogucnosti.more_mogucnosti_backend.controller;

import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.exception.GlobalExceptionHandler;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.security.JwtFilter;
import hr.moremogucnosti.more_mogucnosti_backend.service.KorisnikService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = KorisnikController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class KorisnikControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private KorisnikService service;

    @MockitoBean
    private JwtFilter jwtFilter;

    @Test
    void getKorisnik() throws Exception {
        Long id = 1L;
        KorisnikViewDto dto = new KorisnikViewDto(1L, "Ana", "Anić", "ana@gmail.com");

        when(service.findById(id)).thenReturn(dto);

        mvc.perform(get("/api/v1/korisnik/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.ime").value("Ana"))
                .andExpect(jsonPath("$.prezime").value("Anić"))
                .andExpect(jsonPath("$.email").value("ana@gmail.com"));
    }

    @Test
    void getKorisnik_notFound() throws Exception {
        Long id = 99L;

        when(service.findById(id)).thenThrow(new ResourceNotFoundException("Korisnik sa ID-jem " + id + " ne postoji"));

        mvc.perform(get("/api/v1/korisnik/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Korisnik sa ID-jem 99 ne postoji"));
    }
}