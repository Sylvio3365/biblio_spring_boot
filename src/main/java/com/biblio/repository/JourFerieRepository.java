package com.biblio.repository;

import com.biblio.model.JourFerie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface JourFerieRepository extends JpaRepository<JourFerie, Integer> {
    boolean existsByDatejf(LocalDate datejf);
}
