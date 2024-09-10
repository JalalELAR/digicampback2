package com.capgemini.test1.service.projetservice;

import com.capgemini.test1.entities.Collaborateur;
import com.capgemini.test1.entities.Newsletter;
import com.capgemini.test1.entities.Projet;
import com.capgemini.test1.entities.Status;
import com.capgemini.test1.repositories.CollaborateurRepository;
import com.capgemini.test1.repositories.NewsletterRepository;
import com.capgemini.test1.repositories.ProjetRepository;
import com.capgemini.test1.repositories.StatusRepository;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.transaction.Transactional;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

//import javax.swing.text.Document;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Service
@Transactional
public class IProjetServiceImpl  {
    private ProjetRepository projetRepository;
    private NewsletterRepository newsletterRepository;
    private StatusRepository statusRepository;
    private CollaborateurRepository collaborateurRepository;
    private JavaMailSender javaMailSender;
    public IProjetServiceImpl(ProjetRepository projetRepository,CollaborateurRepository collaborateurRepository,StatusRepository statusRepository,NewsletterRepository newsletterRepository,JavaMailSender javaMailSender) {
        this.projetRepository = projetRepository;
        this.newsletterRepository = newsletterRepository;
        this.javaMailSender = javaMailSender;
        this.statusRepository = statusRepository;
        this.collaborateurRepository = collaborateurRepository;
    }
    public Projet saveProjet(Projet projet) {
        //Projet projetSaved = new Projet();
        //projetSaved.setTitle(projet.getTitle());
        //projetSaved.setDescription(projet.getDescription());
        //projetSaved.setStatus(projet.getStatus());
        return projetRepository.save(projet);
    }

    public List<Projet> getAllProjets() {
        return projetRepository.findAll();
    }

    public Projet getProjetById(Long id) {
        return projetRepository.findById(id).orElse(null);
    }

    public void deleteProjet(Long id) {
        Projet projet = projetRepository.findById(id).orElse(null);
        if (projet != null) {
            projet.getCollaborateurs().forEach(collaborateur -> collaborateur.getProjets().remove(projet));
            // Update newsletters to remove reference to the projet
            newsletterRepository.updateProjetIdToNull(id);
            projetRepository.deleteById(id);
        }
    }


    public Optional<Projet> updateProjet(Long id, Projet projetDetails) {
        return projetRepository.findById(id).map(projet -> {
            // Met à jour les champs du projet avec les nouvelles valeurs
            projet.setTitle(projetDetails.getTitle());
            projet.setDescription(projetDetails.getDescription());
            projet.setImage(projetDetails.getImage());
            projet.setStatus(projetDetails.getStatus());
            projet.setDated(projetDetails.getDated());
            projet.setNewsletters(projetDetails.getNewsletters());

            // Met à jour la liste des collaborateurs associés au projet
            // Utilise les collaborateurs présents dans 'projetDetails'
            List<Collaborateur> updatedCollaborateurs = new ArrayList<>();
            for (Collaborateur collab : projetDetails.getCollaborateurs()) {
                collaborateurRepository.findById(collab.getId()).ifPresent(updatedCollaborateurs::add);
            }
            projet.setCollaborateurs(updatedCollaborateurs);

            // Sauvegarde le projet mis à jour
            return projetRepository.save(projet);
        });
    }

    public Optional<Projet> partialUpdateProjet(Long id, Projet projetDetails) {
        return projetRepository.findById(id).map(projet -> {
            if (projetDetails.getTitle() != null) {
                projet.setTitle(projetDetails.getTitle());
            }
            if (projetDetails.getDescription() != null) {
                projet.setDescription(projetDetails.getDescription());
            }
           // if (projetDetails.getImage() != null) {
             //   projet.setImage(projetDetails.getImage());
           // }
            if (projetDetails.getStatus() != null) {
                projet.setStatus(projetDetails.getStatus());
            }
            if (projetDetails.getDated() != null) {
                projet.setDated(projetDetails.getDated());
            }
            if (projetDetails.getNewsletters() != null) {
                projet.setNewsletters(projetDetails.getNewsletters());
            }
            if (projetDetails.getCollaborateurs() != null) {
                projet.setCollaborateurs(projetDetails.getCollaborateurs());
            }
            return projetRepository.save(projet);
        });
    }

