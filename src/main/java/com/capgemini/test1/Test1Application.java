package com.capgemini.test1;

import com.capgemini.test1.entities.*;
import com.capgemini.test1.repositories.CollaborateurRepository;
import com.capgemini.test1.repositories.StatusRepository;
import com.capgemini.test1.repositories.StatutCollabRepository;
import com.capgemini.test1.repositories.UserRepository;
import com.capgemini.test1.service.collaborateurservice.ICollaborateurServiceImpl;
import com.capgemini.test1.service.gradeservice.IGradeServiceImpl;
import com.capgemini.test1.service.projetservice.IProjetServiceImpl;
import com.capgemini.test1.service.siteservice.ISiteServiceImpl;
import com.capgemini.test1.service.statusservice.IStatusServiceImpl;
import com.capgemini.test1.service.statutcollabservice.StatutCollabService;
import com.capgemini.test1.service.technologieservice.ITechnologieServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Date;


@SpringBootApplication
public class Test1Application {
  @Autowired
  private static ICollaborateurServiceImpl iCollaborateurServiceImpl;
  @Autowired
  private static StatutCollabService statutCollabServiceImpl;

    public static void main(String[] args) {
        SpringApplication.run(Test1Application.class, args);
    }
    /*
    public void createAdminAccount() {
        if (userRepository.findByRole("ADMIN").isEmpty()) {
            User admin = new User();
            admin.setFullName("jalal ELARACHE");
            admin.setEmail("jalal.elarache@capgemini.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");

            userRepository.save(admin);

            System.out.println("Admin account created with email: admin@example.com and password: admin123");
        }*/
    }

