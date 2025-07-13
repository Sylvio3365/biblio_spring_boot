package com.biblio.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.biblio.model.TypePret;
import com.biblio.repository.TypePretRepository;

@Service
public class TypePretService {

    @Autowired
    private TypePretRepository typePretRepository;

    public List<TypePret> findAll() {
        return typePretRepository.findAll();
    }
}