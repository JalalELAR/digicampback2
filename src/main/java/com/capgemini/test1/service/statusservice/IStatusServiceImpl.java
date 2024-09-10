package com.capgemini.test1.service.statusservice;

import com.capgemini.test1.entities.Status;
import com.capgemini.test1.repositories.StatusRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class IStatusServiceImpl {

    private StatusRepository statusRepository;
    public IStatusServiceImpl(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }
    public Status createStatus(String name) {
        Status status = new Status();
        status.setName(name);
        return statusRepository.save(status);
    }

    public List<Status> getAllStatuses() {
        return statusRepository.findAll();
    }

    public Status getStatusById(Long id) {
        return statusRepository.findById(id).orElseThrow(() -> new RuntimeException("Status not found"));
    }

    public Status saveStatus(Status status) {
        return statusRepository.save(status);
    }

    public Status updateStatus(Long id, Status statusDetails) {
        Status status = getStatusById(id);
        status.setName(statusDetails.getName());
        return statusRepository.save(status);
    }

    public void deleteStatus(Long id) {
        statusRepository.deleteById(id);
    }
}
