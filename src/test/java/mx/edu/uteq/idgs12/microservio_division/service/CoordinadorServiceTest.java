package mx.edu.uteq.idgs12.microservio_division.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import mx.edu.uteq.idgs12.microservio_division.dto.CoordinadorCreateDto;
import mx.edu.uteq.idgs12.microservio_division.dto.CoordinadorUpdateDto;
import mx.edu.uteq.idgs12.microservio_division.dto.CoordinadorViewDto;
import mx.edu.uteq.idgs12.microservio_division.entity.CoordinadorEntity;
import mx.edu.uteq.idgs12.microservio_division.entity.Division;
import mx.edu.uteq.idgs12.microservio_division.repository.CoordinadorRepository;
import mx.edu.uteq.idgs12.microservio_division.repository.DivisionRepository;

@ExtendWith(MockitoExtension.class)
class CoordinadorServiceTest {

    @Mock
    private CoordinadorRepository coordinadorRepository;

    @Mock
    private DivisionRepository divisionRepository;

    @InjectMocks
    private CoordinadorService coordinadorService;

    private CoordinadorEntity coordinador;
    private Division division;
    private CoordinadorCreateDto coordinadorCreateDto;
    private CoordinadorUpdateDto coordinadorUpdateDto;

    @BeforeEach
    void setUp() {
        division = new Division();
        division.setId(1L);
        division.setNombre("División de Tecnologías");
        division.setActivo(true);

        coordinador = CoordinadorEntity.builder()
                .id(1L)
                .nombre("Juan")
                .apellido("Pérez")
                .correo("juan.perez@uteq.edu.mx")
                .telefono("4421234567")
                .divisionId(1L)
                .activo(true)
                .build();

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
    void testFindAll() {
        // Arrange
        when(coordinadorRepository.findAll()).thenReturn(Arrays.asList(coordinador));
        when(divisionRepository.findById(1L)).thenReturn(Optional.of(division));

        // Act
        List<CoordinadorViewDto> result = coordinadorService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getNombre());
        verify(coordinadorRepository, times(1)).findAll();
    }

