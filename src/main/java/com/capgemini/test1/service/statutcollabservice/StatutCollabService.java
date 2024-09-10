package com.capgemini.test1.service.statutcollabservice;

import com.capgemini.test1.entities.StatutCollab;
import com.capgemini.test1.repositories.StatutCollabRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Transactional
public class StatutCollabService {
        StatutCollabRepository statutCollabRepository;
        public StatutCollabService(StatutCollabRepository statusCollabRepository) {
            this.statutCollabRepository = statusCollabRepository;
        }
        public StatutCollab createStatutCollab(String Statut) {
            StatutCollab statutCollab = new StatutCollab();
            statutCollab.setStatut(Statut);
            return statutCollabRepository.save(statutCollab);
        }
    public StatutCollab UpdateStatutCollab(Long id,StatutCollab Statut) {
        StatutCollab statutCollab = statutCollabRepository.findById(id).get();
        statutCollab.setStatut(Statut.getStatut());
        return statutCollabRepository.save(statutCollab);
    }
        public List<StatutCollab> getAll() {
            return statutCollabRepository.findAll();
        }
        public StatutCollab getById(Long id) {
            return statutCollabRepository.findById(id).get();
        }
        public StatutCollab save(StatutCollab statutCollab) {
            return statutCollabRepository.save(statutCollab);
        }
        public void delete(StatutCollab statutCollab) {
            statutCollabRepository.delete(statutCollab);
        }
    }

