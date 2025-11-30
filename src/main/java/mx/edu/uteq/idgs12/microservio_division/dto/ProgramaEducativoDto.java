package mx.edu.uteq.idgs12.microservio_division.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProgramaEducativoDto {

    private Long id;

    @NotBlank(message = "El nombre del programa educativo es obligatorio")
    private String nombre;

    private boolean activo;
}
