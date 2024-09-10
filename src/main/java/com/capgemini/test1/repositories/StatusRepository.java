package com.capgemini.test1.repositories;

import com.capgemini.test1.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {
}
