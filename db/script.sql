CREATE DATABASE IF NOT EXISTS boky;

use boky;

CREATE TABLE role(
   idrole INT AUTO_INCREMENT,
   nom VARCHAR(50),
   PRIMARY KEY(idrole)
);

INSERT INTO role (idrole, nom) VALUES (1, 'bibliothecaire');
INSERT INTO role (idrole, nom) VALUES (2, 'adherent');

CREATE TABLE utilisateur(
   idutilisateur INT AUTO_INCREMENT,
   nom VARCHAR(50),
   mdp VARCHAR(50),
   idrole INT NOT NULL,
   PRIMARY KEY(idutilisateur),
   FOREIGN KEY(idrole) REFERENCES role(idrole)
);
