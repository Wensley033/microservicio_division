package mx.edu.uteq.idgs12.microservio_division.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.edu.uteq.idgs12.microservio_division.entity.CoordinadorEntity;

@Repository
public interface CoordinadorRepository extends JpaRepository<CoordinadorEntity, Long> {

    // Buscar coordinadores activos
    List<CoordinadorEntity> findByActivoTrue();

    // Buscar coordinadores por división
    List<CoordinadorEntity> findByDivisionId(Long divisionId);

    // Buscar coordinadores activos por división
    List<CoordinadorEntity> findByDivisionIdAndActivoTrue(Long divisionId);

    // Verificar si existe un correo
    boolean existsByCorreoIgnoreCase(String correo);

    // Verificar si existe un correo excluyendo un ID
    boolean existsByCorreoIgnoreCaseAndIdNot(String correo, Long id);

    // Paginación: Todos los coordinadores
    Page<CoordinadorEntity> findAll(Pageable pageable);

    // Paginación: Coordinadores activos
    Page<CoordinadorEntity> findByActivoTrue(Pageable pageable);

    // Paginación: Coordinadores por división
    Page<CoordinadorEntity> findByDivisionId(Long divisionId, Pageable pageable);
}
