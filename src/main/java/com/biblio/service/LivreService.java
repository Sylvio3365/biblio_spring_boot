package com.biblio.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblio.model.Etat;
import com.biblio.model.EtatExemplaire;
import com.biblio.model.Exemplaire;
import com.biblio.model.Livre;
import com.biblio.model.utils.ExemplaireDetailDTO;
import com.biblio.model.utils.LivreDetailDT0;
import com.biblio.repository.EtatExemplaireRepository;
import com.biblio.repository.ExemplaireRepository;
import com.biblio.repository.LivreRepository;

@Service
public class LivreService {

    @Autowired
    private ExemplaireRepository exemplaireRepository;
    @Autowired
    private LivreRepository livreRepository;
    @Autowired
    private EtatExemplaireRepository etatExemplaireRepository;

    public LivreDetailDT0 getDetailByIdLivre(int id) throws Exception {
        Long idlivre = Long.valueOf(id);

        // 🔍 Recherche du livre
        Livre livre = livreRepository.findById(idlivre).orElse(null);
        if (livre == null) {
            throw new Exception("❌ Livre introuvable avec l'identifiant : " + idlivre);
        }

        // 🔁 Récupération des exemplaires liés
        List<Exemplaire> exemplaires = exemplaireRepository.findByIdLivre(idlivre);
        LivreDetailDT0 livredetail = new LivreDetailDT0();
        livredetail.setLivre(livre);

        // 📦 Construction des détails d'exemplaire
        List<ExemplaireDetailDTO> exemplairedetail = new ArrayList<>();
        for (Exemplaire ex : exemplaires) {
            EtatExemplaire etat = etatExemplaireRepository
                    .findDernierEtatByExemplaireId(ex.getIdexemplaire());

            if (etat == null) {
                // Par défaut, considéré comme disponible
                etat = new EtatExemplaire();
                etat.setDateheure(LocalDateTime.now());
                etat.setEtat(new Etat(1L, "Disponible")); // ⚠️ Assure-toi que ce constructeur existe
                etat.setExemplaire(ex);
            }

            ExemplaireDetailDTO temp = new ExemplaireDetailDTO(ex, etat.getEtat());
            exemplairedetail.add(temp);
        }

        livredetail.setExemplaireDetail(exemplairedetail);
        return livredetail;
    }

}
