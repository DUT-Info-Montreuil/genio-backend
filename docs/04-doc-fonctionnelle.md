# Documentation fonctionnelle – GenioService

---

## Sommaire
- [Page d’accueil](#page-daccueil)
- [Écran de connexion](#écran-de-connexion)
- [Écran d’inscription](#écran-dinscription)
- [Écran de réinitialisation du mot de passe](#écran-de-réinitialisation-du-mot-de-passe)
- [Écran de réinitialisation du mot de passe - nouveau mot de passe](#écran-de-réinitialisation-du-mot-de-passe---nouveau-mot-de-passe)
- [Écran de consultation des modèles – Consultant](#écran-de-consultation-des-modèles--consultant)
- [Écran de consultation des modèles – Exploitant](#écran-de-consultation-des-modèles--exploitant)
- [Écran de visualisation de l’historique des conventions – Exploitant](#écran-de-visualisation-de-lhistorique-des-conventions--exploitant)

## Page d’accueil

### Objectif de l’écran

L’écran d’accueil de GenioService est la porte d’entrée principale de l’application.  
Il permet à l’utilisateur d’accéder aux trois grandes fonctionnalités du service :
- Consulter des modèles prêts à l’emploi
- Gérer les modèles existants
- Consulter l’historique des conventions

---

### Aperçu de la page
<div>
  <img src="./assets/images/page-accueil.png" alt="Page d’accueil GenioService" width="600"/>
</div>

---

### Fonctionnement de l’écran

- La page d’accueil est composée de **trois cartes interactives**.
- Chaque carte redirige vers la page de **connexion** (/connexion) si l’utilisateur n’est pas encore connecté.
- Une fois connecté, l’utilisateur accède à la fonctionnalité selon ses droits.

**Les trois cartes affichées :**
1. **Consulter les modèles**  
   Permet d’accéder à des modèles de conventions prêts à l’emploi.

2. **Gérer les modèles**  
   Permet d’ajouter, modifier ou supprimer des modèles.

3. **Historique des conventions**  
   Permet de visualiser les conventions déjà générées.


---

### Exigences fonctionnelles

- L’écran est **accessible sans authentification**.
- Les liens mènent tous vers la **page de connexion**.
- L’accès aux fonctionnalités est ensuite **filtré par rôle** utilisateur (décrit dans une section séparée de la documentation).
- Chaque carte est cliquable et bien lisible (titre + image + descriptif).
- Le design est **responsive** pour une utilisation sur desktop et mobile.

---

### Exigences techniques

- Application Angular 17
- routerLink vers /connexion utilisé sur chaque carte
- Images en loading="lazy" pour optimiser le chargement
- aria-label présent pour l’accessibilité
- Texte masqué avec .visually-hidden pour les lecteurs d’écran
- Structure HTML sémantique (main, nav, article, etc.)
- Mise en page responsive (grille adaptative CSS)

---

## Écran de connexion

### Objectif de l’écran

Permet à l’utilisateur de s’authentifier pour accéder à son espace GenioService.  
Obligatoire pour consulter, gérer ou visualiser des conventions.

---

### Aperçu de la page
<div>
  <img src="./assets/images/page-connexion.png" alt="Page de connexion GenioService" width="500"/>
</div>

---

## Fonctionnement de l’écran
- L’utilisateur saisit son **email** et son **mot de passe**.
- Le bouton **“Se connecter”** déclenche une requête d’authentification (POST /auth/login).
- En cas d’échec, un **message d’erreur** s’affiche dynamiquement (401 ou 403).
- En cas de succès :
  - Le rôle est récupéré via GET /api/utilisateurs/me
  - L’utilisateur est redirigé automatiquement vers la page consulter-modeles.
- Un lien **“Mot de passe oublié ?”** permet de réinitialiser le mot de passe.
- Un lien **“S’inscrire”** est accessible pour les nouveaux utilisateurs.

---

## Exigences fonctionnelles
- Tous les champs sont **obligatoires** (required HTML).
- Le champ **email** doit respecter un format valide.
- L’utilisateur est informé en cas d'erreur (email ou mot de passe incorrect / compte non activé).
- L'utilisateur peut **afficher/masquer le mot de passe** avec un bouton toggle.
- Les champs sont **accessibles au clavier** et compatibles lecteur d’écran (aria-*).
- Le lien vers la réinitialisation de mot de passe est visible sans être connecté.
- En cas de succès, un **message de confirmation** est affiché.

---

## Exigences techniques
- **Framework** : Angular 17
- **Composants utilisés** : FormsModule, NgIf, RouterLink
- **Services utilisés** :
  - HttpClient pour l’appel à l’API /auth/login
  - AuthService pour enregistrer l’utilisateur connecté
- **Gestion des erreurs** :
  - 401 → identifiants invalides
  - 403 → compte en attente de validation
  - Autres → message générique
- **Mot de passe** :
  - Minimum 12 caractères (côté validation serveur)
  - Possibilité d’affichage/masquage (fa-eye, fa-eye-slash)
- **Accessibilité** :
  - Champs annotés avec aria-label, aria-describedby, aria-invalid
  - Structure HTML claire avec role="form" et aria-labelledby
- **Design** :
  - Bouton désactivé pendant l’envoi ([disabled]="isSubmitting")
  - Composant responsive avec marges et alignements optimisés
  - Messages animés et temporisés (effacement automatique après 5 sec)

---


## Écran d’inscription

### Objectif de l’écran

Permet à un nouvel utilisateur de créer un compte GenioService.  
C’est l’étape préalable à toute utilisation du service.

---

### Aperçu de la page
<div>
  <img src="./assets/images/page-inscription.png" alt="Page d’inscription GenioService" width="500"/>
</div>

---

## Fonctionnement de l’écran

- L’utilisateur doit renseigner :
  - **Prénom**
  - **Nom**
  - **Adresse e-mail**
  - **Mot de passe**
  - **Confirmation du mot de passe**

- Une fois tous les champs valides :
  - Le bouton **S’enregistrer** envoie les données via POST /api/utilisateurs
  - L’adresse e-mail est vérifiée pour **unicité** en temps réel (appel GET /exists)
  - Si inscription réussie, l’utilisateur est redirigé vers la page de **connexion**
  - En cas d’erreur, un message clair est affiché en haut du formulaire

- Le formulaire inclut une **vérification dynamique du mot de passe** :
  - La liste des règles non respectées est affichée en badges rouges sous le champ

---

## Exigences fonctionnelles

- Tous les champs sont **obligatoires** (required)
- Le mot de passe doit **respecter toutes les règles suivantes** :
  - Minimum **12 caractères**
  - Contenir **au moins 1 majuscule**
  - Contenir **au moins 1 chiffre**
  - Contenir **au moins 1 caractère spécial**
- Les mots de passe et confirmation doivent **être identiques**
- Si l’adresse e-mail existe déjà, un message s’affiche : Cet e-mail est déjà utilisé.
- En cas d’erreur, le message disparaît automatiquement après quelques secondes
- Accessibilité :
  - aria-label, aria-live, aria-describedby intégrés
  - Navigation clavier et lecteurs d’écran prise en charge
- Comportement responsive (PC et mobile)

---

## Exigences techniques

- **Framework** : Angular 17
- **Composants** : FormsModule, NgIf, NgForOf, RouterLink
- **Logique Angular** :
  - Requête POST /api/utilisateurs avec Content-Type: application/json
  - Requête GET /api/utilisateurs/exists?email= pour la vérification d’unicité
- **Sécurité** :
  - Aucun mot de passe transmis en clair
  - Affichage/masquage des champs mot de passe via bouton fa-eye
- **Accessibilité & UX** :
  - Badges d’erreur discrets pour les règles non respectées
  - Bouton désactivé pendant l’envoi ([disabled]="isSubmitting")
  - Aria busy actif durant le chargement
- **Redirection** :
  - Succès → redirection vers /connexion après 2 secondes

---

## Écran de réinitialisation du mot de passe

### Objectif de l’écran

Permet à l’utilisateur de demander un lien de réinitialisation de mot de passe par email.  
Cette fonctionnalité est destinée aux utilisateurs ayant oublié leur mot de passe.

---

### Aperçu de la page
<div>
  <img src="./assets/images/page-mot-de-passe-oublie.png" alt="Page de réinitialisation du mot de passe GenioService" width="500"/>
</div>

---

### Fonctionnement de l’écran

- L’utilisateur saisit son **adresse e-mail** dans le champ prévu.
- En cliquant sur le bouton **"Réinitialiser"**, une requête `POST /auth/mot-de-passe-oublie` est envoyée.
- Si l’email est associé à un compte existant :
  - Un email de réinitialisation est envoyé.
  - Un **message de succès** est affiché : _"Un e-mail de réinitialisation a été envoyé si votre adresse est enregistrée."_
- En cas d’erreur :
  - Un **message d’erreur** s’affiche dynamiquement.
- Un lien **"Se connecter"** permet de revenir à la page de connexion.

---

### Exigences fonctionnelles

- Champ **e-mail obligatoire** et validé (`required` + format email)
- Envoi sécurisé de la requête au backend (`POST`)
- Affichage :
  - D’un **message d’erreur** en cas d’échec
  - D’un **message de confirmation** même si l’email n’existe pas (pour éviter l’exploitation)
- Accessibilité :
  - `aria-label`, `aria-describedby`, `aria-invalid`, `aria-live`
  - Navigation **clavier** et **lecteurs d’écran** supportés
- Bouton désactivé pendant la soumission
- Redirection manuelle possible vers la page de connexion

---

### Exigences techniques

- **Framework** : Angular 17
- Composants : `FormsModule`, `NgIf`, `NgClass`, `RouterLink`
- API :
  - `POST /auth/mot-de-passe-oublie` avec `Content-Type: application/json`
- Accessibilité & UX :
  - `aria-labelledby="reset-title"` pour le formulaire
  - `aria-describedby` dynamique selon l’état d’erreur
  - Bouton avec `aria-busy` pendant l’envoi
- Sécurité :
  - Message neutre, identique que l’email existe ou non
  - Aucune information sensible révélée côté frontend
- Design :
  - Interface centrée
  - Message temporaire (5 secondes pour erreur, 2 secondes pour succès)

---

## Écran de réinitialisation du mot de passe - nouveau mot de passe

### Objectif de l’écran

Cet écran permet à l’utilisateur de définir un **nouveau mot de passe** après avoir cliqué sur le lien reçu par email.  
C’est la dernière étape de la procédure “Mot de passe oublié”.

---

### Aperçu de la page
<div>
  <img src="./assets/images/page-reset-password.png" alt="Page de réinitialisation du mot de passe GenioService" width="500"/>
</div>

---

### Fonctionnement de l’écran

- L’utilisateur saisit :
  - Un **nouveau mot de passe**
  - Une **confirmation de ce mot de passe**
- Une **vérification dynamique** affiche les règles non respectées.
- Une fois toutes les règles respectées :
  - Le bouton **"Réinitialiser et connecter"** devient actif.
  - Une requête `POST /auth/reset-password` est envoyée avec le **token** en paramètre.
- En cas de succès :
  - Le mot de passe est mis à jour.
  - L’utilisateur est automatiquement redirigé vers la **page de connexion** après 2 secondes.
- En cas d’erreur :
  - Un message d’erreur s’affiche dynamiquement.

---

### Exigences fonctionnelles

- Champs obligatoires : **nouveau mot de passe** et **confirmation**
- Le mot de passe doit respecter toutes les **règles de sécurité** :
  - Minimum **12 caractères**
  - Contenir **1 majuscule**
  - Contenir **1 chiffre**
  - Contenir **1 caractère spécial**
- Le mot de passe et sa confirmation doivent **être identiques**
- Un **feedback en temps réel** indique les règles non respectées (badges rouges)
- Un **message de succès** ou d'erreur est affiché dynamiquement
- Accessibilité :
  - `aria-label`, `aria-describedby`, `aria-live`
  - Champs accessibles via le clavier
  - Affichage/Masquage du mot de passe possible (`fa-eye`, `fa-eye-slash`)

---

### Exigences techniques

- **Framework** : Angular 17
- **Composants Angular** : `FormsModule`, `NgIf`, `NgForOf`
- **API** :
  - `POST /auth/reset-password` avec `token` et `nouveauMotDePasse`
- **Sécurité** :
  - Aucune information sensible stockée en local
  - Le token est lu dans `queryParams` pour sécuriser le lien
- **UX & accessibilité** :
  - Validation instantanée des règles de mot de passe
  - Bouton désactivé pendant la soumission (`[disabled]="isSubmitting"`)
  - Message temporaire (succès ou erreur)

---
# Écran de consultation des modèles – Consultant

## Objectif de l’écran
Permet aux utilisateurs **Consultant** de visualiser les modèles de conventions disponibles, en lecture seule.

---

## Aperçu de la page
<div>
  <img src="./assets/images/page-consulter-modeles.png" alt="Écran de consultation - Consultant" width="600"/>
</div>

---

## Fonctionnement de l’écran

- La page liste tous les modèles de convention enregistrés dans le système.
- Le **Consultant** peut :
  - Utiliser les filtres de recherche par titre, année ou texte libre
  - Consulter les détails d’un modèle (en cliquant sur l’icône en forme d’œil)
  - Visualiser les variables attendues pour chaque modèle via une modale
- Les icônes **ajouter / modifier / archiver** sont désactivées et inaccessibles.
---

## Exigences fonctionnelles

- **Rôle requis** : Consultant (affichage en haut à droite)
- Les actions disponibles sont :
  - **Voir** le modèle (`fa-eye`)
- Les actions **non autorisées** pour ce rôle sont :
  - Ajouter
  - Modifier
  - Archiver
- La consultation inclut :
  - Nom du modèle
  - Année de création
  - Format (Document)
  - Statut d’utilisation (utilisé ou non)
  - Variables attendues (affichage limité puis dépliable)

---

## Filtres disponibles

- **Nom du modèle**
- **Année** (champ numérique avec datalist entre 2000 et 2099)
- **Recherche avancée** (sur texte libre, statut, description…)

---

## Accessibilité

- Icônes annotées (`aria-label`, `aria-hidden`)
- Focus clavier sur les lignes du tableau
- Boutons utilisables via `Enter`
- Modale accessible (`role=dialog`, `aria-labelledby`, `aria-describedby`)
- Pagination accessible au clavier

---

## Exigences techniques

- **Framework** : Angular 17
- **Droits contrôlés** via `AuthService` (`isConsultant`)
- **Requête API** : `GET /conventionServices` pour obtenir les modèles
- Les modèles sont paginés (5 / 10 / 15 lignes)
- Aucune action n’effectue de POST/PUT/DELETE en tant que Consultant
- Utilisation de `NgClass` pour désactiver dynamiquement les boutons non autorisés
- Icônes grisées avec `disabled-style` sur les boutons restreints

---

## Éléments supplémentaires

- Un **badge** en haut à droite affiche le rôle actuel :  
  `Connecté en tant que : Consultant`

- Un **bouton d’aide** (❔) ouvre une fenêtre modale expliquant le fonctionnement de la page.

---
## Écran de consultation des modèles – Exploitant
## Objectif de l’écran
Accès **identique** à celui du Consultant pour consulter les modèles, avec une navigation **supplémentaire**.

---

## Aperçu de la page
<div>
  <img src="./assets/images/page-historique-exploitant.png" alt="Écran de consultation - Consultant" width="600"/>
</div>

### Fonctionnalités

- Identiques à celles du rôle Consultant
- Lecture seule, sans bouton d’action

### Spécificités

- Rôle requis : **Exploitant**
- Badge : `Connecté en tant que : Exploitant`
- Section **“Gérer les utilisateurs”** invisible
- Accès supplémentaire à l’onglet **“Visualiser”**


## Écran de visualisation de l’historique des conventions – Exploitant

## Objectif de l’écran

Permet aux utilisateurs de type **Exploitant** d’accéder à l’historique des tentatives de génération de conventions, avec un détail sur les étapes de traitement (Flux, JSON, DOCX) et la possibilité d’en consulter les erreurs.

---

## Aperçu de la page
<div>
  <img src="./assets/images/page-historique-exploitant.png" alt="Écran de consultation - Consultant" width="600"/>
</div>

---

## Fonctionnement de l’écran

- Liste paginée des tentatives de génération de conventions.
- Filtres disponibles :
  - Par **Nom d’étudiant**
  - Par **Promotion**
  - Par **Année** (via datalist)
- Pour chaque ligne :
  - Les étapes **Flux**, **JSON** et **DOCX** sont évaluées avec des icônes ✔️ (OK) ou ⚠️ (KO)
  - La colonne **Statut** indique si la tentative globale est un **SUCCÈS** ou un **ÉCHEC**
  - La **Date** de génération est affichée
  - Une icône 👁️ permet d’accéder au **détail de l’erreur** si disponible
- Une **modale d’aide** (❔) fournit un rappel sur l’usage de la page
- Des icônes d’aide supplémentaires sont disponibles dans l’en-tête de chaque étape

---

## Exigences fonctionnelles

- **Rôle requis** : Exploitant (affiché en haut à droite)
- Accès en lecture seule aux historiques
- Les statuts intermédiaires sont :
  - **Flux** : transformation des données d'entrée
  - **JSON** : validation du contenu des champs
  - **DOCX** : génération du document final
- La logique métier affiche KO si une étape échoue, avec détails si présents
- Aucun bouton de modification n’est présent

---

## Accessibilité

- Icônes annotées (`aria-label`)
- Focus clavier activé sur les lignes
- Modales accessibles (`role=dialog`, `aria-labelledby`)
- Navigation paginée avec touches (`Enter`, `Tab`)
- Texte alternatif pour icônes et boutons

---

## Exigences techniques

- **Framework** : Angular 17
- **Contrôle d’accès** : via `AuthService.isExploitant()`
- **Source des données** : `GET /api/genio/historique`
- Classe CSS dynamique pour le statut (`status-error`, `status-success`)
- Pagination configurable (5, 10, 15 lignes)
- Détail d’erreur affiché dans une modale
- Analyse automatisée des erreurs selon le contenu de `details`

---

## Éléments spécifiques à l’utilisateur "Exploitant"

- Affichage du badge : `Connecté en tant que : Exploitant`
- Section "Gérer les utilisateurs" non visible (réservée au gestionnaire)
- Bouton **Visualiser** actif dans la navigation secondaire
- Aucun bouton d’ajout, modification ou suppression de conventions ou modèles

---

## Aide intégrée

- Bouton ❔ général : explication du fonctionnement de l'écran
- Boutons ❔ spécifiques pour :
  - **Flux** : erreurs de format, d’ID modèle manquant, etc.
  - **JSON** : erreurs de validation de champs
  - **DOCX** : erreurs d’export ou fichier manquant
- Ces aides ouvrent une modale explicative contextualisée

---

## Métadonnées

- **Dernière mise à jour** : 29 mai 2025
- **Rédactrice** : Elsa HADJADJ
- **Version** : 1.0  