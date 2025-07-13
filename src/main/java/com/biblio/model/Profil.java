package com.biblio.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profil")
public class Profil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idprofil;

    private String nom;

    @OneToOne
    @JoinColumn(name = "idregle", unique = true)
    private Regle regle;

    @OneToOne
    @JoinColumn(name = "idquotat", unique = true)
    private Quota quota;
}
