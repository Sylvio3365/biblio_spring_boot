-- Active: 1752320166646@@127.0.0.1@3306@boky
DROP DATABASE IF EXISTS boky;

CREATE DATABASE IF NOT EXISTS boky;

use boky;

CREATE TABLE role (
    idrole INT AUTO_INCREMENT,
    nom VARCHAR(50),
    PRIMARY KEY (idrole)
);

CREATE TABLE typepret (
    idtypepret INT AUTO_INCREMENT,
    nom VARCHAR(50),
    PRIMARY KEY (idtypepret)
);

CREATE TABLE reglelivre (
    idreglelivre INT AUTO_INCREMENT,
    agemin INT,
    PRIMARY KEY (idreglelivre)
);

CREATE TABLE quota (
    idquotat INT AUTO_INCREMENT,
    pret INT,
    reservation INT,
    prolongement INT,
    PRIMARY KEY (idquotat)
);

CREATE TABLE regle (
    idregle INT AUTO_INCREMENT,
    nbjourpret INT,
    nbjoursanction INT,
    nbjourprolongement INT,
    PRIMARY KEY (idregle)
);

CREATE TABLE statut (
    idstatut INT AUTO_INCREMENT,
    nom VARCHAR(50),
    PRIMARY KEY (idstatut)
);

CREATE TABLE utilisateur (
    idutilisateur INT AUTO_INCREMENT,
    nom VARCHAR(50),
    mdp VARCHAR(50),
    idrole INT NOT NULL,
    PRIMARY KEY (idutilisateur),
    FOREIGN KEY (idrole) REFERENCES role (idrole)
);

CREATE TABLE profil (
    idprofil INT AUTO_INCREMENT,
    nom VARCHAR(50),
    idregle INT NOT NULL,
    idquotat INT NOT NULL,
    PRIMARY KEY (idprofil),
    UNIQUE (idregle),
    UNIQUE (idquotat),
    FOREIGN KEY (idregle) REFERENCES regle (idregle),
    FOREIGN KEY (idquotat) REFERENCES quota (idquotat)
);

CREATE TABLE adherent (
    idadherent INT AUTO_INCREMENT,
    nom VARCHAR(50),
    prenom VARCHAR(50),
    dtn DATE,
    idutilisateur INT NOT NULL,
    idprofil INT NOT NULL,
    PRIMARY KEY (idadherent),
    UNIQUE (idutilisateur),
    FOREIGN KEY (idutilisateur) REFERENCES utilisateur (idutilisateur),
    FOREIGN KEY (idprofil) REFERENCES profil (idprofil)
);

CREATE TABLE livre (
    idlivre INT AUTO_INCREMENT,
    titre VARCHAR(50),
    auteur VARCHAR(50),
    idreglelivre INT,
    PRIMARY KEY (idlivre),
    FOREIGN KEY (idreglelivre) REFERENCES reglelivre (idreglelivre)
);

CREATE TABLE exemplaire (
    idexemplaire INT AUTO_INCREMENT,
    numero VARCHAR(50),
    idtypepret INT NOT NULL,
    idlivre INT NOT NULL,
    PRIMARY KEY (idexemplaire),
    FOREIGN KEY (idtypepret) REFERENCES typepret (idtypepret),
    FOREIGN KEY (idlivre) REFERENCES livre (idlivre)
);

CREATE TABLE pret (
    idpret INT AUTO_INCREMENT,
    debut DATETIME,
    fin DATETIME,
    idexemplaire INT NOT NULL,
    idtypepret INT NOT NULL,
    idadherent INT NOT NULL,
    PRIMARY KEY (idpret),
    FOREIGN KEY (idexemplaire) REFERENCES exemplaire (idexemplaire),
    FOREIGN KEY (idtypepret) REFERENCES typepret (idtypepret),
    FOREIGN KEY (idadherent) REFERENCES adherent (idadherent)
);

CREATE TABLE rendre (
    idrendre INT AUTO_INCREMENT,
    daterendu DATETIME,
    idpret INT NOT NULL,
    PRIMARY KEY (idrendre),
    UNIQUE (idpret),
    FOREIGN KEY (idpret) REFERENCES pret (idpret)
);

CREATE TABLE prolongement (
    idprolongement INT AUTO_INCREMENT,
    nouveaufin DATETIME,
    idpret INT NOT NULL,
    PRIMARY KEY (idprolongement),
    UNIQUE (idpret),
    FOREIGN KEY (idpret) REFERENCES pret (idpret)
);

CREATE TABLE reservation (
    idreservation INT AUTO_INCREMENT,
    datereservation DATETIME,
    datepret DATE,
    idexemplaire INT NOT NULL,
    PRIMARY KEY (idreservation),
    FOREIGN KEY (idexemplaire) REFERENCES exemplaire (idexemplaire)
);

CREATE TABLE statutreservation (
    idstatutreservation INT AUTO_INCREMENT,
    datemodif DATETIME,
    idstatut INT NOT NULL,
    idreservation INT NOT NULL,
    PRIMARY KEY (idstatutreservation),
    FOREIGN KEY (idstatut) REFERENCES statut (idstatut),
    FOREIGN KEY (idreservation) REFERENCES reservation (idreservation)
);

CREATE TABLE prolongementstatut (
    idprolongementstatut INT AUTO_INCREMENT,
    datemodif DATETIME,
    idstatut INT NOT NULL,
    idprolongement INT NOT NULL,
    PRIMARY KEY (idprolongementstatut),
    FOREIGN KEY (idstatut) REFERENCES statut (idstatut),
    FOREIGN KEY (idprolongement) REFERENCES prolongement (idprolongement)
);

CREATE TABLE sanction (
    idsanction INT AUTO_INCREMENT,
    debut DATETIME,
    fin DATETIME,
    idadherent INT NOT NULL,
    PRIMARY KEY (idsanction),
    FOREIGN KEY (idadherent) REFERENCES adherent (idadherent)
);

CREATE TABLE actif (
    idactif INT AUTO_INCREMENT,
    debut DATETIME,
    fin DATETIME,
    idadherent INT NOT NULL,
    PRIMARY KEY (idactif),
    FOREIGN KEY (idadherent) REFERENCES adherent (idadherent)
);

CREATE TABLE etat (
    idetat INT AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL,
    PRIMARY KEY (idetat)
);

CREATE TABLE etatexemplaire (
    idetatexemplaire INT AUTO_INCREMENT,
    dateheure DATETIME NOT NULL,
    idetat INT NOT NULL,
    idexemplaire INT NOT NULL,
    PRIMARY KEY (idetatexemplaire),
    FOREIGN KEY (idetat) REFERENCES etat (idetat),
    FOREIGN KEY (idexemplaire) REFERENCES exemplaire (idexemplaire)
);

CREATE TABLE abonnement (
    idabonnement INT AUTO_INCREMENT,
    debut DATETIME,
    fin DATETIME,
    idadherent INT NOT NULL,
    PRIMARY KEY (idabonnement),
    FOREIGN KEY (idadherent) REFERENCES adherent (idadherent)
);