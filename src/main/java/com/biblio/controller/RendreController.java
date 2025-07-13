package com.biblio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.biblio.model.Pret;
import com.biblio.service.PretService;
import com.biblio.service.RendreService;

@Controller
@RequestMapping("/rendre")
public class RendreController {

    @Autowired
    private PretService pretService;
    @Autowired
    private RendreService rendreService;

    @GetMapping("/form")
    public String showForm(Model m) {
        List<Pret> p = pretService.getPretNonRendu();
        m.addAttribute("prets", p);
        return "page/bibliothecaire/rendre";
    }

    @PostMapping("/save")
    public String rendre(@RequestParam("idPret") Long idPret, Model model) {
        // try {
        //     rendreService.rendrePret(idPret); // ou toute ta logique métier ici
        //     model.addAttribute("success", "✅ Prêt rendu avec succès.");
        // } catch (Exception e) {
        //     model.addAttribute("error", "⛔ Erreur lors du rendu : " + e.getMessage());
        // }

        model.addAttribute("prets", pretService.getPretNonRendu());
        return "page/bibliothecaire/rendre";
    }

}
