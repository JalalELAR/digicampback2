package com.capgemini.test1.web;

import com.capgemini.test1.entities.Collaborateur;
import com.capgemini.test1.entities.Projet;
import com.capgemini.test1.entities.Status;
import com.capgemini.test1.service.collaborateurservice.ICollaborateurServiceImpl;
import com.capgemini.test1.service.projetservice.IProjetServiceImpl;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("projets")
public class ProjetController {

    private IProjetServiceImpl iProjetServiceImpl;
    private ICollaborateurServiceImpl iCollaborateurServiceImpl;

    public ProjetController(IProjetServiceImpl iProjetServiceImpl,ICollaborateurServiceImpl iCollaborateurServiceImpl) {
        this.iProjetServiceImpl = iProjetServiceImpl;
        this.iCollaborateurServiceImpl = iCollaborateurServiceImpl;
    }

    @PostMapping("/create")
    public ResponseEntity<Projet> createProjet(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam(required = false) String Image,
            @RequestParam Long statusId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dated,
            @RequestParam(required = false) String collaboratorIds) {
        System.out.println("Received parameters:");
        System.out.println("Title: " + title);
        System.out.println("Description: " + description);
        System.out.println("StatusId: " + statusId);
        System.out.println("image: " + Image);
        System.out.println("Dated: " + dated);
        System.out.println("CollaboratorIds: " + collaboratorIds);
        try {
            List<Long> collaboratorIdsList = Arrays.stream(collaboratorIds.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            //byte[] image ;
            //if (Image != null && !Image.isEmpty()) {}
            byte[]  image = Base64.getDecoder().decode(Image);
            Projet projet = iProjetServiceImpl.createProjet(title, description,image,statusId, dated,collaboratorIdsList);
            return new ResponseEntity<>(projet, HttpStatus.CREATED);
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /*
    @PostMapping("/create")
    public ResponseEntity<Projet> createProjet(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam("image") MultipartFile multipartFile,
            @RequestParam Long statusId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dated,
            @RequestParam List<Long> collaboratorIds){
        System.out.println("Received parameters:");
        System.out.println("Title: " + title);
        System.out.println("Description: " + description);
        System.out.println("StatusId: " + statusId);
        System.out.println("Dated: " + dated);
        System.out.println("CollaboratorIds: " + collaboratorIds);
        try {
            byte[] image = multipartFile.getBytes();
            Projet projet = iProjetServiceImpl.createProjet(title, description,image,statusId, dated,collaboratorIds);
            return new ResponseEntity<>(projet, HttpStatus.CREATED);
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    @GetMapping("/list")
    public List<Projet> getAllProjets() {
        iCollaborateurServiceImpl.AssigneCount();
        return iProjetServiceImpl.getAllProjets();
    }

    @GetMapping("/{id}")
    public Projet getProjetById(@PathVariable Long id) {
        iCollaborateurServiceImpl.AssigneCount();
        return iProjetServiceImpl.getProjetById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteProjet(@PathVariable Long id) {
        iProjetServiceImpl.deleteProjet(id);
    }

    @PutMapping("/{id}")
    public Optional<Projet> updateProjet(@PathVariable Long id, @RequestBody Projet projetDetails) {
        iCollaborateurServiceImpl.AssigneCount();
        return iProjetServiceImpl.updateProjet(id, projetDetails);
    }

   /* @PatchMapping("/{id}")
    public Optional<Projet> partialUpdateProjet(@PathVariable Long id, @RequestBody Projet projetDetails) {
        return iProjetServiceImpl.partialUpdateProjet(id, projetDetails);
    }*/

    @GetMapping("/search")
    public List<Projet> searchProjets(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) String siteName,
            @RequestParam(required = false) Date dated,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate) {
        iCollaborateurServiceImpl.AssigneCount();
        if (startDate != null && endDate != null) {
            return iProjetServiceImpl.findProjetsByDateRange(startDate, endDate);
        } else {
            return iProjetServiceImpl.findByCriteria(title, status, siteName, dated);
        }}

    @GetMapping("/search/{title}")
    public List<Projet> findProjetsByTitle(@PathVariable String title) {
        iCollaborateurServiceImpl.AssigneCount();
        return iProjetServiceImpl.findProjetsByTitle(title);
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<?> uploadProjetImage(@PathVariable Long id, @RequestParam("image") MultipartFile image) {
        try {
            // Check if the file is empty
            if (image.isEmpty()) {
                return new ResponseEntity<>("Image file is empty", HttpStatus.BAD_REQUEST);
            }

            // Retrieve the collaborator
            Projet projet = iProjetServiceImpl.getProjetById(id);
            if (projet == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Convert image to byte array
            byte[] imageBytes = image.getBytes();
            projet.setImage(imageBytes);

            // Save the collaborator
            iProjetServiceImpl.saveProjet(projet);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            // Log the exception
            e.printStackTrace();
            return new ResponseEntity<>("Error processing image upload: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            // Log any other exceptions
            e.printStackTrace();
            return new ResponseEntity<>("Unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getProjetImage(@PathVariable Long id) {
        Projet projet = iProjetServiceImpl.getProjetById(id);
        if (projet != null && projet.getImage() != null) {
            byte[] imageBytes = projet.getImage();
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // or MediaType.IMAGE_PNG depending on the format
                    .body(imageBytes);
        }
        return ResponseEntity.notFound().build();
    }

}
