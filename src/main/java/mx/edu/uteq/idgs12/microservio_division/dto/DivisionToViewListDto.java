package mx.edu.uteq.idgs12.microservio_division.dto;

import java.util.List;

import lombok.Data;

@Data
public class DivisionToViewListDto {
    private long divisionId;
    private String nombre;
    private List<String> programaEducativa;
    private boolean activo;
    private int numeroProgramas;
}
