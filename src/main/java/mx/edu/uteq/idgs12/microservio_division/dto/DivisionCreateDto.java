package mx.edu.uteq.idgs12.microservio_division.dto;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DivisionCreateDto {
    
    @NotBlank(message = "El nombre de la división es obligatorio")
    private String nombre;
    
    private List<ProgramaEducativoDto> programasEducativos;
}

