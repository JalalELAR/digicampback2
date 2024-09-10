package com.capgemini.test1.service.gradeservice;

import com.capgemini.test1.entities.Grade;
import com.capgemini.test1.repositories.GradeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class IGradeServiceImpl  {
    GradeRepository gradeRepository;
    public IGradeServiceImpl(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }
    public Grade createGrade(String name) {
        Grade grade = new Grade();
        grade.setGradeName(name);
        return gradeRepository.save(grade);
    }
    public List<Grade> getAllGrades() {
        return gradeRepository.findAll();
    }

    public Grade getGradeById(Long id) {
        return gradeRepository.findById(id).orElseThrow(() -> new RuntimeException("Grade not found"));
    }

    public Grade saveGrade(Grade grade) {
        return gradeRepository.save(grade);
    }

    public Grade updateGrade(Long id, Grade gradeDetails) {
        Grade grade = getGradeById(id);
        grade.setGradeName(gradeDetails.getGradeName());
        return gradeRepository.save(grade);
    }

    public void deleteGrade(Long id) {
        gradeRepository.deleteById(id);
    }
}
