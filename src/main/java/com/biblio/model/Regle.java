package com.biblio.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "regle")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Regle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idregle;

    private Integer nbjourpret;

    private Integer nbjoursanction;

    private Integer nbjourprolongement;
}
