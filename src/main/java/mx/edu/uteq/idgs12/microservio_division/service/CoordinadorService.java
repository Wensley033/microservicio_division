package mx.edu.uteq.idgs12.microservio_division.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.edu.uteq.idgs12.microservio_division.dto.CoordinadorCreateDto;
import mx.edu.uteq.idgs12.microservio_division.dto.CoordinadorUpdateDto;
import mx.edu.uteq.idgs12.microservio_division.dto.CoordinadorViewDto;
import mx.edu.uteq.idgs12.microservio_division.entity.CoordinadorEntity;
import mx.edu.uteq.idgs12.microservio_division.entity.Division;
import mx.edu.uteq.idgs12.microservio_division.repository.CoordinadorRepository;
import mx.edu.uteq.idgs12.microservio_division.repository.DivisionRepository;

@Service
public class CoordinadorService {

    @Autowired
    private CoordinadorRepository coordinadorRepository;

    @Autowired
    private DivisionRepository divisionRepository;

    // Obtener todos los coordinadores
    public List<CoordinadorViewDto> findAll() {
        List<CoordinadorEntity> coordinadores = coordinadorRepository.findAll();
        return coordinadores.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Obtener coordinadores activos
    public List<CoordinadorViewDto> findAllActivos() {
        List<CoordinadorEntity> coordinadores = coordinadorRepository.findByActivoTrue();
        return coordinadores.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Obtener coordinadores por división
    public List<CoordinadorViewDto> findByDivision(Long divisionId) {
        List<CoordinadorEntity> coordinadores = coordinadorRepository.findByDivisionId(divisionId);
        return coordinadores.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Obtener coordinador por ID
    public Optional<CoordinadorViewDto> findById(Long id) {
        Optional<CoordinadorEntity> coordinador = coordinadorRepository.findById(id);
        return coordinador.map(this::convertToDto);
    }

    // Obtener coordinadores con paginación
    public Page<CoordinadorViewDto> findAllPaginated(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<CoordinadorEntity> coordinadoresPage = coordinadorRepository.findAll(pageable);
        return coordinadoresPage.map(this::convertToDto);
    }

    // Obtener coordinadores activos con paginación
    public Page<CoordinadorViewDto> findAllActivosPaginated(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<CoordinadorEntity> coordinadoresPage = coordinadorRepository.findByActivoTrue(pageable);
        return coordinadoresPage.map(this::convertToDto);
    }

    // Obtener coordinadores por división con paginación
    public Page<CoordinadorViewDto> findByDivisionPaginated(Long divisionId, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<CoordinadorEntity> coordinadoresPage = coordinadorRepository.findByDivisionId(divisionId, pageable);
        return coordinadoresPage.map(this::convertToDto);
    }

    // Crear nuevo coordinador
    @Transactional
    public CoordinadorViewDto create(CoordinadorCreateDto coordinadorDto) {
        // Validar que el correo no esté duplicado
        if (coordinadorRepository.existsByCorreoIgnoreCase(coordinadorDto.getCorreo())) {
            throw new IllegalArgumentException("Ya existe un coordinador con el correo: " + coordinadorDto.getCorreo());
        }

        // Validar que la división existe
        Optional<Division> division = divisionRepository.findById(coordinadorDto.getDivisionId());
        if (division.isEmpty()) {
            throw new IllegalArgumentException("La división con ID " + coordinadorDto.getDivisionId() + " no existe");
        }

        CoordinadorEntity coordinador = CoordinadorEntity.builder()
                .nombre(coordinadorDto.getNombre())
                .apellido(coordinadorDto.getApellido())
                .correo(coordinadorDto.getCorreo())
                .telefono(coordinadorDto.getTelefono())
                .divisionId(coordinadorDto.getDivisionId())
                .activo(true)
                .build();

        CoordinadorEntity savedCoordinador = coordinadorRepository.save(coordinador);
        return convertToDto(savedCoordinador);
    }

    // Actualizar coordinador
    @Transactional
    public Optional<CoordinadorViewDto> update(Long id, CoordinadorUpdateDto coordinadorDto) {
        Optional<CoordinadorEntity> coordinadorOpt = coordinadorRepository.findById(id);

        if (coordinadorOpt.isEmpty()) {
            return Optional.empty();
        }

        // Validar que el correo no esté duplicado (excluyendo el coordinador actual)
        if (coordinadorRepository.existsByCorreoIgnoreCaseAndIdNot(coordinadorDto.getCorreo(), id)) {
            throw new IllegalArgumentException("Ya existe un coordinador con el correo: " + coordinadorDto.getCorreo());
        }

        // Validar que la división existe
        Optional<Division> division = divisionRepository.findById(coordinadorDto.getDivisionId());
        if (division.isEmpty()) {
            throw new IllegalArgumentException("La división con ID " + coordinadorDto.getDivisionId() + " no existe");
        }

        CoordinadorEntity coordinador = coordinadorOpt.get();
        coordinador.setNombre(coordinadorDto.getNombre());
        coordinador.setApellido(coordinadorDto.getApellido());
        coordinador.setCorreo(coordinadorDto.getCorreo());
        coordinador.setTelefono(coordinadorDto.getTelefono());
        coordinador.setDivisionId(coordinadorDto.getDivisionId());
        coordinador.setActivo(coordinadorDto.isActivo());

        CoordinadorEntity updatedCoordinador = coordinadorRepository.save(coordinador);
        return Optional.of(convertToDto(updatedCoordinador));
    }

    // Eliminar coordinador (soft delete)
    @Transactional
    public boolean delete(Long id) {
        Optional<CoordinadorEntity> coordinadorOpt = coordinadorRepository.findById(id);

        if (coordinadorOpt.isEmpty()) {
            return false;
        }

        CoordinadorEntity coordinador = coordinadorOpt.get();
        coordinador.setActivo(false);
        coordinadorRepository.save(coordinador);
        return true;
    }

    // Activar/Desactivar coordinador
    @Transactional
    public Optional<CoordinadorViewDto> toggleStatus(Long id) {
        Optional<CoordinadorEntity> coordinadorOpt = coordinadorRepository.findById(id);

        if (coordinadorOpt.isEmpty()) {
            return Optional.empty();
        }

        CoordinadorEntity coordinador = coordinadorOpt.get();
        coordinador.setActivo(!coordinador.isActivo());
        CoordinadorEntity updatedCoordinador = coordinadorRepository.save(coordinador);
        return Optional.of(convertToDto(updatedCoordinador));
    }

    // Método auxiliar para convertir Entity a DTO
    private CoordinadorViewDto convertToDto(CoordinadorEntity coordinador) {
        CoordinadorViewDto dto = new CoordinadorViewDto();
        dto.setId(coordinador.getId());
        dto.setNombre(coordinador.getNombre());
        dto.setApellido(coordinador.getApellido());
        dto.setCorreo(coordinador.getCorreo());
        dto.setTelefono(coordinador.getTelefono());
        dto.setDivisionId(coordinador.getDivisionId());
        dto.setActivo(coordinador.isActivo());

        // Obtener el nombre de la división
        Optional<Division> division = divisionRepository.findById(coordinador.getDivisionId());
        division.ifPresent(d -> dto.setDivisionNombre(d.getNombre()));

        return dto;
    }
}
