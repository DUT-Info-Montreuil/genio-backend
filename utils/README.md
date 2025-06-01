## Classes utilitaires pour tests et debug

Le projet contient deux contrôleurs REST dédiés aux opérations utilitaires liées aux utilisateurs et à la gestion des mots de passe.

### 1. CreateUserController

- **URL de base :** `/test`
- **Fonctionnalités :**
    - **Créer un utilisateur de test**
        - **Méthode :** POST `/test/create-user`
        - **Paramètres requis :** `email`, `password`, `role`
        - **Description :** Crée un utilisateur avec un mot de passe hashé (BCrypt), un nom et prénom fixes ("Test User"), et un rôle donné.
    - **Mettre à jour le mot de passe d’un utilisateur**
        - **Méthode :** PUT `/test/update-password`
        - **Paramètres requis :** `email`, `newPassword`
        - **Description :** Recherche un utilisateur par email, met à jour son mot de passe hashé avec la nouvelle valeur, puis sauvegarde en base.

Ce contrôleur facilite la création rapide d’utilisateurs pour les tests d’authentification et de rôles dans l’application.

---

### 2. PasswordDebugController

- **URL de base :** `/debug`
- **Fonctionnalité :**
    - **Vérifier un mot de passe en clair contre un hash BCrypt**
        - **Méthode :** GET `/debug/check`
        - **Paramètres :** `raw` (mot de passe en clair), `hash` (mot de passe hashé)
        - **Description :** Retourne `true` si le mot de passe correspond au hash, `false` sinon.

Ce contrôleur est utile pour valider le fonctionnement du hachage et la comparaison des mots de passe lors du développement ou du debug.

---

> **Remarque :** Ces contrôleurs sont conçus uniquement pour un usage de test et debug en environnement de développement. Ils ne doivent pas être exposés en production.

## Configuration externe

Un fichier de configuration externe est fourni dans le dossier `utils/tests` sous le nom  
`application-external.properties.template`.

Ce fichier contient les propriétés à personnaliser pour la connexion à la base de données, le serveur SMTP,  
le port de l'application, ainsi que les options Hibernate et le chemin des modèles DOCX.

Pour déployer ou exécuter l'application, copiez ce fichier en `application-external.properties` et adaptez-le  
avec vos paramètres locaux (hôte, utilisateur, mot de passe, etc.).  
Cela permet de séparer la configuration sensible ou variable du code source.

## Fichiers SQL d'initialisation

Deux fichiers SQL sont disponibles dans le dossier `utils/tests` pour faciliter la préparation de la base de données en environnement de développement :

- **`create-db.sql`**  
  Ce fichier contient :
    - La création de la base `genioservice_db` si elle n’existe pas.
    - La suppression de la table `utilisateur` si elle existe (utile pour réinitialiser).
    - La création de la table `utilisateur` avec les champs requis (email, rôle, mot de passe hashé, etc.).

- **`init.sql`**  
  Ce fichier insère trois utilisateurs de test avec des mots de passe déjà hashés :

  | Email                     | Rôle         | Mot de passe en clair    |
    |---------------------------|--------------|---------------------------|
  | alice.martin@genio.com    | GESTIONNAIRE | `Genio2025!@A`            |
  | bruno.leroy@genio.com     | EXPLOITANT   | `StrongPass#22`           |
  | clara.dubois@genio.com    | CONSULTANT   | `Cl@raSecure99`           |

> Ces fichiers peuvent être exécutés manuellement via un client MySQL ou automatiquement montés dans un conteneur Docker via le dossier `docker-entrypoint-initdb.d`.