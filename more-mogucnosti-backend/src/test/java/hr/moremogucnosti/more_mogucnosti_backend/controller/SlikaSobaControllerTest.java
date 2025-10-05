package hr.moremogucnosti.more_mogucnosti_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaSobaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaBasicDto;
import hr.moremogucnosti.more_mogucnosti_backend.exception.GlobalExceptionHandler;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.security.JwtFilter;
import hr.moremogucnosti.more_mogucnosti_backend.service.SlikaSobaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SlikaSobaController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class SlikaSobaControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    SlikaSobaService service;

    @MockitoBean
    JwtFilter jwtFilter;

    @Test
    void addSlika() throws Exception {
        Long sobaId = 10L;
        SlikaCreateDto body = new SlikaCreateDto("p1", true);

        SlikaSobaResponseDto out = new SlikaSobaResponseDto(
                5L, "p1", true, new SobaBasicDto(sobaId, 101)
        );

        Mockito.when(service.addSlikaSoba(eq(sobaId), any(SlikaCreateDto.class))).thenReturn(out);

        mvc.perform(post("/api/v1/slikaSoba/admin/create/{sobaId}", sobaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.putanja", is("p1")))
                .andExpect(jsonPath("$.glavna", is(true)))
                .andExpect(jsonPath("$.soba.id", is(10)));
    }

    @Test
    void addSlika_validation_badRequest() throws Exception {
        SlikaCreateDto body = new SlikaCreateDto("", null);

        mvc.perform(post("/api/v1/slikaSoba/admin/create/{sobaId}", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Neispravni podaci")))
                .andExpect(jsonPath("$.errors.putanja", notNullValue()))
                .andExpect(jsonPath("$.errors.glavna", notNullValue()));
    }

    @Test
    void deleteSlika_noContent() throws Exception {
        Mockito.doNothing().when(service).deleteSlikaSoba(7L);

        mvc.perform(delete("/api/v1/slikaSoba/admin/delete/{id}", 7L))
                .andExpect(status().isNoContent());

        Mockito.verify(service).deleteSlikaSoba(7L);
    }

    @Test
    void setGlavna_ok() throws Exception {
        SlikaResponseDto out = new SlikaResponseDto(15L, "p1", true);
        Mockito.when(service.setGlavna(15L)).thenReturn(out);

        mvc.perform(put("/api/v1/slikaSoba/admin/setGlavna/{id}", 15L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(15)))
                .andExpect(jsonPath("$.glavna", is(true)));
    }

    @Test
    void setGlavna_notFound() throws Exception {
        Mockito.when(service.setGlavna(99L))
                .thenThrow(new ResourceNotFoundException("Slika ne postoji."));

        mvc.perform(put("/api/v1/slikaSoba/admin/setGlavna/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("ne postoji")))
                .andExpect(jsonPath("$.path", is("/api/v1/slikaSoba/admin/setGlavna/99")));
    }
}
