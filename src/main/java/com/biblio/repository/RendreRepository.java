package com.biblio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.biblio.model.Rendre;

public interface RendreRepository extends JpaRepository<Rendre, Long> {
    
}