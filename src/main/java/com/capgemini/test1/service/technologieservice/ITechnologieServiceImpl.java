package com.capgemini.test1.service.technologieservice;

import com.capgemini.test1.entities.Technologie;
import com.capgemini.test1.repositories.TechnologieRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ITechnologieServiceImpl {
    private TechnologieRepository technologieRepository;
    public ITechnologieServiceImpl(TechnologieRepository technologieRepository) {
        this.technologieRepository = technologieRepository;
    }

    public Technologie createTechnologie(String name) {
        Technologie technologie = new Technologie();
        technologie.setName(name);
        return technologieRepository.save(technologie);
    }

    public List<Technologie> getAllTechnologies() {
        return technologieRepository.findAll();
    }

    public Technologie getTechnologieById(Long id) {
        return technologieRepository.findById(id).orElseThrow(() -> new RuntimeException("Technologie not found"));
    }

    public Technologie saveTechnologie(Technologie technologie) {
        return technologieRepository.save(technologie);
    }

    public Technologie updateTechnologie(Long id, Technologie technologieDetails) {
        Technologie technologie = getTechnologieById(id);
        technologie.setName(technologieDetails.getName());
        return technologieRepository.save(technologie);
    }

    public void deleteTechnologie(Long id) {
        Technologie technologie = technologieRepository.findById(id).orElseThrow(null);
        if (technologie != null) {
            // Supprimer les relations du côté collaborateur
            technologie.getCollaborateurs().forEach(collaborateur -> collaborateur.getTechnologies().remove(technologie));
            // Supprimer la techno
            technologieRepository.deleteById(id); }
    }
}
