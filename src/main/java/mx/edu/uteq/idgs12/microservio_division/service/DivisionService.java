package mx.edu.uteq.idgs12.microservio_division.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.edu.uteq.idgs12.microservio_division.dto.DivisionToViewListDto;
import mx.edu.uteq.idgs12.microservio_division.entity.Division;
import mx.edu.uteq.idgs12.microservio_division.entity.ProgramaEducativa;
import mx.edu.uteq.idgs12.microservio_division.repository.DivisionRepository;


@Service
public class DivisionService {

    @Autowired
    private DivisionRepository divisionRepository;
    

    public List<DivisionToViewListDto> FindAll() {
        List <Division> divisiones = divisionRepository.findAll();
        List<DivisionToViewListDto> resultado = new ArrayList<>();
        for (Division division : divisiones) {
            DivisionToViewListDto dto = new DivisionToViewListDto();
            dto.setDivisionId(division.getId());
            dto.setNombre(division.getNombre());
            if (division.getProgramaEducativas() != null) {
                List<String> programas = new ArrayList<>();
                for (ProgramaEducativa prog : division.getProgramaEducativas()) {
                    programas.add(prog.getPrograma());
                }
                dto.setProgramaEducativa (programas);
            } else {
                dto.setNumeroProgramas(0);
            }
            resultado.add(dto);
        }
        return resultado;
    }
}
