package mx.edu.uteq.idgs12.microservio_division.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.edu.uteq.idgs12.microservio_division.entity.Division;


@Repository
public interface DivisionRepository extends JpaRepository<Division, Long> {
    
    // Buscar todas las divisiones activas
    List<Division> findByActivoTrue();
    
    // Buscar divisiones por nombre
    List<Division> findByNombreContainingIgnoreCase(String nombre);
    
    // Verificar si existe una división con un nombre específico
    boolean existsByNombreIgnoreCase(String nombre);
}
