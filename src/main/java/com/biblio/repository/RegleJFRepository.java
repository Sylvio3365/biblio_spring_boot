package com.biblio.repository;

import com.biblio.model.RegleJF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegleJFRepository extends JpaRepository<RegleJF, Integer> {
    RegleJF findTopByOrderByIdreglejfDesc();
}
