-- Création de la base si besoin
CREATE DATABASE IF NOT EXISTS genio_db;
USE genio_db;

-- Création de la table utilisateur
CREATE TABLE IF NOT EXISTS utilisateur (
    id BIGINT PRIMARY KEY,
    actif TINYINT(1) NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    email VARCHAR(255) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    nom VARCHAR(255),
    prenom VARCHAR(255),
    role VARCHAR(50),
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Création de l'utilisateur MySQL et droits
CREATE USER IF NOT EXISTS 'genio'@'%' IDENTIFIED BY 'change-me';
GRANT ALL PRIVILEGES ON genio_db.* TO 'genio'@'%';
FLUSH PRIVILEGES;
