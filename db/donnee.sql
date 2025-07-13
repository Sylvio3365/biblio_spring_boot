INSERT INTO role (idrole, nom) VALUES (1, "bibliothecaire");

INSERT INTO role (idrole, nom) VALUES (2, "adherent");

INSERT INTO typepret (idtypepret, nom) VALUES (1, "A domicile");

INSERT INTO typepret (idtypepret, nom) VALUES (2, "Sur place");

INSERT INTO reglelivre (idreglelivre, agemin) VALUES (1, 18);

INSERT INTO
    quota (
        idquotat,
        pret,
        reservation,
        prolongement
    )
VALUES (1, 2, 2, 2);

INSERT INTO
    quota (
        idquotat,
        pret,
        reservation,
        prolongement
    )
VALUES (2, 3, 3, 3);

INSERT INTO
    regle (
        idregle,
        nbjourpret,
        nbjoursanction,
        nbjourprolongement
    )
VALUES (1, 10, 10, 10);

INSERT INTO
    regle (
        idregle,
        nbjourpret,
        nbjoursanction,
        nbjourprolongement
    )
VALUES (2, 15, 15, 15);

INSERT INTO statut (idstatut, nom) VALUES (1, "En attente");

INSERT INTO statut (idstatut, nom) VALUES (2, "Valider");

INSERT INTO statut (idstatut, nom) VALUES (3, "Transformer en pret");

INSERT INTO statut (idstatut, nom) VALUES (4, "Annuler");

INSERT INTO
    utilisateur (
        idutilisateur,
        nom,
        mdp,
        idrole
    )
VALUES (
        1,
        "bibliothecaire",
        "bibliothecaire",
        1
    );

INSERT INTO
    utilisateur (
        idutilisateur,
        nom,
        mdp,
        idrole
    )
VALUES (2, "adherent", "adherent", 2);

INSERT INTO
    utilisateur (
        idutilisateur,
        nom,
        mdp,
        idrole
    )
VALUES (3, "prof", "prof", 2);

INSERT INTO
    profil (
        idprofil,
        nom,
        idregle,
        idquotat
    )
VALUES (1, 'Etudiant', 1, 1);

INSERT INTO
    profil (
        idprofil,
        nom,
        idregle,
        idquotat
    )
VALUES (2, 'Professeur', 2, 2);

INSERT INTO
    adherent (
        idadherent,
        nom,
        prenom,
        dtn,
        idutilisateur,
        idprofil
    )
VALUES (
        1,
        'Jean',
        'Pierre',
        '2000-05-15',
        2,
        1
    );

INSERT INTO
    adherent (
        idadherent,
        nom,
        prenom,
        dtn,
        idutilisateur,
        idprofil
    )
VALUES (
        2,
        'Jule',
        'Rakoto',
        '2000-05-15',
        3,
        2
    );

INSERT INTO
    livre (titre, auteur)
VALUES (
        'Le Petit Prince',
        'Antoine de Saint-Exupéry'
    );

INSERT INTO
    livre (titre, auteur, idreglelivre)
VALUES ('1984', 'George Orwell', 1);

INSERT INTO
    exemplaire (numero, idtypepret, idlivre)
VALUES ('EX001', 1, 1),
    ('EX002', 1, 1);

INSERT INTO
    exemplaire (numero, idtypepret, idlivre)
VALUES ('EX003', 1, 2),
    ('EX004', 1, 2);

INSERT INTO etat (idetat, nom) VALUES (1, "Disponible");

INSERT INTO etat (idetat, nom) VALUES (2, "En pret");

INSERT INTO
    etatexemplaire (
        idetat,
        idexemplaire,
        dateheure
    )
VALUES (1, 1, NOW());

INSERT INTO
    etatexemplaire (
        idetat,
        idexemplaire,
        dateheure
    )
VALUES (2, 2, NOW());

INSERT INTO
    actif (debut, fin, idadherent)
VALUES (
        '2025-06-01 08:00:00',
        '2025-12-31 23:59:59',
        1
    );

INSERT INTO
    abonnement (debut, fin, idadherent)
VALUES (
        '2025-06-01 08:00:00',
        '2025-12-31 23:59:59',
        1
    );

INSERT INTO
    actif (debut, fin, idadherent)
VALUES (
        '2025-06-01 08:00:00',
        '2025-12-31 23:59:59',
        1
    );

INSERT INTO
    sanction (debut, fin, idadherent)
VALUES (
        '2025-06-01 08:00:00',
        '2025-12-31 23:59:59',
        1
    );

SELECT * FROM exemplaire;
SELECT * from etatexemplaire;
