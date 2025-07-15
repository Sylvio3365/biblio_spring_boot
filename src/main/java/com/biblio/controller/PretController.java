package com.biblio.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.biblio.model.Adherent;
import com.biblio.model.Exemplaire;
import com.biblio.model.Pret;
import com.biblio.model.TypePret;
import com.biblio.model.Utilisateur;
import com.biblio.model.utils.PretAvecProlongementDTO;
import com.biblio.service.AdherentService;
import com.biblio.service.ExemplaireService;
import com.biblio.service.PretService;
import com.biblio.service.TypePretService;

import jakarta.servlet.http.HttpSession;

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

    @GetMapping("/mes_prets")
    public String mesPrets(Model model, HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) {
            return "redirect:/";
        }
        model.addAttribute("username", utilisateur.getNom());
        model.addAttribute("role", utilisateur.getRole());

        try {
            List<PretAvecProlongementDTO> mesprets = pretService
                    .getPretNonRenduAvecProlongement(utilisateur.getIdutilisateur());
            model.addAttribute("mesprets", mesprets);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        return "page/adherent/mes_prets";
    }

    @GetMapping("/form")
    public String newPret(Model model, HttpSession session) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) {
            return "redirect:/";
        }
        model.addAttribute("username", utilisateur.getNom());
        model.addAttribute("role", utilisateur.getRole());

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
            @RequestParam("datePret") LocalDate datePret,
            Model model,
            HttpSession session) {

        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) {
            return "redirect:/";
        }
        model.addAttribute("username", utilisateur.getNom());
        model.addAttribute("role", utilisateur.getRole());

        model.addAttribute("adherents", adherentService.findAll());
        model.addAttribute("typeprets", typePretService.findAll());
        model.addAttribute("exemplaires", exemplaireService.findAll());

        try {
            String resultat = pretService.traiterPretAvecDate(idAdherent, idTypePret, idExemplaire, datePret);
            model.addAttribute("success", resultat);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        return "page/bibliothecaire/pret";
    }
}
