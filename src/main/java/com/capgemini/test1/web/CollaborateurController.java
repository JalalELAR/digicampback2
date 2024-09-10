package com.capgemini.test1.web;

import com.capgemini.test1.entities.Collaborateur;
import com.capgemini.test1.entities.Technologie;
import com.capgemini.test1.repositories.TechnologieRepository;
import com.capgemini.test1.service.collaborateurservice.ICollaborateurServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("collaborateurs")
public class CollaborateurController {
    private final ICollaborateurServiceImpl iCollaborateurServiceImpl;
    private final TechnologieRepository technologieRepository;

    @Autowired
    public CollaborateurController(ICollaborateurServiceImpl iCollaborateurServiceImpl,TechnologieRepository technologieRepository) {
        this.iCollaborateurServiceImpl = iCollaborateurServiceImpl;
        this.technologieRepository = technologieRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<Collaborateur> createCollaborateur(
            @RequestParam String nom,
            @RequestParam String email,
            @RequestParam String Image,
            @RequestParam Long gradeId,
            @RequestParam Long roleId,
            @RequestParam Long statutId,
            @RequestParam Long siteId,

            @RequestBody List<Long> technologieIds)  {

        byte[] image = Base64.getDecoder().decode(Image);
        List<Technologie> technologies1 = technologieRepository.findAllById(technologieIds);
        Set<Technologie> technologies = new HashSet<>(technologies1);
        Collaborateur collaborateur = iCollaborateurServiceImpl.createCollaborateur(nom, email, image,gradeId, siteId, technologies, roleId, statutId);
        return new ResponseEntity<>(collaborateur, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public Collection<Collaborateur> getAllCollaborateurs() {
        return iCollaborateurServiceImpl.GetCollaborateurs();
    }

    @GetMapping("/{id}")
    public Collaborateur getCollaborateurById(@PathVariable Long id) {
        iCollaborateurServiceImpl.AssigneCount();
        return iCollaborateurServiceImpl.GetCollaborateur(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCollaborateur(@PathVariable Long id) {
        iCollaborateurServiceImpl.DeleteCollaborateur(id);
    }

    @PutMapping("/{id}")
    public Collaborateur updateCollaborateur(@PathVariable Long id, @RequestBody Collaborateur collaborateurDetails) {
        iCollaborateurServiceImpl.AssigneCount();
        return iCollaborateurServiceImpl.updateCollaborateur(id, collaborateurDetails);
        }

//       @PatchMapping("/{id}")
//        public Optional<Collaborateur> partialUpdateCollaborateur(@PathVariable Long id, @RequestBody Collaborateur collaborateurDetails) {
//            iCollaborateurServiceImpl.AssigneCount();
//            return iCollaborateurServiceImpl.partialUpdateCollaborateur(id, collaborateurDetails);
//        }

    @GetMapping("/search")
    public List<Collaborateur> searchCollaborateurs(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String siteName,
            @RequestParam(required = false) String gradeName,
            @RequestParam(required = false) String technologieName,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String Statut
            ) {
        iCollaborateurServiceImpl.AssigneCount();
        return iCollaborateurServiceImpl.searchCollaborateurs(nom, email, siteName, gradeName, technologieName, role, Statut);
    }
    @PostMapping("/{id}/image")
    public ResponseEntity<?> uploadCollaborateurImage(@PathVariable Long id, @RequestParam("image") MultipartFile image) {
        try {
            // Check if the file is empty
            if (image.isEmpty()) {
                return new ResponseEntity<>("Image file is empty", HttpStatus.BAD_REQUEST);
            }

            // Retrieve the collaborator
            Collaborateur collaborateur = iCollaborateurServiceImpl.findCollaborateurById(id);
            if (collaborateur == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Convert image to byte array
            byte[] imageBytes = image.getBytes();
            collaborateur.setImage(imageBytes);

            // Save the collaborator
            iCollaborateurServiceImpl.SaveCollaborateur(collaborateur);

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
    public ResponseEntity<byte[]> getCollaborateurImage(@PathVariable Long id) {
        Collaborateur collaborateur = iCollaborateurServiceImpl.findCollaborateurById(id);
        if (collaborateur != null && collaborateur.getImage() != null) {
            byte[] imageBytes = collaborateur.getImage();
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // or MediaType.IMAGE_PNG depending on the format
                    .body(imageBytes);
        }
        return ResponseEntity.notFound().build();
    }
}
