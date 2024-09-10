package com.capgemini.test1.repositories;

import com.capgemini.test1.entities.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade,Long> {
}
