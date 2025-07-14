package com.biblio.model.utils;

import com.biblio.model.Pret;
import com.biblio.model.Prolongement;
import com.biblio.model.StatutProlongement;

public class PretAvecProlongementDTO {
    private Pret pret;
    private Prolongement prolongement;
    private StatutProlongement statutProlongement;

    public PretAvecProlongementDTO(Pret pret, Prolongement prolongement, StatutProlongement statutProlongement) {
        this.pret = pret;
        this.prolongement = prolongement;
        this.statutProlongement = statutProlongement;
    }

    public Pret getPret() {
        return pret;
    }

    public void setPret(Pret pret) {
        this.pret = pret;
    }

    public Prolongement getProlongement() {
        return prolongement;
    }

    public void setProlongement(Prolongement prolongement) {
        this.prolongement = prolongement;
    }

    public StatutProlongement getStatutProlongement() {
        return statutProlongement;
    }

    public void setStatutProlongement(StatutProlongement statutProlongement) {
        this.statutProlongement = statutProlongement;
    }

}
