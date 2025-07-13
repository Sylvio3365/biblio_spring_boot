package com.biblio.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblio.model.Pret;
import com.biblio.model.Rendre;
import com.biblio.repository.RendreRepository;

@Service
public class RendreService {

    @Autowired
    private RendreRepository rendreRepository;

    public Rendre save(Rendre r) {
        return rendreRepository.save(r);
    }

    public String rendrePret(Pret r) {
        LocalDateTime fin = r.getFin();
        LocalDateTime today = LocalDateTime.now().plusHours(3);
        if (today.isAfter(fin)) {
            
        }
        return "✅ Prêt rendu avec succès.";
    }
}
