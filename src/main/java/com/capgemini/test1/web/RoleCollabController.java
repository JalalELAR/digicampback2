package com.capgemini.test1.web;

import com.capgemini.test1.entities.RoleCollab;
import com.capgemini.test1.entities.StatutCollab;
import com.capgemini.test1.service.rolecollabservice.RoleCollabService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rolescollab")
public class RoleCollabController {
    private RoleCollabService roleCollabService;

    public RoleCollabController(RoleCollabService roleCollabService) {
        this.roleCollabService = roleCollabService;
    }

    @PostMapping("/create")
    public ResponseEntity<RoleCollab> createRoleCollab(@RequestParam String role) {
        RoleCollab rolecollab = roleCollabService.createRoleCollab(role);
        return new ResponseEntity<>(rolecollab, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<RoleCollab> updateRoleCollab(@PathVariable Long id,@RequestBody RoleCollab roleCollab) {
        RoleCollab rolecollab = roleCollabService.updateRoleCollab(id, roleCollab);
        return  ResponseEntity.ok(rolecollab);
    }
    @GetMapping("/{id}")
    public ResponseEntity<RoleCollab> getRoleCollab(@PathVariable Long id) {
        RoleCollab roleCollab = roleCollabService.getRoleCollabById(id);
        return new ResponseEntity<>(roleCollab, HttpStatus.OK);
    }
    @GetMapping("/list")
    public List<RoleCollab> getAllRoleCollab() {
        return roleCollabService.getAllRoleCollab();
    }
    @DeleteMapping("/{id}")
    public void deleteRoleCollab(@PathVariable Long id) {
        roleCollabService.deleteRoleCollabById(id);
    }


}
