package mx.edu.uteq.idgs12.microservio_division.dto;

import lombok.Data;

@Data
public class CoordinadorViewDto {

    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private Long divisionId;
    private String divisionNombre;
    private boolean activo;
}
