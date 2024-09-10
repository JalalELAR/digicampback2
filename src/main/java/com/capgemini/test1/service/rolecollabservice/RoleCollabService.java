package com.capgemini.test1.service.rolecollabservice;

import com.capgemini.test1.entities.RoleCollab;
import com.capgemini.test1.repositories.RoleCollabRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class RoleCollabService {
    private RoleCollabRepository roleCollabRepository;

    public RoleCollabService(RoleCollabRepository roleCollabRepository) {
        this.roleCollabRepository = roleCollabRepository;
    }
    public RoleCollab createRoleCollab(String role) {
        RoleCollab roleCollab = new RoleCollab();
        roleCollab.setRole(role);
        return roleCollabRepository.save(roleCollab);
    }
    public RoleCollab getRoleCollabById(Long id) {
        return roleCollabRepository.findById(id).get();
    }
    public List<RoleCollab> getAllRoleCollab() {
        return roleCollabRepository.findAll();
    }
    public RoleCollab saveRoleCollab(RoleCollab roleCollab) {
        return roleCollabRepository.save(roleCollab);
    }
    public void deleteRoleCollabById(Long id) {
        roleCollabRepository.deleteById(id);
    }
    public RoleCollab updateRoleCollab(Long id,RoleCollab roleCollab) {
        RoleCollab roleCollab1 = roleCollabRepository.findById(id).get();
        roleCollab1.setRole(roleCollab.getRole());
        return roleCollabRepository.save(roleCollab);
    }
}
