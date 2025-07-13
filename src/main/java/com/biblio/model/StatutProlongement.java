package com.biblio.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
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
