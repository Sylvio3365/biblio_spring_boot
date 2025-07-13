package com.biblio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblio.model.Sanction;
import com.biblio.repository.SanctionRepository;

@Service
public class SanctionService {
    @Autowired
    private SanctionRepository sanctionRepository;

    public Sanction save(Sanction s) {
        return sanctionRepository.save(s);
    }
}
