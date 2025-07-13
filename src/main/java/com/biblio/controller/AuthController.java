package com.biblio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.biblio.model.Utilisateur;
import com.biblio.service.UtilisateurService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping("/")
    public String showLogin() {
        return "auth/login";
    }

    @PostMapping("/doLogin")
    public String doLogin(@RequestParam String nom, @RequestParam String mdp, Model m, HttpSession session) {
        Utilisateur utilisateur = utilisateurService.findByNomAndMdp(nom, mdp);
        if (utilisateur == null) {
            String error = "Nom d'utilisateur ou mot de passe incorrect";
            m.addAttribute("error", error);
            return "auth/login";
        }
        m.addAttribute("username", utilisateur.getNom());
        m.addAttribute("role", utilisateur.getRole());
        return "page/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

}
