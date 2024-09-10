package com.capgemini.test1.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class StatutCollab {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String statut;
    @JsonIgnore
    @OneToMany(mappedBy = "statutCollab", fetch = FetchType.LAZY)
    //@OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Collaborateur> collaborateurs = new HashSet<>();
}
