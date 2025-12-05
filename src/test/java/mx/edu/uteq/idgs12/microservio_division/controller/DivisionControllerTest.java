package mx.edu.uteq.idgs12.microservio_division.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import mx.edu.uteq.idgs12.microservio_division.dto.DivisionCreateDto;
import mx.edu.uteq.idgs12.microservio_division.dto.DivisionToViewListDto;
import mx.edu.uteq.idgs12.microservio_division.dto.DivisionUpdateDto;
import mx.edu.uteq.idgs12.microservio_division.service.DivisionService;

@WebMvcTest(DivisionController.class)
class DivisionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DivisionService divisionService;

    @Autowired
    private ObjectMapper objectMapper;

    private DivisionToViewListDto divisionDto;
    private DivisionCreateDto divisionCreateDto;
    private DivisionUpdateDto divisionUpdateDto;

    @BeforeEach
    void setUp() {
        divisionDto = new DivisionToViewListDto();
        divisionDto.setDivisionId(1L);
        divisionDto.setNombre("División de Tecnologías");
        divisionDto.setActivo(true);
        divisionDto.setNumeroProgramas(2);

        divisionCreateDto = new DivisionCreateDto();
        divisionCreateDto.setNombre("División de Tecnologías");

        divisionUpdateDto = new DivisionUpdateDto();
        divisionUpdateDto.setNombre("División Actualizada");
    }

    @Test
    void testGetAllDivisiones() throws Exception {
        // Arrange
        when(divisionService.findAll()).thenReturn(Arrays.asList(divisionDto));

        // Act & Assert
        mockMvc.perform(get("/divisiones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("División de Tecnologías"))
                .andExpect(jsonPath("$[0].activo").value(true));

        verify(divisionService, times(1)).findAll();
    }

    @Test
    void testGetDivisionesActivas() throws Exception {
        // Arrange
        when(divisionService.findAllActivas()).thenReturn(Arrays.asList(divisionDto));

        // Act & Assert
        mockMvc.perform(get("/divisiones/activas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].activo").value(true));

        verify(divisionService, times(1)).findAllActivas();
    }

    @Test
    void testGetDivisionById_Found() throws Exception {
        // Arrange
        when(divisionService.findById(1L)).thenReturn(Optional.of(divisionDto));

        // Act & Assert
        mockMvc.perform(get("/divisiones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("División de Tecnologías"));

        verify(divisionService, times(1)).findById(1L);
    }

    @Test
    void testGetDivisionById_NotFound() throws Exception {
        // Arrange
        when(divisionService.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/divisiones/1"))
                .andExpect(status().isNotFound());

        verify(divisionService, times(1)).findById(1L);
    }

    @Test
    void testSearchDivisionesByNombre() throws Exception {
        // Arrange
        when(divisionService.findByNombre("Tecnologías")).thenReturn(Arrays.asList(divisionDto));

        // Act & Assert
        mockMvc.perform(get("/divisiones/search")
                .param("nombre", "Tecnologías"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("División de Tecnologías"));

        verify(divisionService, times(1)).findByNombre("Tecnologías");
    }

    @Test
    void testGetAllDivisionesPaginated() throws Exception {
        // Arrange
        Page<DivisionToViewListDto> page = new PageImpl<>(Arrays.asList(divisionDto));
        when(divisionService.findAllPaginated(0, 10, "id")).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/divisiones/paginated")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nombre").value("División de Tecnologías"));

        verify(divisionService, times(1)).findAllPaginated(0, 10, "id");
    }

    @Test
    void testCreateDivision() throws Exception {
        // Arrange
        when(divisionService.create(any(DivisionCreateDto.class))).thenReturn(divisionDto);

        // Act & Assert
        mockMvc.perform(post("/divisiones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(divisionCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("División de Tecnologías"));

        verify(divisionService, times(1)).create(any(DivisionCreateDto.class));
    }

    @Test
    void testCreateDivision_InvalidData() throws Exception {
        // Arrange
        DivisionCreateDto invalidDto = new DivisionCreateDto();
        invalidDto.setNombre(""); // Nombre vacío (inválido)

        // Act & Assert
        mockMvc.perform(post("/divisiones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verify(divisionService, never()).create(any(DivisionCreateDto.class));
    }

    @Test
    void testUpdateDivision_Success() throws Exception {
        // Arrange
        when(divisionService.update(eq(1L), any(DivisionUpdateDto.class))).thenReturn(Optional.of(divisionDto));

        // Act & Assert
        mockMvc.perform(put("/divisiones/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(divisionUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("División de Tecnologías"));

        verify(divisionService, times(1)).update(eq(1L), any(DivisionUpdateDto.class));
    }

    @Test
    void testUpdateDivision_NotFound() throws Exception {
        // Arrange
        when(divisionService.update(eq(1L), any(DivisionUpdateDto.class))).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/divisiones/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(divisionUpdateDto)))
                .andExpect(status().isNotFound());

        verify(divisionService, times(1)).update(eq(1L), any(DivisionUpdateDto.class));
    }

    @Test
    void testDeleteDivision_Success() throws Exception {
        // Arrange
        when(divisionService.delete(1L)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/divisiones/1"))
                .andExpect(status().isNoContent());

        verify(divisionService, times(1)).delete(1L);
    }

    @Test
    void testDeleteDivision_NotFound() throws Exception {
        // Arrange
        when(divisionService.delete(1L)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/divisiones/1"))
                .andExpect(status().isNotFound());

        verify(divisionService, times(1)).delete(1L);
    }

    @Test
    void testToggleDivisionStatus_Success() throws Exception {
        // Arrange
        when(divisionService.toggleStatus(1L)).thenReturn(Optional.of(divisionDto));

        // Act & Assert
        mockMvc.perform(patch("/divisiones/1/toggle-status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("División de Tecnologías"));

        verify(divisionService, times(1)).toggleStatus(1L);
    }

    @Test
    void testToggleDivisionStatus_NotFound() throws Exception {
        // Arrange
        when(divisionService.toggleStatus(1L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(patch("/divisiones/1/toggle-status"))
                .andExpect(status().isNotFound());

        verify(divisionService, times(1)).toggleStatus(1L);
    }
}
