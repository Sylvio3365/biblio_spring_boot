package com.biblio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.biblio.model.Pret;

public interface PretRepository extends JpaRepository<Pret, Long> {
    @Query("""
                SELECT p FROM Pret p
                WHERE p.idpret NOT IN (
                    SELECT r.pret.idpret FROM Rendre r
                )
            """)
    List<Pret> getPretNonRendu();
}