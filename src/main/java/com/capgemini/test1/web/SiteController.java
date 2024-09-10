package com.capgemini.test1.web;

import com.capgemini.test1.entities.Site;
import com.capgemini.test1.service.siteservice.ISiteServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("sites")
public class SiteController {

    private ISiteServiceImpl siteService;
    public SiteController(ISiteServiceImpl siteService) {
        this.siteService = siteService;
    }
    @PostMapping("/create")
    public ResponseEntity<Site> createSite(@RequestParam String name) {
        Site site = siteService.createSite(name);
        return new ResponseEntity<>(site, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public List<Site> getAllSites() {
        return siteService.getAllSites();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Site> getSiteById(@PathVariable Long id) {
        Site site = siteService.getSiteById(id);
        return ResponseEntity.ok(site);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Site> updateSite(@PathVariable Long id, @RequestBody Site siteDetails) {
        Site updatedSite = siteService.updateSite(id, siteDetails);
        return ResponseEntity.ok(updatedSite);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSite(@PathVariable Long id) {
        siteService.deleteSite(id);
        return ResponseEntity.noContent().build();
    }
}
