-- Création de la base si besoin
CREATE DATABASE IF NOT EXISTS genioservice_db;
USE genioservice_db;

-- Suppression si déjà existante (utile en dev)
DROP TABLE IF EXISTS utilisateur;

-- Création de la table utilisateur
CREATE TABLE utilisateur (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     actif TINYINT(1) NOT NULL DEFAULT 1,
     created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
     email VARCHAR(255) NOT NULL UNIQUE,
     mot_de_passe VARCHAR(255) NOT NULL,
     nom VARCHAR(255),
     prenom VARCHAR(255),
     role VARCHAR(50),
     updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
