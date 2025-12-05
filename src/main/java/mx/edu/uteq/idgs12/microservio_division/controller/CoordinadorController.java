package mx.edu.uteq.idgs12.microservio_division.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import mx.edu.uteq.idgs12.microservio_division.dto.CoordinadorCreateDto;
import mx.edu.uteq.idgs12.microservio_division.dto.CoordinadorUpdateDto;
import mx.edu.uteq.idgs12.microservio_division.dto.CoordinadorViewDto;
import mx.edu.uteq.idgs12.microservio_division.service.CoordinadorService;

@RestController
@RequestMapping("/coordinadores")
public class CoordinadorController {

    @Autowired
    private CoordinadorService coordinadorService;

    // Obtener todos los coordinadores
    @GetMapping
    public ResponseEntity<List<CoordinadorViewDto>> getAllCoordinadores() {
        List<CoordinadorViewDto> coordinadores = coordinadorService.findAll();
        return ResponseEntity.ok(coordinadores);
    }

    // Obtener coordinadores activos
    @GetMapping("/activos")
    public ResponseEntity<List<CoordinadorViewDto>> getCoordinadoresActivos() {
        List<CoordinadorViewDto> coordinadores = coordinadorService.findAllActivos();
        return ResponseEntity.ok(coordinadores);
    }

    // Obtener coordinadores por división
    @GetMapping("/division/{divisionId}")
    public ResponseEntity<List<CoordinadorViewDto>> getCoordinadoresByDivision(@PathVariable Long divisionId) {
        List<CoordinadorViewDto> coordinadores = coordinadorService.findByDivision(divisionId);
        return ResponseEntity.ok(coordinadores);
    }

    // Obtener coordinador por ID
    @GetMapping("/{id}")
    public ResponseEntity<CoordinadorViewDto> getCoordinadorById(@PathVariable Long id) {
        Optional<CoordinadorViewDto> coordinador = coordinadorService.findById(id);

        if (coordinador.isPresent()) {
            return ResponseEntity.ok(coordinador.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener coordinadores con paginación
    @GetMapping("/paginated")
    public ResponseEntity<Page<CoordinadorViewDto>> getAllCoordinadoresPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Page<CoordinadorViewDto> coordinadores = coordinadorService.findAllPaginated(page, size, sortBy);
        return ResponseEntity.ok(coordinadores);
    }

    // Obtener coordinadores activos con paginación
    @GetMapping("/activos/paginated")
    public ResponseEntity<Page<CoordinadorViewDto>> getCoordinadoresActivosPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Page<CoordinadorViewDto> coordinadores = coordinadorService.findAllActivosPaginated(page, size, sortBy);
        return ResponseEntity.ok(coordinadores);
    }

    // Obtener coordinadores por división con paginación
    @GetMapping("/division/{divisionId}/paginated")
    public ResponseEntity<Page<CoordinadorViewDto>> getCoordinadoresByDivisionPaginated(
            @PathVariable Long divisionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Page<CoordinadorViewDto> coordinadores = coordinadorService.findByDivisionPaginated(divisionId, page, size, sortBy);
        return ResponseEntity.ok(coordinadores);
    }

    // Crear nuevo coordinador
    @PostMapping
    public ResponseEntity<CoordinadorViewDto> createCoordinador(@Valid @RequestBody CoordinadorCreateDto coordinadorDto) {
        CoordinadorViewDto nuevoCoordinador = coordinadorService.create(coordinadorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCoordinador);
    }

    // Actualizar coordinador
    @PutMapping("/{id}")
    public ResponseEntity<CoordinadorViewDto> updateCoordinador(
            @PathVariable Long id,
            @Valid @RequestBody CoordinadorUpdateDto coordinadorDto) {

        Optional<CoordinadorViewDto> coordinadorActualizado = coordinadorService.update(id, coordinadorDto);

        if (coordinadorActualizado.isPresent()) {
            return ResponseEntity.ok(coordinadorActualizado.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar coordinador (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoordinador(@PathVariable Long id) {
        boolean deleted = coordinadorService.delete(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Activar/Desactivar coordinador
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<CoordinadorViewDto> toggleCoordinadorStatus(@PathVariable Long id) {
        Optional<CoordinadorViewDto> coordinador = coordinadorService.toggleStatus(id);

        if (coordinador.isPresent()) {
            return ResponseEntity.ok(coordinador.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
