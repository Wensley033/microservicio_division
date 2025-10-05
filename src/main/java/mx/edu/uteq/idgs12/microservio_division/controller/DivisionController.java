package mx.edu.uteq.idgs12.microservio_division.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.edu.uteq.idgs12.microservio_division.dto.DivisionToViewListDto;
import mx.edu.uteq.idgs12.microservio_division.entity.Division;
import mx.edu.uteq.idgs12.microservio_division.repository.DivisionRepository;
import mx.edu.uteq.idgs12.microservio_division.service.DivisionService;




@RestController
@RequestMapping("/api/divisiones")
public class DivisionController {
    @Autowired
    private DivisionService divisionService;

    @Autowired
    public DivisionRepository divisionRepository;

    @GetMapping //("path")
    public List<DivisionToViewListDto> getAllDivisiones() {
        return divisionService.FindAll();
    }
    
    @GetMapping("/all")
    public List<Division> getAll(){
        return divisionRepository.findAll();
    }
    

}