    @Test
    void testFindAllActivos() {
        // Arrange
        when(coordinadorRepository.findByActivoTrue()).thenReturn(Arrays.asList(coordinador));
        when(divisionRepository.findById(1L)).thenReturn(Optional.of(division));

        // Act
        List<CoordinadorViewDto> result = coordinadorService.findAllActivos();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isActivo());
        verify(coordinadorRepository, times(1)).findByActivoTrue();
    }

    @Test
    void testFindByDivision() {
        // Arrange
        when(coordinadorRepository.findByDivisionId(1L)).thenReturn(Arrays.asList(coordinador));
        when(divisionRepository.findById(1L)).thenReturn(Optional.of(division));

        // Act
        List<CoordinadorViewDto> result = coordinadorService.findByDivision(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getDivisionId());
        verify(coordinadorRepository, times(1)).findByDivisionId(1L);
    }

    @Test
    void testFindById() {
        // Arrange
        when(coordinadorRepository.findById(1L)).thenReturn(Optional.of(coordinador));
        when(divisionRepository.findById(1L)).thenReturn(Optional.of(division));

        // Act
        Optional<CoordinadorViewDto> result = coordinadorService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Juan", result.get().getNombre());
        verify(coordinadorRepository, times(1)).findById(1L);
    }

    @Test
    void testCreate_Success() {
        // Arrange
        when(coordinadorRepository.existsByCorreoIgnoreCase(anyString())).thenReturn(false);
        when(divisionRepository.findById(1L)).thenReturn(Optional.of(division));
        when(coordinadorRepository.save(any(CoordinadorEntity.class))).thenReturn(coordinador);

        // Act
        CoordinadorViewDto result = coordinadorService.create(coordinadorCreateDto);

        // Assert
        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        verify(coordinadorRepository, times(1)).existsByCorreoIgnoreCase(anyString());
        verify(divisionRepository, times(2)).findById(1L); // Una para validar, otra para DTO
        verify(coordinadorRepository, times(1)).save(any(CoordinadorEntity.class));
    }

    @Test
    void testCreate_DuplicateEmail() {
        // Arrange
        when(coordinadorRepository.existsByCorreoIgnoreCase(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            coordinadorService.create(coordinadorCreateDto);
        });

        verify(coordinadorRepository, times(1)).existsByCorreoIgnoreCase(anyString());
        verify(coordinadorRepository, never()).save(any(CoordinadorEntity.class));
    }

    @Test
    void testCreate_DivisionNotFound() {
        // Arrange
        when(coordinadorRepository.existsByCorreoIgnoreCase(anyString())).thenReturn(false);
        when(divisionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            coordinadorService.create(coordinadorCreateDto);
        });

        verify(divisionRepository, times(1)).findById(1L);
        verify(coordinadorRepository, never()).save(any(CoordinadorEntity.class));
    }

    @Test
    void testUpdate_Success() {
        // Arrange
        when(coordinadorRepository.findById(1L)).thenReturn(Optional.of(coordinador));
        when(coordinadorRepository.existsByCorreoIgnoreCaseAndIdNot(anyString(), anyLong())).thenReturn(false);
        when(divisionRepository.findById(1L)).thenReturn(Optional.of(division));
        when(coordinadorRepository.save(any(CoordinadorEntity.class))).thenReturn(coordinador);

        // Act
        Optional<CoordinadorViewDto> result = coordinadorService.update(1L, coordinadorUpdateDto);

        // Assert
        assertTrue(result.isPresent());
        verify(coordinadorRepository, times(1)).findById(1L);
        verify(coordinadorRepository, times(1)).save(any(CoordinadorEntity.class));
    }

    @Test
    void testUpdate_NotFound() {
        // Arrange
        when(coordinadorRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<CoordinadorViewDto> result = coordinadorService.update(1L, coordinadorUpdateDto);

        // Assert
        assertFalse(result.isPresent());
        verify(coordinadorRepository, times(1)).findById(1L);
        verify(coordinadorRepository, never()).save(any(CoordinadorEntity.class));
    }

    @Test
    void testDelete_Success() {
        // Arrange
        when(coordinadorRepository.findById(1L)).thenReturn(Optional.of(coordinador));
        when(coordinadorRepository.save(any(CoordinadorEntity.class))).thenReturn(coordinador);

        // Act
        boolean result = coordinadorService.delete(1L);

        // Assert
        assertTrue(result);
        verify(coordinadorRepository, times(1)).findById(1L);
        verify(coordinadorRepository, times(1)).save(any(CoordinadorEntity.class));
    }

    @Test
    void testDelete_NotFound() {
        // Arrange
        when(coordinadorRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        boolean result = coordinadorService.delete(1L);

        // Assert
        assertFalse(result);
        verify(coordinadorRepository, times(1)).findById(1L);
        verify(coordinadorRepository, never()).save(any(CoordinadorEntity.class));
    }

    @Test
    void testToggleStatus_Success() {
        // Arrange
        when(coordinadorRepository.findById(1L)).thenReturn(Optional.of(coordinador));
        when(coordinadorRepository.save(any(CoordinadorEntity.class))).thenReturn(coordinador);
        when(divisionRepository.findById(1L)).thenReturn(Optional.of(division));

        // Act
        Optional<CoordinadorViewDto> result = coordinadorService.toggleStatus(1L);

        // Assert
        assertTrue(result.isPresent());
        verify(coordinadorRepository, times(1)).findById(1L);
        verify(coordinadorRepository, times(1)).save(any(CoordinadorEntity.class));
    }

    @Test
    void testFindAllPaginated() {
        // Arrange
        Page<CoordinadorEntity> page = new PageImpl<>(Arrays.asList(coordinador));
        when(coordinadorRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(divisionRepository.findById(1L)).thenReturn(Optional.of(division));

        // Act
        Page<CoordinadorViewDto> result = coordinadorService.findAllPaginated(0, 10, "id");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(coordinadorRepository, times(1)).findAll(any(Pageable.class));
    }
}
