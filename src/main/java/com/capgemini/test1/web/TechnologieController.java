package com.capgemini.test1.web;

import com.capgemini.test1.entities.Technologie;
import com.capgemini.test1.service.technologieservice.ITechnologieServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("technologies")
public class TechnologieController {

    private ITechnologieServiceImpl technologieService;
    public TechnologieController(ITechnologieServiceImpl technologieService) {
        this.technologieService = technologieService;
    }

    @PostMapping("/create")
    public ResponseEntity<Technologie> createTechnologie(@RequestParam String name) {
     Technologie technologie = technologieService.createTechnologie(name);
     return new ResponseEntity<>(technologie, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public List<Technologie> getAllTechnologies() {
        return technologieService.getAllTechnologies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Technologie> getTechnologieById(@PathVariable Long id) {
        Technologie technologie = technologieService.getTechnologieById(id);
        return ResponseEntity.ok(technologie);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Technologie> updateTechnologie(@PathVariable Long id, @RequestBody Technologie technologieDetails) {
        Technologie updatedTechnologie = technologieService.updateTechnologie(id, technologieDetails);
        return ResponseEntity.ok(updatedTechnologie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnologie(@PathVariable Long id) {
        technologieService.deleteTechnologie(id);
        return ResponseEntity.noContent().build();
    }
}
