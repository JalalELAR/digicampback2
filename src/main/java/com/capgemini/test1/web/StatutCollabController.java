package com.capgemini.test1.web;


import com.capgemini.test1.entities.StatutCollab;
import com.capgemini.test1.service.statutcollabservice.StatutCollabService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("statutscollab")
public class StatutCollabController {
    private StatutCollabService statutCollabService;

    public StatutCollabController(StatutCollabService statutCollabService) {
        this.statutCollabService = statutCollabService;
    }
    @PostMapping("/create")
    public ResponseEntity<StatutCollab> createStatus(@RequestParam String statut) {
        StatutCollab statutcollab = statutCollabService.createStatutCollab(statut);
        return new ResponseEntity<>(statutcollab, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<StatutCollab> update(@PathVariable Long id,@RequestBody StatutCollab Statut) {
        StatutCollab updatedStatutCollab = statutCollabService.UpdateStatutCollab(id,Statut);
        return ResponseEntity.ok(updatedStatutCollab);
    }
    @DeleteMapping("/{id}")
    public void deletestatutcollab(@PathVariable Long id) {
        StatutCollab statutCollab = statutCollabService.getById(id);
        statutCollabService.delete(statutCollab);
    }
    @GetMapping("/{id}")
    public ResponseEntity<StatutCollab> getstatutbyid(@PathVariable Long id) {
        StatutCollab statutcollab = statutCollabService.getById(id);
        return new ResponseEntity<>(statutcollab, HttpStatus.CREATED);
    } 
    @GetMapping("/list")
    public List<StatutCollab> getallstatuts() {
        return statutCollabService.getAll();
    }
}
