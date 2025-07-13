package com.biblio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "prolongementstatut")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatutProlongement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idprolongementstatut;

    private LocalDateTime datemodif;

    @ManyToOne
    @JoinColumn(name = "idstatut", nullable = false)
    private Statut statut;

    @ManyToOne
    @JoinColumn(name = "idprolongement", nullable = false)
    private Prolongement prolongement;
}
