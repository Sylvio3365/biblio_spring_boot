-- Active: 1752320166646@@127.0.0.1@3306@boky
DROP DATABASE IF EXISTS boky;

CREATE DATABASE IF NOT EXISTS boky;

use boky;

CREATE TABLE role (
    idrole INT NOT NULL AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL,
    PRIMARY KEY (idrole)
);

INSERT INTO role (idrole, nom) VALUES (1, 'bibliothecaire');

INSERT INTO role (idrole, nom) VALUES (2, 'adherent');

CREATE TABLE utilisateur (
    idutilisateur INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    mdp VARCHAR(50) NOT NULL,
    idrole INT NOT NULL,
    FOREIGN KEY (idrole) REFERENCES role (idrole)
);