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
import mx.edu.uteq.idgs12.microservio_division.dto.DivisionCreateDto;
import mx.edu.uteq.idgs12.microservio_division.dto.DivisionToViewListDto;
import mx.edu.uteq.idgs12.microservio_division.dto.DivisionUpdateDto;
import mx.edu.uteq.idgs12.microservio_division.service.DivisionService;




@RestController
@RequestMapping("/divisiones")
public class DivisionController {

    @Autowired
    private DivisionService divisionService;

    // Obtener todas las divisiones
    @GetMapping
    public ResponseEntity<List<DivisionToViewListDto>> getAllDivisiones() {
        List<DivisionToViewListDto> divisiones = divisionService.findAll();
        return ResponseEntity.ok(divisiones);
    }

    // Obtener todas las divisiones activas
    @GetMapping("/activas")
    public ResponseEntity<List<DivisionToViewListDto>> getDivisionesActivas() {
        List<DivisionToViewListDto> divisiones = divisionService.findAllActivas();
        return ResponseEntity.ok(divisiones);
    }

    // Buscar divisiones por nombre
    @GetMapping("/search")
    public ResponseEntity<List<DivisionToViewListDto>> searchDivisionesByNombre(@RequestParam String nombre) {
        List<DivisionToViewListDto> divisiones = divisionService.findByNombre(nombre);
        return ResponseEntity.ok(divisiones);
    }

    // Obtener divisiones con paginación
    @GetMapping("/paginated")
    public ResponseEntity<Page<DivisionToViewListDto>> getAllDivisionesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Page<DivisionToViewListDto> divisiones = divisionService.findAllPaginated(page, size, sortBy);
        return ResponseEntity.ok(divisiones);
    }

    // Obtener divisiones activas con paginación
    @GetMapping("/activas/paginated")
    public ResponseEntity<Page<DivisionToViewListDto>> getDivisionesActivasPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Page<DivisionToViewListDto> divisiones = divisionService.findAllActivasPaginated(page, size, sortBy);
        return ResponseEntity.ok(divisiones);
    }

    // Buscar divisiones por nombre con paginación
    @GetMapping("/search/paginated")
    public ResponseEntity<Page<DivisionToViewListDto>> searchDivisionesByNombrePaginated(
            @RequestParam String nombre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Page<DivisionToViewListDto> divisiones = divisionService.findByNombrePaginated(nombre, page, size, sortBy);
        return ResponseEntity.ok(divisiones);
    }

    // Obtener división por ID
    @GetMapping("/{id}")
    public ResponseEntity<DivisionToViewListDto> getDivisionById(@PathVariable Long id) {
        Optional<DivisionToViewListDto> division = divisionService.findById(id);
        
        if (division.isPresent()) {
            return ResponseEntity.ok(division.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear nueva división
    @PostMapping
    public ResponseEntity<DivisionToViewListDto> createDivision(@Valid @RequestBody DivisionCreateDto divisionDto) {
        DivisionToViewListDto nuevaDivision = divisionService.create(divisionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaDivision);
    }

    // Actualizar división existente
    @PutMapping("/{id}")
    public ResponseEntity<DivisionToViewListDto> updateDivision(
            @PathVariable Long id,
            @Valid @RequestBody DivisionUpdateDto divisionDto) {
        
        Optional<DivisionToViewListDto> divisionActualizada = divisionService.update(id, divisionDto);
        
        if (divisionActualizada.isPresent()) {
            return ResponseEntity.ok(divisionActualizada.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar división (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDivision(@PathVariable Long id) {
        boolean deleted = divisionService.delete(id);
        
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Activar/Desactivar división
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<DivisionToViewListDto> toggleDivisionStatus(@PathVariable Long id) {
        Optional<DivisionToViewListDto> division = divisionService.toggleStatus(id);
        
        if (division.isPresent()) {
            return ResponseEntity.ok(division.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
} 