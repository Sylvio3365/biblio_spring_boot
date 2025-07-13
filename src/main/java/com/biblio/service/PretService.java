package com.biblio.service;

import com.biblio.model.Pret;
import com.biblio.repository.PretRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PretService {

    @Autowired
    private PretRepository pretRepository;

    public Pret save(Pret pret) {
        return pretRepository.save(pret);
    }
}
