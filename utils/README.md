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