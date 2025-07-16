package com.biblio.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblio.model.Adherent;
import com.biblio.model.utils.AdherentDetailDTO;
import com.biblio.repository.AdherentRepository;

@Service
public class AdherentService {

    @Autowired
    private AdherentRepository adherentRepository;

    public AdherentDetailDTO getDetailAdherent(int id) throws Exception {
        Long idAdherent = Long.valueOf(id);

        // Recherche de l'adhérent
        Adherent adherent = this.findById(idAdherent);
        if (adherent == null) {
            throw new Exception("Adhérent introuvable");
        }

        // Création du DTO de réponse
        AdherentDetailDTO dto = new AdherentDetailDTO();
        dto.setAdherent(adherent);

        // Informations complémentaires
        dto.setEstSanctionner(this.isSanctioned(idAdherent));
        dto.setEstAbonner(adherentRepository.isAbonne(idAdherent));
        dto.setQuota_pret(this.getNbPretNonRenduAdomicile(idAdherent));

        return dto;
    }

    public boolean isSanctioned(Long idadherent) {
        return adherentRepository.isSanctioned(idadherent);
    }

    public boolean isActif(Long idadherent) {
        return adherentRepository.isActif(idadherent);
    }

    public List<Adherent> findAll() {
        return adherentRepository.findAll();
    }

    public Adherent findById(Long idadherent) {
        return adherentRepository.findById(idadherent).orElse(null);
    }

    public int countReservationEnAttente(Long id) {
        return adherentRepository.countReservationEnAttente(id);
    }

    public Adherent findByIdutilisateur(Long iduser) {
        return adherentRepository.findByIdutilisateur(iduser);
    }

    public String checkAdherent(Long idadherent) throws Exception {
        Adherent a = adherentRepository.findById(idadherent).orElse(null);

        if (a == null) {
            throw new Exception("❌ Adhérent non trouvé.");
        }

        boolean estSanctionne = adherentRepository.isSanctioned(idadherent);
        boolean estActif = adherentRepository.isActif(idadherent);
        boolean estAbonne = adherentRepository.isAbonne(idadherent);

        if (estSanctionne) {
            throw new Exception("⛔ Adhérent sanctionné – Prêt refusé.");
        }

        if (!estActif) {
            throw new Exception("⛔ Adhérent inactif – Prêt refusé.");
        }

        if (!estAbonne) {
            throw new Exception("⛔ Adhérent non abonné – Prêt refusé.");
        }

        return "✅ Adhérent valide – Prêt autorisé";
    }

    public String checkAdherentWithDate(Long idadherent, LocalDate date) throws Exception {
        Adherent a = adherentRepository.findById(idadherent).orElse(null);

        if (a == null) {
            throw new Exception("❌ Adhérent non trouvé.");
        }

        // Convertir LocalDate en LocalDateTime à minuit (00:00)
        LocalDateTime dateTime = date.atStartOfDay().plusHours(2);

        boolean estSanctionne = adherentRepository.isSanctionedAtDateTime(idadherent, dateTime);
        boolean estActif = adherentRepository.isActifAtDateTime(idadherent, dateTime);
        boolean estAbonne = adherentRepository.isAbonneAtDateTime(idadherent, dateTime);

        if (estSanctionne) {
            throw new Exception("⛔ Adhérent sanctionné à cette date – Prêt refusé.");
        }

        // if (!estActif) {
        // throw new Exception("⛔ Adhérent inactif à cette date – Prêt refusé.");
        // }

        if (!estAbonne) {
            throw new Exception("⛔ Adhérent non abonné à cette date – Prêt refusé.");
        }

        return "✅ Adhérent valide à cette date – Prêt autorisé";
    }

    public boolean isPretAutorise(Long idadherent) throws Exception {
        String message = checkAdherent(idadherent);
        return message.contains("✅");
    }

    public int getNbPretNonRendu(Long idadherent) {
        return adherentRepository.getNbPretNonRenduAdomicile(idadherent);
    }

    public int getNbPretNonRenduAdomicile(Long idadherent) {
        return adherentRepository.getNbPretNonRenduAdomicile(idadherent);
    }

    public int getNbProlongementEnAttente(Long idadherent) {
        return adherentRepository.getNbProlongementEnAttente(idadherent);
    }
}
