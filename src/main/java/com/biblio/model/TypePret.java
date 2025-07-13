package com.biblio.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "typepret")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypePret {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idtypepret;

    @Column(nullable = false, length = 50)
    private String nom;
}
