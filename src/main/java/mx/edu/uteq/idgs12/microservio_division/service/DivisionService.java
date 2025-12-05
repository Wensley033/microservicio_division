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

import mx.edu.uteq.idgs12.microservio_division.dto.DivisionCreateDto;
import mx.edu.uteq.idgs12.microservio_division.dto.DivisionToViewListDto;
import mx.edu.uteq.idgs12.microservio_division.dto.DivisionUpdateDto;
import mx.edu.uteq.idgs12.microservio_division.entity.Division;
import mx.edu.uteq.idgs12.microservio_division.entity.ProgramaEducativa;
import mx.edu.uteq.idgs12.microservio_division.repository.DivisionRepository;

@Service
public class DivisionService {

    @Autowired
    private DivisionRepository divisionRepository;

    // Obtener todas las divisiones
    public List<DivisionToViewListDto> findAll() {
        List<Division> divisiones = divisionRepository.findAll();
        return divisiones.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Obtener solo divisiones activas
    public List<DivisionToViewListDto> findAllActivas() {
        List<Division> divisiones = divisionRepository.findByActivoTrue();
        return divisiones.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Buscar divisiones por nombre
    public List<DivisionToViewListDto> findByNombre(String nombre) {
        List<Division> divisiones = divisionRepository.findByNombreContainingIgnoreCase(nombre);
        return divisiones.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Obtener todas las divisiones con paginación
    public Page<DivisionToViewListDto> findAllPaginated(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<Division> divisionesPage = divisionRepository.findAll(pageable);
        return divisionesPage.map(this::convertToDto);
    }

    // Obtener divisiones activas con paginación
    public Page<DivisionToViewListDto> findAllActivasPaginated(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<Division> divisionesPage = divisionRepository.findByActivoTrue(pageable);
        return divisionesPage.map(this::convertToDto);
    }

    // Buscar divisiones por nombre con paginación
    public Page<DivisionToViewListDto> findByNombrePaginated(String nombre, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<Division> divisionesPage = divisionRepository.findByNombreContainingIgnoreCase(nombre, pageable);
        return divisionesPage.map(this::convertToDto);
    }

    // Obtener división por ID
    public Optional<DivisionToViewListDto> findById(Long id) {
        Optional<Division> division = divisionRepository.findById(id);
        return division.map(this::convertToDto);
    }

    // Crear nueva división
    @Transactional
    public DivisionToViewListDto create(DivisionCreateDto divisionDto) {
        // Validar que el nombre no esté duplicado
        if (divisionRepository.existsByNombreIgnoreCase(divisionDto.getNombre())) {
            throw new IllegalArgumentException("Ya existe una división con el nombre: " + divisionDto.getNombre());
        }

        Division division = new Division();
        division.setNombre(divisionDto.getNombre());
        division.setActivo(true);

        // Agregar programas educativos si existen
        if (divisionDto.getProgramasEducativos() != null && !divisionDto.getProgramasEducativos().isEmpty()) {
            List<ProgramaEducativa> programas = divisionDto.getProgramasEducativos().stream()
                    .map(dto -> {
                        ProgramaEducativa programa = new ProgramaEducativa();
                        programa.setPrograma(dto.getNombre());
                        programa.setActivo(true);
                        return programa;
                    })
                    .collect(Collectors.toList());
            division.setProgramaEducativas(programas);
        }

        Division savedDivision = divisionRepository.save(division);
        return convertToDto(savedDivision);
    }

    // Actualizar división existente
    @Transactional
    public Optional<DivisionToViewListDto> update(Long id, DivisionUpdateDto divisionDto) {
        Optional<Division> divisionOpt = divisionRepository.findById(id);

        if (divisionOpt.isEmpty()) {
            return Optional.empty();
        }

        // Validar que el nombre no esté duplicado (excluyendo la división actual)
        if (divisionRepository.existsByNombreIgnoreCaseAndIdNot(divisionDto.getNombre(), id)) {
            throw new IllegalArgumentException("Ya existe una división con el nombre: " + divisionDto.getNombre());
        }

        Division division = divisionOpt.get();
        division.setNombre(divisionDto.getNombre());

        // Actualizar programas educativos
        if (divisionDto.getProgramasEducativos() != null) {
            // Limpiar programas existentes
            division.getProgramaEducativas().clear();

            // Agregar nuevos programas
            List<ProgramaEducativa> programas = divisionDto.getProgramasEducativos().stream()
                    .map(dto -> {
                        ProgramaEducativa programa = new ProgramaEducativa();
                        if (dto.getId() != null) {
                            programa.setId(dto.getId());
                        }
                        programa.setPrograma(dto.getNombre());
                        programa.setActivo(dto.isActivo());
                        return programa;
                    })
                    .collect(Collectors.toList());
            division.getProgramaEducativas().addAll(programas);
        }

        Division updatedDivision = divisionRepository.save(division);
        return Optional.of(convertToDto(updatedDivision));
    }

    // Eliminar división (soft delete)
    @Transactional
    public boolean delete(Long id) {
        Optional<Division> divisionOpt = divisionRepository.findById(id);
        
        if (divisionOpt.isEmpty()) {
            return false;
        }

        Division division = divisionOpt.get();
        division.setActivo(false);
        
        // También desactivar todos los programas educativos
        if (division.getProgramaEducativas() != null) {
            division.getProgramaEducativas().forEach(p -> p.setActivo(false));
        }
        
        divisionRepository.save(division);
        return true;
    }

    // Activar/Desactivar división
    @Transactional
    public Optional<DivisionToViewListDto> toggleStatus(Long id) {
        Optional<Division> divisionOpt = divisionRepository.findById(id);
        
        if (divisionOpt.isEmpty()) {
            return Optional.empty();
        }

        Division division = divisionOpt.get();
        division.setActivo(!division.isActivo());
        Division updatedDivision = divisionRepository.save(division);
        return Optional.of(convertToDto(updatedDivision));
    }

    // Método auxiliar para convertir Entity a DTO
    private DivisionToViewListDto convertToDto(Division division) {
        DivisionToViewListDto dto = new DivisionToViewListDto();
        dto.setDivisionId(division.getId());
        dto.setNombre(division.getNombre());
        dto.setActivo(division.isActivo());

        if (division.getProgramaEducativas() != null) {
            List<String> programas = division.getProgramaEducativas().stream()
                    .filter(ProgramaEducativa::isActivo)
                    .map(ProgramaEducativa::getPrograma)
                    .collect(Collectors.toList());
            dto.setProgramaEducativa(programas);
            dto.setNumeroProgramas(programas.size());
        } else {
            dto.setNumeroProgramas(0);
        }

        return dto;
    }
}