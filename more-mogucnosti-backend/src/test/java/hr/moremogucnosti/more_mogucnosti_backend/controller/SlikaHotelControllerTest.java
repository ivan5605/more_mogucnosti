package hr.moremogucnosti.more_mogucnosti_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaHotelResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.HotelViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.exception.GlobalExceptionHandler;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.security.JwtFilter;
import hr.moremogucnosti.more_mogucnosti_backend.service.SlikaHotelService;
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

@WebMvcTest(controllers = SlikaHotelController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class SlikaHotelControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean
    SlikaHotelService service;
    @MockitoBean JwtFilter jwtFilter;

    @Test
    void addSlikaHotel() throws Exception {
        Long hotelId = 7L;
        SlikaCreateDto body = new SlikaCreateDto("p1", true);

        SlikaHotelResponseDto out = new SlikaHotelResponseDto(
                15L, "p1", true, new HotelViewDto(hotelId, "Hotel")
        );

        Mockito.when(service.addSlikaHotel(eq(hotelId), any(SlikaCreateDto.class))).thenReturn(out);

        mvc.perform(post("/api/v1/slikaHotel/admin/create/{hotelId}", hotelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(15)))
                .andExpect(jsonPath("$.putanja", is("p1")))
                .andExpect(jsonPath("$.glavna", is(true)))
                .andExpect(jsonPath("$.hotel.id", is(7)));
    }

    @Test
    void addSlikaHotel_validation_badRequest() throws Exception {
        SlikaCreateDto body = new SlikaCreateDto("", null);

        mvc.perform(post("/api/v1/slikaHotel/admin/create/{hotelId}", 7L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Neispravni podaci")))
                .andExpect(jsonPath("$.errors.putanja", notNullValue()))
                .andExpect(jsonPath("$.errors.glavna", notNullValue()));
    }

    @Test
    void deleteSlika_noContent() throws Exception {
        Mockito.doNothing().when(service).deleteSlikaHotel(20L);

        mvc.perform(delete("/api/v1/slikaHotel/admin/delete/{id}", 20L))
                .andExpect(status().isNoContent());

        Mockito.verify(service).deleteSlikaHotel(20L);
    }

    @Test
    void setGlavna() throws Exception {
        SlikaResponseDto out = new SlikaResponseDto(30L, "p1", true);
        Mockito.when(service.setGlavna(30L)).thenReturn(out);

        mvc.perform(put("/api/v1/slikaHotel/admin/setGlavna/{id}", 30L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(30)))
                .andExpect(jsonPath("$.glavna", is(true)));
    }

    @Test
    void setGlavna_notFound() throws Exception {
        Mockito.when(service.setGlavna(99L))
                .thenThrow(new ResourceNotFoundException("Slika ne postoji."));

        mvc.perform(put("/api/v1/slikaHotel/admin/setGlavna/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("ne postoji")))
                .andExpect(jsonPath("$.path", is("/api/v1/slikaHotel/admin/setGlavna/99")));
    }
}
