package com.capgemini.test1.repositories;

import com.capgemini.test1.entities.RoleCollab;
import com.capgemini.test1.entities.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleCollabRepository extends JpaRepository<RoleCollab, Long> {

    Optional<RoleCollab> findById(Id roleId);
}

