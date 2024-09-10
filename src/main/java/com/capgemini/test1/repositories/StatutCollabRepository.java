package com.capgemini.test1.repositories;

import com.capgemini.test1.entities.RoleCollab;
import com.capgemini.test1.entities.StatutCollab;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatutCollabRepository extends JpaRepository<StatutCollab, Long> {


    Optional<StatutCollab> findById(Id statutId);

}
