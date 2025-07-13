package com.biblio.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reglelivre")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegleLivre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idreglelivre;

    private Integer agemin;
}
