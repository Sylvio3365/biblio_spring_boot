INSERT INTO role (idrole, nom) VALUES (1, "bibliothecaire");

INSERT INTO role (idrole, nom) VALUES (2, "adherent");

INSERT INTO typepret (idtypepret, nom) VALUES (1, "A domicile");

INSERT INTO typepret (idtypepret, nom) VALUES (2, "Sur place");

INSERT INTO
    quota (
        idquotat,
        pret,
        reservation,
        prolongement
    )
VALUES (1, 2, 1, 3);

INSERT INTO
    quota (
        idquotat,
        pret,
        reservation,
        prolongement
    )
VALUES (2, 3, 2, 5);

INSERT INTO
    quota (
        idquotat,
        pret,
        reservation,
        prolongement
    )
VALUES (3, 4, 3, 7);

INSERT INTO
    regle (
        idregle,
        nbjourpret,
        nbjoursanction,
        nbjourprolongement
    )
VALUES (1, 7, 10, 10);

INSERT INTO
    regle (
        idregle,
        nbjourpret,
        nbjoursanction,
        nbjourprolongement
    )
VALUES (2, 9, 9, 10);

INSERT INTO
    regle (
        idregle,
        nbjourpret,
        nbjoursanction,
        nbjourprolongement
    )
VALUES (3, 12, 8, 10);

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
        'bibliothecaire',
        'bibliothecaire',
        1
    ),
    (2, 'ETU001', 'ETU001', 2),
    (3, 'ETU002', 'ETU002', 2),
    (4, 'ETU003', 'ETU003', 2),
    (5, 'ENS001', 'ENS001', 2),
    (6, 'ENS002', 'ENS002', 2),
    (7, 'ENS003', 'ENS003', 2),
    (8, 'PROF001', 'PROF001', 2),
    (9, 'PROF002', 'PROF002', 2);

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
VALUES (2, 'Enseignant', 2, 2);

INSERT INTO
    profil (
        idprofil,
        nom,
        idregle,
        idquotat
    )
VALUES (3, 'Professionnel', 3, 3);

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
        'ETU001',
        'Amine Bensaïd',
        '2001-01-15',
        2,
        1
    ),
    (
        2,
        'ETU002',
        'Sarah El Khattabi',
        '2002-02-20',
        3,
        1
    ),
    (
        3,
        'ETU003',
        'Youssef Moujahid',
        '2001-06-30',
        4,
        1
    ),
    (
        4,
        'ENS001',
        'Nadia Benali',
        '1985-03-25',
        5,
        2
    ),
    (
        5,
        'ENS002',
        'Karim Haddadi',
        '1979-11-10',
        6,
        2
    ),
    (
        6,
        'ENS003',
        'Salima Touhami',
        '1988-07-07',
        7,
        2
    ),
    (
        7,
        'PROF001',
        'Rachid El Mansouri',
        '1975-09-12',
        8,
        3
    ),
    (
        8,
        'PROF002',
        'Amina Zerouali',
        '1982-04-18',
        9,
        3
    );

INSERT INTO
    abonnement (
        idabonnement,
        debut,
        fin,
        idadherent
    )
VALUES (
        1,
        '2025-02-01',
        '2025-07-24',
        1
    ), -- ETU001
    (
        2,
        '2025-02-01',
        '2025-07-01',
        2
    ), -- ETU002
    (
        3,
        '2025-04-01',
        '2025-12-01',
        3
    ), -- ETU003
    (
        4,
        '2025-07-01',
        '2026-07-01',
        4
    ), -- ENS001
    (
        5,
        '2025-08-01',
        '2026-05-01',
        5
    ), -- ENS002
    (
        6,
        '2025-07-01',
        '2026-06-01',
        6
    ), -- ENS003
    (
        7,
        '2025-06-01',
        '2025-12-01',
        7
    ), -- PROF001
    (
        8,
        '2024-10-01',
        '2025-06-01',
        8
    );
-- PROF002

INSERT INTO
    livre (
        idlivre,
        titre,
        auteur,
        idreglelivre
    )
VALUES (
        1,
        'Les Misérables',
        'Victor Hugo',
        NULL
    ),
    (
        2,
        'L''Étranger',
        'Albert Camus',
        NULL
    ),
    (
        3,
        'Harry Potter à l''école des sorciers',
        'J.K. Rowling',
        NULL
    );

INSERT INTO
    exemplaire (
        idexemplaire,
        numero,
        idtypepret,
        idlivre
    )
VALUES (1, 'MIS001', 1, 1),
    (2, 'MIS002', 1, 1),
    (3, 'MIS003', 1, 1),
    (4, 'ETR001', 1, 2),
    (5, 'ETR002', 1, 2),
    (6, 'HAR001', 1, 3);

INSERT INTO jourferie (idjourferie, datejf) VALUES (1, '2025-07-26');

INSERT INTO jourferie (idjourferie, datejf) VALUES (2, '2025-07-19');

INSERT INTO etat (idetat, nom) VALUES (1, "Disponible");

INSERT INTO etat (idetat, nom) VALUES (2, "En pret");

delete from abonnement where idadherent = 2;

delete from abonnement where idadherent = 8;

delete from abonnement where idadherent = 5;

UPDATE regle SET nbjourprolongement = 7 WHERE idregle = 1;

UPDATE regle SET nbjourprolongement = 9 WHERE idregle = 2;

UPDATE regle SET nbjourprolongement = 12 WHERE idregle = 3;

INSERT INTO reglejf (comportement) VALUES (1);