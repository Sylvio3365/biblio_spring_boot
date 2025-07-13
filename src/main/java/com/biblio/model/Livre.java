package com.biblio.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "livre")
public class Livre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idlivre;

    private String titre;
    private String auteur;

    @ManyToOne
    @JoinColumn(name = "idreglelivre")
    private RegleLivre regleLivre;
}
