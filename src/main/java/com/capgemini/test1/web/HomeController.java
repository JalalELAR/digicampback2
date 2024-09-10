package com.capgemini.test1.web;

import com.capgemini.test1.entities.Collaborateur;
import com.capgemini.test1.entities.Projet;
import com.capgemini.test1.service.collaborateurservice.ICollaborateurServiceImpl;
import com.capgemini.test1.service.projetservice.IProjetServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {

    private final IProjetServiceImpl projetService;
    private final ICollaborateurServiceImpl collaborateurService;

    @Autowired
    public HomeController(IProjetServiceImpl projetService, ICollaborateurServiceImpl collaborateurService) {
        this.projetService = projetService;
        this.collaborateurService = collaborateurService;
    }

    @GetMapping("/projets")
    public ResponseEntity<List<Projet>> getProjets() {
        List<Projet> projets = projetService.getAllProjets();
        return ResponseEntity.ok(projets);
    }

    @GetMapping("/collaborateurs")
    public ResponseEntity<List<Collaborateur>> getCollaborateurs() {
        // Convert Collection to List
        List<Collaborateur> collaborateurs = new ArrayList<>(collaborateurService.GetCollaborateurs());
        return ResponseEntity.ok(collaborateurs);
    }
}
