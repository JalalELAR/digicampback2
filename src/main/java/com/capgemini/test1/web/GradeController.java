package com.capgemini.test1.web;

import com.capgemini.test1.entities.Grade;
import com.capgemini.test1.service.gradeservice.IGradeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("grades")
public class GradeController {

    private IGradeServiceImpl gradeService;
    public GradeController(IGradeServiceImpl gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping("/create")
    public ResponseEntity<Grade> createGrade(@RequestParam String name) {
        Grade grade = gradeService.createGrade(name);
        return new ResponseEntity<>(grade, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public List<Grade> getAllGrades() {
        return gradeService.getAllGrades();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grade> getGradeById(@PathVariable Long id) {
        Grade grade = gradeService.getGradeById(id);
        return ResponseEntity.ok(grade);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Grade> updateGrade(@PathVariable Long id, @RequestBody Grade gradeDetails) {
        Grade updatedGrade = gradeService.updateGrade(id, gradeDetails);
        return ResponseEntity.ok(updatedGrade);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.noContent().build();
    }
}
