package com.capgemini.test1.repositories;

import com.capgemini.test1.entities.Site;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site, Long> {
}
