package com.biblio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.biblio.model.Utilisateur;
import com.biblio.service.UtilisateurService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private UtilisateurService utilisateurService;

    // 📌 Page de connexion
    @GetMapping("/")
    public String showLogin() {
        return "auth/login";
    }

    // 🔐 Traitement de l'authentification
    @PostMapping("/doLogin")
    public String doLogin(@RequestParam String nom,
            @RequestParam String mdp,
            Model model,
            HttpSession session) {
        Utilisateur utilisateur = utilisateurService.findByNomAndMdp(nom, mdp);
        if (utilisateur == null) {
            model.addAttribute("error", "⛔ Nom d'utilisateur ou mot de passe incorrect.");
            return "auth/login";
        }

        // ✅ Stockage en session
        session.setAttribute("utilisateur", utilisateur);
        return "redirect:/goAccueil";
    }

    // 🏠 Page d'accueil après connexion
    @GetMapping("/goAccueil")
    public String goAccueil(HttpSession session, Model model) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) {
            return "redirect:/";
        }
        model.addAttribute("username", utilisateur.getNom());
        model.addAttribute("role", utilisateur.getRole());
        return "page/index";
    }

    // 🚪 Déconnexion
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
