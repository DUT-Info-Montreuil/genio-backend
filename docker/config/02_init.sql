-- Fichier d'initialisation MySQL : init.sql

USE genio_db;

INSERT IGNORE INTO utilisateur (id, actif, created_at, email, mot_de_passe, nom, prenom, role, updated_at) VALUES
-- GESTIONNAIRE : mot de pase en clair : Genio2025!@A
(1001, 1, NOW(), 'alice.martin@genio.com',
  '$2a$10$nlIpNm4gyHWI/sEqjAifSe512IVY2UWM1AxcyNT//D.efM/XHUPRC',
  'Martin', 'Alice', 'GESTIONNAIRE', NOW()),

-- EXPLOITANT : mot de passe en clair : StrongPass#22
(1002, 1, NOW(), 'bruno.leroy@genio.com',
  '$2a$10$RLB6.caGKci0oTCLqalfb.iiw7CLv5q4BUUAca3zUV0v58Y8gWo/a',
  'Leroy', 'Bruno', 'EXPLOITANT', NOW()),

-- CONSULTANT : mot de passe en clair : Cl@raSecure99
(1003, 1, NOW(), 'clara.dubois@genio.com',
  '$2a$10$PubCOI.vsAm.1zkymcuHOOJaM8g/IlKQNO61vWeHCVqMSNgNCF7dO',
  'Dubois', 'Clara', 'CONSULTANT', NOW());
