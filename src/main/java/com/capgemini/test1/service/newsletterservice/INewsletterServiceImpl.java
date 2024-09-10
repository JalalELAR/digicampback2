package com.capgemini.test1.service.newsletterservice;

import com.capgemini.test1.entities.Newsletter;
import com.capgemini.test1.entities.Collaborateur;
import com.capgemini.test1.repositories.NewsletterRepository;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.properties.TextAlignment;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class INewsletterServiceImpl {
    private NewsletterRepository newsletterRepository;
    public INewsletterServiceImpl(NewsletterRepository newsletterRepository) {
        this.newsletterRepository = newsletterRepository;
    }
    public List<Newsletter> getAllNewsletters() {
        return newsletterRepository.findAll();
    }

    public Newsletter getNewsletterById(Long id) {
        return newsletterRepository.findById(id).orElseThrow(() -> new RuntimeException("Newsletter not found"));
    }

    public Newsletter saveNewsletter(Newsletter newsletter) {
        return newsletterRepository.save(newsletter);
    }

    public Newsletter updateNewsletter(Long id, Newsletter newsletterDetails) {
        Newsletter newsletter = getNewsletterById(id);
        newsletter.setProjet(newsletterDetails.getProjet());
        return newsletterRepository.save(newsletter);
    }

    public void deleteNewsletter(Long id) {
        newsletterRepository.deleteById(id);
    }

}



