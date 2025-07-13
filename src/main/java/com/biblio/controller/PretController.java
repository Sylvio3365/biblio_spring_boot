package com.biblio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.biblio.model.Adherent;
import com.biblio.model.Exemplaire;
import com.biblio.model.TypePret;
import com.biblio.service.AdherentService;
import com.biblio.service.ExemplaireService;
import com.biblio.service.PretService;
import com.biblio.service.TypePretService;

@Controller
@RequestMapping("/pret")
public class PretController {

    @Autowired
    private AdherentService adherentService;

    @Autowired
    private TypePretService typePretService;

    @Autowired
    private ExemplaireService exemplaireService;

    @Autowired
    private PretService pretService;


    @GetMapping("/form")
    public String newPret(Model model) {
        List<Adherent> adherents = adherentService.findAll();
        List<TypePret> typeprets = typePretService.findAll();
        List<Exemplaire> exemplaires = exemplaireService.findAll();
        model.addAttribute("adherents", adherents);
        model.addAttribute("typeprets", typeprets);
        model.addAttribute("exemplaires", exemplaires);
        return "page/bibliothecaire/pret";
    }

    @PostMapping("/save")
    public String savePret(
            @RequestParam("idAdherent") Long idAdherent,
            @RequestParam("idTypePret") Long idTypePret,
            @RequestParam("idExemplaire") Long idExemplaire,
            Model model) {

        // Pour réaffichage des listes
        model.addAttribute("adherents", adherentService.findAll());
        model.addAttribute("typeprets", typePretService.findAll());
        model.addAttribute("exemplaires", exemplaireService.findAll());

        String resultat = pretService.traiterPret(idAdherent, idTypePret, idExemplaire);
        if (!resultat.startsWith("✅")) {
            model.addAttribute("error", resultat);
        } else {
            model.addAttribute("success", resultat);
        }
        return "page/bibliothecaire/pret";
    }

}
