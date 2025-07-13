package com.biblio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.biblio.model.Adherent;
import com.biblio.model.TypePret;
import com.biblio.service.AdherentService;
import com.biblio.service.TypePretService;

@Controller
@RequestMapping("/pret")
public class PretController {

    @Autowired
    private AdherentService adherentService;

    @Autowired
    private TypePretService typePretService;

    @GetMapping("/form")
    public String newPret(Model model) {
        List<Adherent> adherents = adherentService.findAll();
        List<TypePret> typeprets = typePretService.findAll();
        model.addAttribute("adherents", adherents);
        model.addAttribute("typeprets", typeprets);
        return "page/bibliothecaire/pret";
    }
}
