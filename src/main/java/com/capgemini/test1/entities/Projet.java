    package com.capgemini.test1.entities;
    
    import com.fasterxml.jackson.annotation.JsonBackReference;
    import com.fasterxml.jackson.annotation.JsonFormat;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import lombok.ToString;

    import java.util.*;

    @Entity
    @Data @NoArgsConstructor @AllArgsConstructor
    //@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public class Projet {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String title;
        private String description;
        @Lob
        @Column(name = "image", columnDefinition = "LONGBLOB")
        private byte[] image;
        @ManyToOne
        private Status status;
        @Temporal(TemporalType.DATE)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private Date dated;
        @OneToMany(mappedBy = "projet")
        @JsonIgnore
        private Collection<Newsletter> newsletters;
        @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
        private Collection<Collaborateur> collaborateurs ;
        /*@JoinTable(
                name = "project_collaborator",
                joinColumns = @JoinColumn(name = "project_id"),
                inverseJoinColumns = @JoinColumn(name = "collaborator_id")
        )*/

    
    }
