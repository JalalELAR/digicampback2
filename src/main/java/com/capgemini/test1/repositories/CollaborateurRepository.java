package com.capgemini.test1.repositories;

import com.capgemini.test1.entities.Collaborateur;
import com.capgemini.test1.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollaborateurRepository extends JpaRepository<Collaborateur, Long> {

    @Query("SELECT c FROM Collaborateur c WHERE c.nom LIKE %:nom%")
    List<Collaborateur> findByNom(@Param("nom") String nom);

    @Query("SELECT c FROM Collaborateur c WHERE c.email LIKE %:email%")
    List<Collaborateur> findByEmail(@Param("email") String email);

    @Query("SELECT c FROM Collaborateur c WHERE c.site.city LIKE %:siteName%")
    List<Collaborateur> findBySiteName(@Param("siteName") String siteName);

    @Query("SELECT c FROM Collaborateur c WHERE c.grade.GradeName LIKE %:gradeName%")
    List<Collaborateur> findByGradeName(@Param("gradeName") String gradeName);


    @Query("SELECT c FROM Collaborateur c JOIN c.technologies t WHERE t.name LIKE %:technologieName%")
    List<Collaborateur> findByTechnologieName(@Param("technologieName") String technologieName);

    @Query("SELECT c FROM Collaborateur c WHERE c.roleCollab.role LIKE %:role%")
    List<Collaborateur> findByRoleName(@Param("role") String role);

    @Query("SELECT c FROM Collaborateur c WHERE c.statutCollab.statut LIKE %:Statut%")
    List<Collaborateur> findByStatutName(@Param("Statut") String Statut);


    @Query("SELECT c FROM Collaborateur c WHERE (:nom IS NULL OR c.nom LIKE %:nom%) AND " +
            "(:email IS NULL OR c.email LIKE %:email%) AND " +
            "(:siteName IS NULL OR c.site.city LIKE %:siteName%) AND " +
            "(:gradeName IS NULL OR c.grade.GradeName LIKE %:gradeName%) AND " +
            "(:role IS NULL OR c.roleCollab.role LIKE %:role%) AND " +
            "(:Statut IS NULL OR c.statutCollab.statut LIKE %:Statut%)AND " +
            "(:technologieName IS NULL OR EXISTS (SELECT t FROM c.technologies t WHERE t.name LIKE %:technologieName%))"
    )
    List<Collaborateur> findByCriteria(@Param("nom") String nom,
                                       @Param("email") String email,
                                       @Param("siteName") String siteName,
                                       @Param("gradeName") String gradeName,
                                       @Param("technologieName") String technologieName,
                                       @Param("role") String role,
                                       @Param("Statut") String Statut);


}

