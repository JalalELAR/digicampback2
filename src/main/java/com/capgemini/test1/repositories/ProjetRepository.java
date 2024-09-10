package com.capgemini.test1.repositories;

import com.capgemini.test1.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjetRepository extends JpaRepository<Projet, Long> {


    // Find project by ID
    //Projet findById(Long id);

    @Query("SELECT p FROM Projet p JOIN p.collaborateurs c WHERE c.id = :collaborateurId")
    List<Projet> findAllByCollaborateurId(@Param("collaborateurId") Long collaborateurId);

    // Find project by title (name)
    @Query("SELECT p FROM Projet p WHERE p.title LIKE %:title%")
    List<Projet> findByTitleContaining(@Param("title") String title);

    // Find projects between two dates
    @Query("SELECT p FROM Projet p WHERE p.dated BETWEEN :startDate AND :endDate")
    List<Projet> findByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    // Dynamic search with multiple criteria
    @Query("SELECT p FROM Projet p " +
            "WHERE (:title IS NULL OR p.title LIKE %:title%) " +
            "AND (:status IS NULL OR p.status = :status) " +
            "AND (:siteName IS NULL OR EXISTS (SELECT 1 FROM p.collaborateurs c WHERE c.site.city = :siteName)) " +
            "AND (:dated IS NULL OR p.dated >= :dated)")
    List<Projet> findByCriteria(
            @Param("title") String title,
            @Param("status") Status status,
            @Param("siteName") String siteName,
            @Param("dated") Date dated);


}
