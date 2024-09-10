package com.capgemini.test1.service.siteservice;

import com.capgemini.test1.entities.Site;
import com.capgemini.test1.repositories.SiteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ISiteServiceImpl  {
    private SiteRepository siteRepository;
    public ISiteServiceImpl(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }
    public Site createSite(String name) {
        Site site = new Site();
        site.setCity(name);
        return siteRepository.save(site);
    }
    public List<Site> getAllSites() {
        return siteRepository.findAll();
    }

    public Site getSiteById(Long id) {
        return siteRepository.findById(id).orElseThrow(() -> new RuntimeException("Site not found"));
    }

    public Site saveSite(Site site) {
        return siteRepository.save(site);
    }

    public Site updateSite(Long id, Site siteDetails) {
        Site site = getSiteById(id);
        site.setCity(siteDetails.getCity());
        return siteRepository.save(site);
    }

    public void deleteSite(Long id) {
        siteRepository.deleteById(id);
    }
}
