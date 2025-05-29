# Introduction – Projet GenioService

Ce projet a été développé dans le cadre de mon **semestre 5** du BUT Informatique à l’**IUT de Montreuil**.

L’objectif principal de **GenioService** est double :

1. **Générer automatiquement des conventions de stage** à partir de données envoyées par une autre application (outil de gestion des stages).
2. **Permettre la gestion complète des modèles de convention** via une interface web dédiée, avec des droits d’accès spécifiques selon le rôle de l’utilisateur.

---

##  Deux volets fonctionnels complémentaires

### 1. Un service de génération de conventions `.docx`

GenioService propose un **service REST autonome**, qui :
- reçoit des **données structurées** (nom de l’étudiant, dates de stage, entreprise, etc.),
- les injecte dans un **modèle de convention au format `.docx`**,
- génère dynamiquement le document,
- et le **renvoie encodé en BASE64** à l’application appelante.

Ce service fonctionne en arrière-plan, sans interface utilisateur. Il est utilisé par un outil externe qui appelle l’API avec les données nécessaires, puis récupère le fichier généré.

### 2. Une interface web pour la gestion des modèles et des utilisateurs

L’application comprend aussi une **interface Angular** permettant :

- de **gérer les modèles de convention** : ajouter, modifier, archiver des fichiers `.docx`,
- de **consulter les modèles existants** selon différents filtres,
- de **visualiser l’historique des générations**,
- de **gérer les utilisateurs** (rôle Gestionnaire uniquement).

---

## Trois rôles utilisateurs bien définis

L’application repose sur un système de rôles, chacun ayant des permissions différentes :

- **Consultant** : peut uniquement **consulter les modèles** disponibles.
- **Exploitant** : peut **consulter les modèles** et **accéder à l’historique des conventions générées**.
- **Gestionnaire** : a un accès complet :
  - **ajout, modification, archivage de modèles**,
  - **gestion des utilisateurs** (activation, suppression, attribution de rôles),
  - **visualisation complète de l’historique**.

Chaque rôle est identifié en haut de l’interface, et les fonctionnalités sont **affichées ou masquées** dynamiquement en fonction des droits.

---

## Technologies utilisées

- **Frontend** : Angular 17
  - Design responsive (sauf tableaux complexes)
  - Accessibilité conforme WCAG (lecteurs d’écran, navigation clavier)
  - UX fluide et composants interactifs
- **Backend** : Spring Boot (Java 17)
  - API REST sécurisée
  - Génération de fichiers `.docx` avec injection de données
  - Encodage et envoi des fichiers en base64
  - Gestion des rôles et des accès

---

## Projet universitaire à fort enjeu technique

Ce projet m’a permis de :
- concevoir un **service de génération de documents automatisé**,
- développer une **interface complète de gestion d’utilisateurs et de modèles**,
- mettre en œuvre une **séparation claire des rôles** et des droits,
- respecter des normes professionnelles (accessibilité, sécurité, UX/UI),
- et travailler sur un **projet structuré de bout en bout**, intégrant à la fois **frontend** et **backend**.

---

**En résumé**, GenioService est une solution modulaire et robuste qui centralise la gestion de modèles de conventions, tout en automatisant leur génération à partir de données externes.