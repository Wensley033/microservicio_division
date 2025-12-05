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

import mx.edu.uteq.idgs12.microservio_division.dto.DivisionCreateDto;
import mx.edu.uteq.idgs12.microservio_division.dto.DivisionToViewListDto;
import mx.edu.uteq.idgs12.microservio_division.dto.DivisionUpdateDto;
import mx.edu.uteq.idgs12.microservio_division.entity.Division;
import mx.edu.uteq.idgs12.microservio_division.entity.ProgramaEducativa;
import mx.edu.uteq.idgs12.microservio_division.repository.DivisionRepository;

@ExtendWith(MockitoExtension.class)
class DivisionServiceTest {

    @Mock
    private DivisionRepository divisionRepository;

    @InjectMocks
    private DivisionService divisionService;

    private Division division;
    private DivisionCreateDto divisionCreateDto;
    private DivisionUpdateDto divisionUpdateDto;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        division = new Division();
        division.setId(1L);
        division.setNombre("División de Tecnologías");
        division.setActivo(true);

        ProgramaEducativa programa = new ProgramaEducativa();
        programa.setId(1L);
        programa.setPrograma("Ingeniería en Software");
        programa.setActivo(true);
        division.setProgramaEducativas(Arrays.asList(programa));

        divisionCreateDto = new DivisionCreateDto();
        divisionCreateDto.setNombre("División de Tecnologías");

        divisionUpdateDto = new DivisionUpdateDto();
        divisionUpdateDto.setNombre("División Actualizada");
    }

    @Test
    void testFindAll() {
        // Arrange
        when(divisionRepository.findAll()).thenReturn(Arrays.asList(division));

        // Act
        List<DivisionToViewListDto> result = divisionService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("División de Tecnologías", result.get(0).getNombre());
        verify(divisionRepository, times(1)).findAll();
    }

    @Test
    void testFindAllActivas() {
        // Arrange
        when(divisionRepository.findByActivoTrue()).thenReturn(Arrays.asList(division));

        // Act
        List<DivisionToViewListDto> result = divisionService.findAllActivas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isActivo());
        verify(divisionRepository, times(1)).findByActivoTrue();
    }

    @Test
    void testFindById() {
        // Arrange
        when(divisionRepository.findById(1L)).thenReturn(Optional.of(division));

        // Act
        Optional<DivisionToViewListDto> result = divisionService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("División de Tecnologías", result.get().getNombre());
        verify(divisionRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByNombre() {
        // Arrange
        when(divisionRepository.findByNombreContainingIgnoreCase("Tecnologías"))
                .thenReturn(Arrays.asList(division));

        // Act
        List<DivisionToViewListDto> result = divisionService.findByNombre("Tecnologías");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(divisionRepository, times(1)).findByNombreContainingIgnoreCase("Tecnologías");
    }

    @Test
    void testCreate_Success() {
        // Arrange
        when(divisionRepository.existsByNombreIgnoreCase(anyString())).thenReturn(false);
        when(divisionRepository.save(any(Division.class))).thenReturn(division);

        // Act
        DivisionToViewListDto result = divisionService.create(divisionCreateDto);

        // Assert
        assertNotNull(result);
        assertEquals("División de Tecnologías", result.getNombre());
        verify(divisionRepository, times(1)).existsByNombreIgnoreCase(anyString());
        verify(divisionRepository, times(1)).save(any(Division.class));
    }

    @Test
    void testCreate_DuplicateName() {
        // Arrange
        when(divisionRepository.existsByNombreIgnoreCase(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            divisionService.create(divisionCreateDto);
        });

        verify(divisionRepository, times(1)).existsByNombreIgnoreCase(anyString());
        verify(divisionRepository, never()).save(any(Division.class));
    }

    @Test
    void testUpdate_Success() {
        // Arrange
        when(divisionRepository.findById(1L)).thenReturn(Optional.of(division));
        when(divisionRepository.existsByNombreIgnoreCaseAndIdNot(anyString(), anyLong())).thenReturn(false);
        when(divisionRepository.save(any(Division.class))).thenReturn(division);

        // Act
        Optional<DivisionToViewListDto> result = divisionService.update(1L, divisionUpdateDto);

        // Assert
        assertTrue(result.isPresent());
        verify(divisionRepository, times(1)).findById(1L);
        verify(divisionRepository, times(1)).save(any(Division.class));
    }

    @Test
    void testUpdate_NotFound() {
        // Arrange
        when(divisionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<DivisionToViewListDto> result = divisionService.update(1L, divisionUpdateDto);

        // Assert
        assertFalse(result.isPresent());
        verify(divisionRepository, times(1)).findById(1L);
        verify(divisionRepository, never()).save(any(Division.class));
    }

    @Test
    void testDelete_Success() {
        // Arrange
        when(divisionRepository.findById(1L)).thenReturn(Optional.of(division));
        when(divisionRepository.save(any(Division.class))).thenReturn(division);

        // Act
        boolean result = divisionService.delete(1L);

        // Assert
        assertTrue(result);
        verify(divisionRepository, times(1)).findById(1L);
        verify(divisionRepository, times(1)).save(any(Division.class));
    }

    @Test
    void testDelete_NotFound() {
        // Arrange
        when(divisionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        boolean result = divisionService.delete(1L);

        // Assert
        assertFalse(result);
        verify(divisionRepository, times(1)).findById(1L);
        verify(divisionRepository, never()).save(any(Division.class));
    }

    @Test
    void testToggleStatus_Success() {
        // Arrange
        when(divisionRepository.findById(1L)).thenReturn(Optional.of(division));
        when(divisionRepository.save(any(Division.class))).thenReturn(division);

        // Act
        Optional<DivisionToViewListDto> result = divisionService.toggleStatus(1L);

        // Assert
        assertTrue(result.isPresent());
        verify(divisionRepository, times(1)).findById(1L);
        verify(divisionRepository, times(1)).save(any(Division.class));
    }

    @Test
    void testFindAllPaginated() {
        // Arrange
        Page<Division> page = new PageImpl<>(Arrays.asList(division));
        when(divisionRepository.findAll(any(Pageable.class))).thenReturn(page);

        // Act
        Page<DivisionToViewListDto> result = divisionService.findAllPaginated(0, 10, "id");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(divisionRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testFindAllActivasPaginated() {
        // Arrange
        Page<Division> page = new PageImpl<>(Arrays.asList(division));
        when(divisionRepository.findByActivoTrue(any(Pageable.class))).thenReturn(page);

        // Act
        Page<DivisionToViewListDto> result = divisionService.findAllActivasPaginated(0, 10, "id");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(divisionRepository, times(1)).findByActivoTrue(any(Pageable.class));
    }
}
