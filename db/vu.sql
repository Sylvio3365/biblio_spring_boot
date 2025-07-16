CREATE VIEW vue_quota AS
SELECT u.nom AS "ADHERENT", q.pret AS "LIVRES EMPRUNTES", r.nbjourpret AS "JOURS PRET", q.reservation AS "RESERVATION LIVRES", q.prolongement AS "PROLONGEMENT PRET"
FROM
    adherent a
    JOIN utilisateur u ON a.idutilisateur = u.idutilisateur
    JOIN profil p ON a.idprofil = p.idprofil
    JOIN regle r ON p.idregle = r.idregle
    JOIN quota q ON p.idquotat = q.idquotat;

CREATE VIEW vue_jours_feries AS
SELECT DATE_FORMAT(datejf, '%d/%m/%Y') AS "Jour férié"
FROM jourferie
ORDER BY datejf;

CREATE VIEW vue_penalites AS
SELECT
    u.nom AS "ADHERENT",
    CASE
        WHEN r.nbjoursanction IS NOT NULL THEN r.nbjoursanction
        ELSE 0
    END AS "NOMBRE DE JOURS DE PENALITE"
FROM
    adherent a
    JOIN utilisateur u ON a.idutilisateur = u.idutilisateur
    JOIN profil p ON a.idprofil = p.idprofil
    JOIN regle r ON p.idregle = r.idregle;  