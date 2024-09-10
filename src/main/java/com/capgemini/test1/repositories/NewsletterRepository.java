package com.capgemini.test1.repositories;

import com.capgemini.test1.entities.Newsletter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NewsletterRepository extends JpaRepository<Newsletter,Long> {
    @Modifying
    @Query("UPDATE Newsletter n SET n.projet.id = NULL WHERE n.projet.id = :projetId")
    void updateProjetIdToNull(Long projetId);
}
