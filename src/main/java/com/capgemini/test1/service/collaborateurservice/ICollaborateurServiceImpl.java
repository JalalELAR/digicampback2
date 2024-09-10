package com.capgemini.test1.service.collaborateurservice;

import com.capgemini.test1.entities.*;
import com.capgemini.test1.repositories.*;
import com.capgemini.test1.repositories.CollaborateurRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class ICollaborateurServiceImpl  {

    private CollaborateurRepository collaborateurRepository;
    private SiteRepository siteRepository;
    private GradeRepository gradeRepository;
    private RoleCollabRepository roleCollabRepository;
    private StatutCollabRepository statutCollabRepository;

    public ICollaborateurServiceImpl(CollaborateurRepository collaborateurRepository,SiteRepository siteRepository,GradeRepository gradeRepository, RoleCollabRepository roleCollabRepository, StatutCollabRepository statutCollabRepository) {
        this.collaborateurRepository = collaborateurRepository;
        this.siteRepository = siteRepository;
        this.gradeRepository = gradeRepository;
        this.roleCollabRepository = roleCollabRepository;
        this.statutCollabRepository = statutCollabRepository;

    }
    public Collaborateur createCollaborateur(String nom, String email, byte[] image, Long gradeId, Long siteId,
                                             Collection<Technologie> technologies, Long roleId,Long statutId) {
        // Find the site and handle if not found
        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new NoSuchElementException("Site not found for ID: " + siteId));

        // Find the grade and handle if not found
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new NoSuchElementException("Grade not found for ID: " + gradeId));

        // Find the role and handle if not found
        RoleCollab roleCollab = roleCollabRepository.findById(roleId)
                .orElseThrow(() -> new NoSuchElementException("RoleCollab not found for ID: " + roleId));

        // Find the statut and handle if not found
        StatutCollab statutCollab = statutCollabRepository.findById(statutId)
                .orElseThrow(() -> new NoSuchElementException("StatutCollab not found for ID: " + statutId));

        // Create the collaborateur object
        Collaborateur collaborateur = new Collaborateur();
        collaborateur.setNom(nom);
        collaborateur.setEmail(email);
        collaborateur.setImage(image);
        collaborateur.setGrade(grade);
        collaborateur.setSite(site);
        collaborateur.setTechnologies(technologies);
        collaborateur.setRoleCollab(roleCollab);
        collaborateur.setStatutCollab(statutCollab);

        // Save the collaborateur
        return collaborateurRepository.save(collaborateur);
    }

    public Collaborateur SaveCollaborateur(Collaborateur collaborateur) {
        return collaborateurRepository.save(collaborateur);
    }
    public Collection<Collaborateur> GetCollaborateurs() {
        List<Collaborateur> collaborateurs = collaborateurRepository.findAll();
        for (Collaborateur collaborateur : collaborateurs) {
            collaborateur.setProjectCount(collaborateur.getProjets().size());
        }
        return collaborateurs;
    }
    public void AssigneCount() {
        List<Collaborateur> collaborateurs = collaborateurRepository.findAll();
        for (Collaborateur collaborateur : collaborateurs) {
            collaborateur.setProjectCount(collaborateur.getProjets().size());
        }
    }

    public Collaborateur GetCollaborateur(Long id) {
        Collaborateur collaborateur = collaborateurRepository.findById(id).orElseThrow(() -> new RuntimeException("Collaborateur not found"));
        collaborateur.setProjectCount(collaborateur.getProjets().size());
        return collaborateur;
    }

    public void DeleteCollaborateur(Long id) {
        Collaborateur collaborateur = collaborateurRepository.findById(id).orElse(null);
        if (collaborateur != null) {
            // Supprimer les relations du côté Projet
            collaborateur.getProjets().forEach(projet -> projet.getCollaborateurs().remove(collaborateur));
            // Clear the associations with Technologie
            collaborateur.getTechnologies().clear();
            // Save the Collaborateur to update the join table
            collaborateurRepository.save(collaborateur);
            // Ensuite, supprimer le collaborateur
            collaborateurRepository.deleteById(id);
        }
    }


    public Collaborateur updateCollaborateur(Long id, Collaborateur collaborateurDetails) {
        Collaborateur newCollaborator = new Collaborateur();
        newCollaborator.setId(id);
        newCollaborator.setNom(collaborateurDetails.getNom());
        newCollaborator.setEmail(collaborateurDetails.getEmail());
        newCollaborator.setImage(collaborateurDetails.getImage());
        newCollaborator.setTechnologies(collaborateurDetails.getTechnologies());
        newCollaborator.setSite(collaborateurDetails.getSite());
        newCollaborator.setProjets(collaborateurDetails.getProjets());
        newCollaborator.setGrade(collaborateurDetails.getGrade());
        newCollaborator.setStatutCollab(collaborateurDetails.getStatutCollab());
        newCollaborator.setRoleCollab(collaborateurDetails.getRoleCollab());
        return collaborateurRepository.save(newCollaborator);
    }

//    public Optional<Collaborateur> partialUpdateCollaborateur(Long id, Collaborateur collaborateurDetails) {
//        return collaborateurRepository.findById(id).map(collaborateur -> {
//            if (collaborateurDetails.getNom() != null) {
//                collaborateur.setNom(collaborateurDetails.getNom());
//            }
//            if (collaborateurDetails.getEmail() != null) {
//                collaborateur.setEmail(collaborateurDetails.getEmail());
//            }
//            if (collaborateurDetails.getImage() != null) {
//                collaborateur.setImage(collaborateurDetails.getImage());
//            }
//            if (collaborateurDetails.getTechnologies() != null) {
//                collaborateur.setTechnologies(collaborateurDetails.getTechnologies());
//            }
//            if (collaborateurDetails.getSite() != null) {
//                collaborateur.setSite(collaborateurDetails.getSite());
//            }
//            if (collaborateurDetails.getProjets() != null) {
//                collaborateur.setProjets(collaborateurDetails.getProjets());
//            }
//            if (collaborateurDetails.getGrade() != null) {
//                collaborateur.setGrade(collaborateurDetails.getGrade());
//            }
//            return collaborateurRepository.save(collaborateur);
//        });
//    }

    public List<Collaborateur> searchCollaborateurs(String nom, String email, String siteName, String gradeName, String technologieName, String role, String Statut) {
        return collaborateurRepository.findByCriteria(nom, email, siteName, gradeName, technologieName, role, Statut);
    }
    public Collaborateur findCollaborateurById(Long id) {
        Optional<Collaborateur> optionalCollaborateur = collaborateurRepository.findById(id);
        if (optionalCollaborateur.isPresent()) {
            return optionalCollaborateur.get();
        } else {
            throw new RuntimeException("Collaborateur not found for id: " + id);
        }
    }
}
