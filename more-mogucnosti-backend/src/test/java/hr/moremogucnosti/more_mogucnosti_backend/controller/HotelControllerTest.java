package hr.moremogucnosti.more_mogucnosti_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.moremogucnosti.more_mogucnosti_backend.dto.GradResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.HotelCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.HotelDetailsDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.HotelPreviewDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.HotelResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.exception.GlobalExceptionHandler;
import hr.moremogucnosti.more_mogucnosti_backend.exception.ResourceNotFoundException;
import hr.moremogucnosti.more_mogucnosti_backend.security.JwtFilter;
import hr.moremogucnosti.more_mogucnosti_backend.service.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = HotelController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class HotelControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    HotelService hotelService;

    @MockitoBean
    private JwtFilter jwtFilter;

    @Test
    void getHotel() throws Exception {
        GradResponseDto grad = new GradResponseDto(1L, "Zagreb");
        SlikaResponseDto glavna = new SlikaResponseDto(10L, "p1", true);
        HotelDetailsDto dto = new HotelDetailsDto(
                5L, "Hotel", grad, "Adresa 1",
                true, true, false, glavna, List.of(), true
        );

        when(hotelService.findDetailById(5L)).thenReturn(dto);

        mvc.perform(get("/api/v1/hotel/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.naziv", is("Hotel")))
                .andExpect(jsonPath("$.grad.imeGrad", is("Zagreb")))
                .andExpect(jsonPath("$.glavnaSlika.putanja", is("p1")));
    }

    @Test
    void getHotel_notFound() throws Exception {
        when(hotelService.findDetailById(99L))
                .thenThrow(new ResourceNotFoundException("Hotel sa ID-jem 99 ne postoji"));

        mvc.perform(get("/api/v1/hotel/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", containsString("ne postoji")))
                .andExpect(jsonPath("$.path", is("/api/v1/hotel/99")));
    }

    @Test
    void getAllHoteli() throws Exception {
        GradResponseDto grad = new GradResponseDto(1L, "Zagreb");
        SlikaResponseDto glavna = new SlikaResponseDto(10L, "p1", true);

        HotelPreviewDto h1 = new HotelPreviewDto(1L, "Hotel1", grad, "A1", true, true, true, glavna, true);
        HotelPreviewDto h2 = new HotelPreviewDto(2L, "Hotel2", grad, "A2", false, true, false, glavna, true);

        when(hotelService.findAll()).thenReturn(List.of(h1, h2));

        mvc.perform(get("/api/v1/hotel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].naziv", is("Hotel1")))
                .andExpect(jsonPath("$[1].naziv", is("Hotel2")));
    }

    @Test
    void createHotel() throws Exception {
        HotelCreateDto body = new HotelCreateDto(
                "Hotel", "Zagreb", "Adresa 1", true, true, false
        );

        GradResponseDto grad = new GradResponseDto(1L, "Zagreb");
        HotelResponseDto odgovor = new HotelResponseDto(
                10L, "Hotel", grad, "Adresa 1", true, true, false, false
        );

        when(hotelService.createHotel(any(HotelCreateDto.class))).thenReturn(odgovor);

        mvc.perform(post("/api/v1/hotel/admin/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.naziv", is("Hotel")))
                .andExpect(jsonPath("$.grad.imeGrad", is("Zagreb")));
    }

    @Test
    void createHotel_validationError_badRequest() throws Exception {
        HotelCreateDto body = new HotelCreateDto(
                "", "", "", null, null, null
        );

        mvc.perform(post("/api/v1/hotel/admin/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Neispravni podaci")))
                .andExpect(jsonPath("$.errors.naziv", notNullValue()))
                .andExpect(jsonPath("$.errors.gradNaziv", notNullValue()))
                .andExpect(jsonPath("$.errors.adresa", notNullValue()))
                .andExpect(jsonPath("$.errors.parking", notNullValue()))
                .andExpect(jsonPath("$.errors.wifi", notNullValue()))
                .andExpect(jsonPath("$.errors.bazen", notNullValue()));
    }
}
