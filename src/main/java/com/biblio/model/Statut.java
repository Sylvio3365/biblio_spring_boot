package com.biblio.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "statut")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Statut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idstatut;

    private String nom;
}
