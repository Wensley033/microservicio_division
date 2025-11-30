package mx.edu.uteq.idgs12.microservio_division.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coordinadores")
public class CoordinadorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String apellido;
    
    @Column(unique = true, nullable = false)
    private String correo;
    
    private String telefono;
    
    @Column(name = "division_id", nullable = false)
    private Long divisionId; // FK a División
    
    @Column(nullable = false)
    private boolean activo = true;
}