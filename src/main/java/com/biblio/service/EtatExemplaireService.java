package com.biblio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblio.model.EtatExemplaire;
import com.biblio.repository.EtatExemplaireRepository;

@Service
public class EtatExemplaireService {

    @Autowired
    private EtatExemplaireRepository etatExemplaireRepository;

    public EtatExemplaire save(EtatExemplaire e) {
        return etatExemplaireRepository.save(e);
    }
}
