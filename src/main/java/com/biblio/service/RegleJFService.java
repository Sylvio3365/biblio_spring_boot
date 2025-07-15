package com.biblio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.biblio.model.RegleJF;
import com.biblio.repository.RegleJFRepository;

@Service
public class RegleJFService {

    @Autowired
    private RegleJFRepository regleJFRepository;

    public RegleJF getDerniereRegle() {
        return regleJFRepository.findTopByOrderByIdreglejfDesc();
    }
}
