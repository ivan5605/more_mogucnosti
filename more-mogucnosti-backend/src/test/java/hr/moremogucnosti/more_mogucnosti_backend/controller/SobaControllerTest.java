package hr.moremogucnosti.more_mogucnosti_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.HotelViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaUpdateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.exception.GlobalExceptionHandler;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.security.JwtFilter;
import hr.moremogucnosti.more_mogucnosti_backend.service.SobaService;
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
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SobaController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class SobaControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    SobaService sobaService;

    @MockitoBean
    JwtFilter jwtFilter;

    @Test
    void getSoba() throws Exception {
        SobaResponseDto dto = new SobaResponseDto(
                1L, 3, BigDecimal.valueOf(120), 101, true, true,
                new SlikaResponseDto(10L, "p1", true),
                List.of(new SlikaResponseDto(11L, "p2", false)),
                true
        );

        Mockito.when(sobaService.findById(1L)).thenReturn(dto);

        mvc.perform(get("/api/v1/soba/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.kapacitet", is(3)))
                .andExpect(jsonPath("$.brojSobe", is(101)))
                .andExpect(jsonPath("$.glavnaSlika.putanja", is("p1")));
    }

    @Test
    void getSoba_notFound() throws Exception {
        Mockito.when(sobaService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Soba sa ID-jem 99 ne postoji!"));

        mvc.perform(get("/api/v1/soba/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("ne postoji")))
                .andExpect(jsonPath("$.path", is("/api/v1/soba/99")));
    }

    @Test
    void getAllSobaHotel() throws Exception {
        SobaResponseDto s1 = new SobaResponseDto(
                1L, 2, BigDecimal.valueOf(80), 100, true, true,
                null, List.of(), true
        );
        SobaResponseDto s2 = new SobaResponseDto(
                2L, 4, BigDecimal.valueOf(150), 102, false, true,
                null, List.of(), true
        );

        Mockito.when(sobaService.findAllByIdHotel(5L)).thenReturn(List.of(s1, s2));

        mvc.perform(get("/api/v1/soba/hotel/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].brojSobe", is(100)))
                .andExpect(jsonPath("$[1].kapacitet", is(4)));
    }

    @Test
    void createSoba() throws Exception {
        Long hotelId = 10L;
        SobaCreateDto body = new SobaCreateDto(
                3, BigDecimal.valueOf(120), 201, true, false
        );

        SobaViewDto dto = new SobaViewDto(
                7L, BigDecimal.valueOf(120), 201, 3,
                new HotelViewDto(10L, "Hotel X"), true
        );

        Mockito.when(sobaService.createSoba(eq(hotelId), any()))
                .thenReturn(dto);

        mvc.perform(post("/api/v1/soba/admin/create/{id}", hotelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(7)))
                .andExpect(jsonPath("$.brojSobe", is(201)))
                .andExpect(jsonPath("$.kapacitet", is(3)));
    }

    @Test
    void updateSoba_validationError_badRequest() throws Exception {
        SobaUpdateDto body = new SobaUpdateDto(
                0, null, null, null
        );

        mvc.perform(put("/api/v1/soba/admin/update/{id}", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Neispravni podaci")))
                .andExpect(jsonPath("$.errors.kapacitet", notNullValue()))
                .andExpect(jsonPath("$.errors.cijenaNocenja", notNullValue()))
                .andExpect(jsonPath("$.errors.balkon", notNullValue()))
                .andExpect(jsonPath("$.errors.petFriendly", notNullValue()));
    }
}
