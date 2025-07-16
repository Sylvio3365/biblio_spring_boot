package com.biblio.model.utils;

import com.biblio.model.Etat;
import com.biblio.model.Exemplaire;

public class ExemplaireDetailDTO {
    private Exemplaire exemplaire;
    private Etat etat;

    public ExemplaireDetailDTO(Exemplaire exemplaire, Etat etat) {
        this.exemplaire = exemplaire;
        this.etat = etat;
    }

    public ExemplaireDetailDTO() {
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public Exemplaire getExemplaire() {
        return exemplaire;
    }

    public void setExemplaire(Exemplaire exemplaire) {
        this.exemplaire = exemplaire;
    }

}
