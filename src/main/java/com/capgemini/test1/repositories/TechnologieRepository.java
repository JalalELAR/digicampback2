package com.capgemini.test1.repositories;

import com.capgemini.test1.entities.Technologie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechnologieRepository extends JpaRepository<Technologie, Long> {
    Technologie findByName(String name);
}
