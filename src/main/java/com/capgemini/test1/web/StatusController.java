package com.capgemini.test1.web;

import com.capgemini.test1.entities.Status;
import com.capgemini.test1.service.statusservice.IStatusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("status")
public class StatusController {

    private IStatusServiceImpl statusService;
    public StatusController(IStatusServiceImpl statusService) {
        this.statusService = statusService;
    }
    @PostMapping("/create")
    public ResponseEntity<Status> createStatus(@RequestParam String name) {
        Status status = statusService.createStatus(name);
        return new ResponseEntity<>(status, HttpStatus.CREATED);
    }
    @GetMapping("/list")
    public List<Status> getAllStatuses() {
        return statusService.getAllStatuses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Status> getStatusById(@PathVariable Long id) {
        Status status = statusService.getStatusById(id);
        return ResponseEntity.ok(status);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Status> updateStatus(@PathVariable Long id, @RequestBody Status statusDetails) {
        Status updatedStatus = statusService.updateStatus(id, statusDetails);
        return ResponseEntity.ok(updatedStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatus(@PathVariable Long id) {
        statusService.deleteStatus(id);
        return ResponseEntity.noContent().build();
    }
}