    public List<Projet> findByCriteria(String title, Status status, String siteName, Date dated) {
        return projetRepository.findByCriteria(title, status, siteName, dated);
    }

    public List<Projet> findProjetsByTitle(String title) {
        return projetRepository.findByTitleContaining(title);
    }

    public List<Projet> findProjetsByDateRange(Date startDate, Date endDate) {
        return projetRepository.findByDateRange(startDate, endDate);
    }

    public Projet createProjet(String title, String description,byte[] image,Long statusId, Date dated, List<Long> collaboratorIds) throws IOException, MessagingException {
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new RuntimeException("Status not found"));
        Projet projet = new Projet();
        projet.setTitle(title);
        projet.setDescription(description);
        projet.setImage(image);
        projet.setStatus(status);
        projet.setDated(dated);
        List<Collaborateur> collaborateurs = collaborateurRepository.findAllById(collaboratorIds);
        Set<Collaborateur> collaborators = new HashSet<>(collaborateurs);
        projet.setCollaborateurs(collaborators);

        Projet savedProjet = projetRepository.save(projet);

        // Generate and save the newsletter for the new project
        generateAndSaveNewsletter(savedProjet);

        return savedProjet;
    }

    private void generateAndSaveNewsletter(Projet projet) throws MessagingException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        Paragraph title = new Paragraph(projet.getTitle())
                .setFontSize(20)
                .setFontColor(ColorConstants.BLACK)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(title);

        Paragraph projectTitle = new Paragraph("Project: " + projet.getTitle())
                .setFontSize(16)
                .setFontColor(ColorConstants.DARK_GRAY)
                .setTextAlignment(TextAlignment.LEFT);
        document.add(projectTitle);

        Paragraph projectDescription = new Paragraph("Description: " + projet.getDescription())
                .setFontSize(12)
                .setFontColor(ColorConstants.BLACK)
                .setTextAlignment(TextAlignment.LEFT);
        document.add(projectDescription);

        Paragraph projectDate = new Paragraph("Date: " + projet.getDated())
                .setFontSize(12)
                .setFontColor(ColorConstants.BLACK)
                .setTextAlignment(TextAlignment.LEFT);
        document.add(projectDate);

        document.close();

        Newsletter newsletter = new Newsletter();
        newsletter.setTitle(projet.getTitle() + " Newsletter");
        newsletter.setPdfContent(out.toByteArray());
        newsletter.setProjet(projet);

        newsletterRepository.save(newsletter);

        // Send email with the newsletter PDF attached
        sendEmailWithAttachment(
                "mouad-ali.alil@capgemini.com",  // Replace with the actual recipient email address
                "Newsletter: " + newsletter.getTitle(),
                "Please find the attached newsletter PDF.",
                newsletter.getPdfContent(),
                newsletter.getTitle() + ".pdf"
        );
    }

    private void sendEmailWithAttachment(String to, String subject, String text, byte[] pdfContent, String attachmentFilename) throws MessagingException {
        try{
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("MTACOR1.capgemini.com");
            mailSender.setPort(587);
            mailSender.setProtocol("smtp");
            mailSender.setUsername("jalal.elarache@capgemini.com");
            mailSender.setPassword("Support2002");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MULTIPART_MODE_MIXED,
                UTF_8.name());
        helper.setFrom("jalal.elarache@capgemini.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        helper.addBcc("jalal.elarache@capgemini.com");
        helper.addAttachment(attachmentFilename, new ByteArrayDataSource(pdfContent, "application/pdf"));
        System.out.println("Email envoyé avec succès à " + to);
        mailSender.send(message);

    } catch (MailAuthenticationException e) {
        System.out.println("EmailService::sendFeedbackRequestEmail wrong password");
    } catch (MailSendException | MessagingException e) {
        System.out.println("EmailService::sendFeedbackRequestEmail failed to sent email");
        throw new IllegalStateException("exception.messaging");
    } catch (Exception e) {
        System.out.println("EmailService::sendFeedbackRequestEmail unknown error");
        throw new IllegalStateException("exception.messaging");
    }
    }
}
