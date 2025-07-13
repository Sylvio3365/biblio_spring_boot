package com.biblio.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "etatexemplaire")
public class EtatExemplaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idetatexemplaire;

    @Column(nullable = false)
    private LocalDateTime dateheure;

    @ManyToOne
    @JoinColumn(name = "idetat", nullable = false)
    private Etat etat;

    @ManyToOne
    @JoinColumn(name = "idexemplaire", nullable = false)
    private Exemplaire exemplaire;
}
