-- Fichier d'initialisation MySQL : init.sql

USE genio_db;

INSERT IGNORE INTO utilisateur (id, actif, created_at, email, mot_de_passe, nom, prenom, role, updated_at) VALUES
-- GESTIONNAIRE
(1001, 1, NOW(), 'jean.dupond@example.com',
  '$2a$10$90yfR7/5dcRZQJJ76CU1du3f5fyaeKDX41TVLJO1czaMb30A2O3lO',
  'Dupont', 'Jean', 'GESTIONNAIRE', NOW()),

-- EXPLOITANT
(1002, 1, NOW(), 'elsa.simha.hadjadj@gmail.com',
  '$2a$10$2PzyRey0QtfOCaxvUo9BZOPRDVZF.jx4ErzxzhNQAwT/adG1iFxF2',
  'Martin', 'Clara', 'EXPLOITANT', NOW()),

-- CONSULTANT
(1003, 1, NOW(), 'jean.dupont@example.com',
  '$2a$10$90yfR7/5dcRZQJJ76CU1du3f5fyaeKDX41TVLJO1czaMb30A2O3lO',
  'Leroy', 'Thomas', 'CONSULTANT', NOW());
