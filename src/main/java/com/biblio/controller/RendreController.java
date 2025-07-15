package com.biblio.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.biblio.model.Pret;
import com.biblio.model.Prolongement;
import com.biblio.model.StatutProlongement;
import com.biblio.model.Utilisateur;
import com.biblio.model.utils.PretAvecProlongementDTO;
import com.biblio.service.PretService;
import com.biblio.service.ProlongementService;
import com.biblio.service.RendreService;
import com.biblio.service.StatutProlongementService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/rendre")
public class RendreController {

    @Autowired
    private PretService pretService;

    @Autowired
    private RendreService rendreService;

    @Autowired
    private StatutProlongementService statutProlongementService;

    @Autowired
    private ProlongementService prolongementService;

    @GetMapping("/form")
    public String showForm(Model model, HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) {
            return "redirect:/";
        }
        model.addAttribute("username", utilisateur.getNom());
        model.addAttribute("role", utilisateur.getRole());

        List<Pret> prets = pretService.getPretNonRendu();
        List<PretAvecProlongementDTO> details = new ArrayList<>();

        for (Pret p : prets) {
            Prolongement prolongement = prolongementService.findByIdPret(p.getIdpret());
            StatutProlongement statut = null;

            if (prolongement != null) {
                statut = statutProlongementService.findDernierStatutByIdProlongement(prolongement.getIdprolongement());
            }

            PretAvecProlongementDTO dto = new PretAvecProlongementDTO(p, prolongement, statut);
            details.add(dto);
        }

        model.addAttribute("details", details);
        return "page/bibliothecaire/rendre";
    }

    @PostMapping("/save")
    public String rendre(@RequestParam("idPret") Long idPret, Model model, HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) {
            return "redirect:/";
        }
        model.addAttribute("username", utilisateur.getNom());
        model.addAttribute("role", utilisateur.getRole());

        Pret p = pretService.findById(idPret);
        String message = rendreService.rendrePret(p);

        List<Pret> prets = pretService.getPretNonRendu();
        List<PretAvecProlongementDTO> details = new ArrayList<>();

        for (Pret pret : prets) {
            Prolongement prolongement = prolongementService.findByIdPret(pret.getIdpret());
            StatutProlongement statut = null;
            if (prolongement != null) {
                statut = statutProlongementService.findDernierStatutByIdProlongement(prolongement.getIdprolongement());
            }
            details.add(new PretAvecProlongementDTO(pret, prolongement, statut));
        }

        model.addAttribute("message", message);
        model.addAttribute("details", details);
        return "page/bibliothecaire/rendre";
    }
}
