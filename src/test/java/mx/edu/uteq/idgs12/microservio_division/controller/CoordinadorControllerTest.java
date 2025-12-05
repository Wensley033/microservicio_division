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
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import mx.edu.uteq.idgs12.microservio_division.dto.CoordinadorCreateDto;
import mx.edu.uteq.idgs12.microservio_division.dto.CoordinadorUpdateDto;
import mx.edu.uteq.idgs12.microservio_division.dto.CoordinadorViewDto;
import mx.edu.uteq.idgs12.microservio_division.service.CoordinadorService;

@WebMvcTest(CoordinadorController.class)
class CoordinadorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CoordinadorService coordinadorService;

    @Autowired
    private ObjectMapper objectMapper;

    private CoordinadorViewDto coordinadorDto;
    private CoordinadorCreateDto coordinadorCreateDto;
    private CoordinadorUpdateDto coordinadorUpdateDto;

    @BeforeEach
    void setUp() {
        coordinadorDto = new CoordinadorViewDto();
        coordinadorDto.setId(1L);
        coordinadorDto.setNombre("Juan");
        coordinadorDto.setApellido("Pérez");
        coordinadorDto.setCorreo("juan.perez@uteq.edu.mx");
        coordinadorDto.setTelefono("4421234567");
        coordinadorDto.setDivisionId(1L);
        coordinadorDto.setDivisionNombre("División de Tecnologías");
        coordinadorDto.setActivo(true);

        coordinadorCreateDto = new CoordinadorCreateDto();
        coordinadorCreateDto.setNombre("Juan");
        coordinadorCreateDto.setApellido("Pérez");
        coordinadorCreateDto.setCorreo("juan.perez@uteq.edu.mx");
        coordinadorCreateDto.setTelefono("4421234567");
        coordinadorCreateDto.setDivisionId(1L);

        coordinadorUpdateDto = new CoordinadorUpdateDto();
        coordinadorUpdateDto.setNombre("Juan Carlos");
        coordinadorUpdateDto.setApellido("Pérez López");
        coordinadorUpdateDto.setCorreo("juan.perez@uteq.edu.mx");
        coordinadorUpdateDto.setTelefono("4421234567");
        coordinadorUpdateDto.setDivisionId(1L);
        coordinadorUpdateDto.setActivo(true);
    }

    @Test
    void testGetAllCoordinadores() throws Exception {
        // Arrange
        when(coordinadorService.findAll()).thenReturn(Arrays.asList(coordinadorDto));

        // Act & Assert
        mockMvc.perform(get("/coordinadores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[0].apellido").value("Pérez"))
                .andExpect(jsonPath("$[0].activo").value(true));

        verify(coordinadorService, times(1)).findAll();
    }

    @Test
    void testGetCoordinadoresActivos() throws Exception {
        // Arrange
        when(coordinadorService.findAllActivos()).thenReturn(Arrays.asList(coordinadorDto));

        // Act & Assert
        mockMvc.perform(get("/coordinadores/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].activo").value(true));

        verify(coordinadorService, times(1)).findAllActivos();
    }

    @Test
    void testGetCoordinadoresByDivision() throws Exception {
        // Arrange
        when(coordinadorService.findByDivision(1L)).thenReturn(Arrays.asList(coordinadorDto));

        // Act & Assert
        mockMvc.perform(get("/coordinadores/division/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].divisionId").value(1));

        verify(coordinadorService, times(1)).findByDivision(1L);
    }

    @Test
    void testGetCoordinadorById_Found() throws Exception {
        // Arrange
        when(coordinadorService.findById(1L)).thenReturn(Optional.of(coordinadorDto));

        // Act & Assert
        mockMvc.perform(get("/coordinadores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.apellido").value("Pérez"));

        verify(coordinadorService, times(1)).findById(1L);
    }

    @Test
    void testGetCoordinadorById_NotFound() throws Exception {
        // Arrange
        when(coordinadorService.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/coordinadores/1"))
                .andExpect(status().isNotFound());

        verify(coordinadorService, times(1)).findById(1L);
    }

    @Test
    void testGetAllCoordinadoresPaginated() throws Exception {
        // Arrange
        Page<CoordinadorViewDto> page = new PageImpl<>(Arrays.asList(coordinadorDto));
        when(coordinadorService.findAllPaginated(0, 10, "id")).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/coordinadores/paginated")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nombre").value("Juan"));

        verify(coordinadorService, times(1)).findAllPaginated(0, 10, "id");
    }

    @Test
    void testCreateCoordinador() throws Exception {
        // Arrange
        when(coordinadorService.create(any(CoordinadorCreateDto.class))).thenReturn(coordinadorDto);

        // Act & Assert
        mockMvc.perform(post("/coordinadores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(coordinadorCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.apellido").value("Pérez"));

        verify(coordinadorService, times(1)).create(any(CoordinadorCreateDto.class));
    }

    @Test
    void testCreateCoordinador_InvalidData() throws Exception {
        // Arrange
        CoordinadorCreateDto invalidDto = new CoordinadorCreateDto();
        invalidDto.setNombre(""); // Nombre vacío (inválido)

        // Act & Assert
        mockMvc.perform(post("/coordinadores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verify(coordinadorService, never()).create(any(CoordinadorCreateDto.class));
    }

    @Test
    void testUpdateCoordinador_Success() throws Exception {
        // Arrange
        when(coordinadorService.update(eq(1L), any(CoordinadorUpdateDto.class)))
                .thenReturn(Optional.of(coordinadorDto));

        // Act & Assert
        mockMvc.perform(put("/coordinadores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(coordinadorUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));

        verify(coordinadorService, times(1)).update(eq(1L), any(CoordinadorUpdateDto.class));
    }

    @Test
    void testUpdateCoordinador_NotFound() throws Exception {
        // Arrange
        when(coordinadorService.update(eq(1L), any(CoordinadorUpdateDto.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/coordinadores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(coordinadorUpdateDto)))
                .andExpect(status().isNotFound());

        verify(coordinadorService, times(1)).update(eq(1L), any(CoordinadorUpdateDto.class));
    }

    @Test
    void testDeleteCoordinador_Success() throws Exception {
        // Arrange
        when(coordinadorService.delete(1L)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/coordinadores/1"))
                .andExpect(status().isNoContent());

        verify(coordinadorService, times(1)).delete(1L);
    }

    @Test
    void testDeleteCoordinador_NotFound() throws Exception {
        // Arrange
        when(coordinadorService.delete(1L)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/coordinadores/1"))
                .andExpect(status().isNotFound());

        verify(coordinadorService, times(1)).delete(1L);
    }

    @Test
    void testToggleCoordinadorStatus_Success() throws Exception {
        // Arrange
        when(coordinadorService.toggleStatus(1L)).thenReturn(Optional.of(coordinadorDto));

        // Act & Assert
        mockMvc.perform(patch("/coordinadores/1/toggle-status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));

        verify(coordinadorService, times(1)).toggleStatus(1L);
    }

    @Test
    void testToggleCoordinadorStatus_NotFound() throws Exception {
        // Arrange
        when(coordinadorService.toggleStatus(1L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(patch("/coordinadores/1/toggle-status"))
                .andExpect(status().isNotFound());

        verify(coordinadorService, times(1)).toggleStatus(1L);
    }
}
