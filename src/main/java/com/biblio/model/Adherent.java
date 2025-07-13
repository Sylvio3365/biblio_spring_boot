package com.biblio.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "adherent")
public class Adherent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idadherent;

    private String nom;
    private String prenom;

    private LocalDate dtn;

    @OneToOne
    @JoinColumn(name = "idutilisateur", unique = true)
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "idprofil")
    private Profil profil;

    public int getAge() {
        if (dtn == null) {
            return 0;
        }
        return Period.between(dtn, LocalDate.now()).getYears();
    }
}
