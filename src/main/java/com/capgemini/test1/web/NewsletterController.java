package com.capgemini.test1.web;

import com.capgemini.test1.entities.Newsletter;
import com.capgemini.test1.service.newsletterservice.INewsletterServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("newsletters")
public class NewsletterController {

    private INewsletterServiceImpl newsletterService;
    public NewsletterController(INewsletterServiceImpl newsletterService) {
        this.newsletterService = newsletterService;
    }

    @GetMapping("/list")
    public List<Newsletter> getAllNewsletters() {
        return newsletterService.getAllNewsletters();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Newsletter> getNewsletterById(@PathVariable Long id) {
        Newsletter newsletter = newsletterService.getNewsletterById(id);
        return ResponseEntity.ok(newsletter);
    }

    @PostMapping("/create")
    public Newsletter createNewsletter(@RequestBody Newsletter newsletter) {
        return newsletterService.saveNewsletter(newsletter);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Newsletter> updateNewsletter(@PathVariable Long id, @RequestBody Newsletter newsletterDetails) {
        Newsletter updatedNewsletter = newsletterService.updateNewsletter(id, newsletterDetails);
        return ResponseEntity.ok(updatedNewsletter);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNewsletter(@PathVariable Long id) {
        newsletterService.deleteNewsletter(id);
        return ResponseEntity.noContent().build();
    }

}
