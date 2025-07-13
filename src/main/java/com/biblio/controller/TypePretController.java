package com.biblio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblio.model.TypePret;
import com.biblio.service.TypePretService;

@RestController
@RequestMapping("/typepret")
public class TypePretController {
    @Autowired
    private TypePretService typePretService;

    @GetMapping
    public List<TypePret> findall() {
        return typePretService.findAll();
    }
}
