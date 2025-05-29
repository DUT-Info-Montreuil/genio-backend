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
- [Écran de consultation des modèles – Gestionnaire](#écran-de-consultation-des-modèles--gestionnaire)
- [Écran de visualisation de l’historique des conventions – Exploitant](#écran-de-visualisation-de-lhistorique-des-conventions--exploitant)
- [Écran de visualisation de l’historique des conventions – Gestionnaire](#écran-de-visualisation-de-lhistorique-des-conventions--gestionnaire)
- [Écran de gestion des modèles – Gestionnaire](#écran-de-gestion-des-modèles--gestionnaire)
  - [Sous-onglet : Ajouter un modèle](#sous-onglet--ajouter-un-modèle)
  - [Sous-onglet : Modifier un modèle](#sous-onglet--modifier-un-modèle)
  - [Sous-onglet : Archiver un modèle](#sous-onglet--archiver-un-modèle)
- [Écran de gestion des utilisateurs – Gestionnaire](#écran-de-gestion-des-utilisateurs--gestionnaire)
- [Page – Plan du site](#page--plan-du-site)
- [Pied de page (footer)](#pied-de-page-footer)
- [Page – À propos de GenioService](#page--à-propos-de-genioservice)
- [Page – Contact](#page--contact)
- [Page – Données personnelles & cookies](#page--données-personnelles--cookies)
- [Pages - Mentions légales](#page--mentions-légales)

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

### Fonctionnement de l’écran
- L’utilisateur saisit son **email** et son **mot de passe**.
- Le bouton **“Se connecter”** déclenche une requête d’authentification (POST /auth/login).
- En cas d’échec, un **message d’erreur** s’affiche dynamiquement (401 ou 403).
- En cas de succès :
  - Le rôle est récupéré via GET /api/utilisateurs/me
  - L’utilisateur est redirigé automatiquement vers la page consulter-modeles.
- Un lien **“Mot de passe oublié ?”** permet de réinitialiser le mot de passe.
- Un lien **“S’inscrire”** est accessible pour les nouveaux utilisateurs.

---

### Exigences fonctionnelles
- Tous les champs sont **obligatoires** (required HTML).
- Le champ **email** doit respecter un format valide.
- L’utilisateur est informé en cas d'erreur (email ou mot de passe incorrect / compte non activé).
- L'utilisateur peut **afficher/masquer le mot de passe** avec un bouton toggle.
- Les champs sont **accessibles au clavier** et compatibles lecteur d’écran (aria-*).
- Le lien vers la réinitialisation de mot de passe est visible sans être connecté.
- En cas de succès, un **message de confirmation** est affiché.

---

### Exigences techniques
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

### Fonctionnement de l’écran

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

### Exigences fonctionnelles

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

### Exigences techniques

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


## Écran de consultation des modèles – Consultant

### Objectif de l’écran

Permet aux utilisateurs ayant le rôle **Consultant** de consulter les modèles de conventions disponibles dans GenioService.

Cette interface offre un accès en **lecture seule** à l’ensemble des modèles enregistrés, sans possibilité de modification, ajout ou archivage.  
Elle constitue l’interface de consultation standard, simple et sécurisée.

---

### Aperçu de la page

<div>
  <img src="./assets/images/page-consulter-modeles.png" alt="Page de consultation – Consultant GenioService" width="600"/>
</div>

---

### Fonctionnement de l’écran

- L’interface se compose d’un tableau listant tous les modèles de convention enregistrés.
- L’utilisateur **Consultant** peut :
  - Filtrer les modèles par **nom**, **année**, ou **recherche avancée** (texte libre)
  - Consulter les détails d’un modèle (via l’icône œil)
  - Voir les **variables attendues** dans une modale (affichage limité avec bouton “Voir toutes les variables”)
- Les actions suivantes sont **désactivées et grisées** :
  - ➕ Ajouter un modèle
  - ✏️ Modifier un modèle
  - 🗃️ Archiver un modèle
- Aucun bouton de gestion des utilisateurs n’est présent.
- Aucun accès à l’onglet “Visualiser”.
- En haut à droite, un badge affiche le rôle actuel :

  > Connecté en tant que : **Consultant**

---

### Exigences fonctionnelles

- **Rôle requis** : Consultant
- Les fonctionnalités disponibles sont strictement limitées à la **consultation** :
  - Visualisation du modèle
  - Filtres actifs (nom, année, recherche avancée)
  - Consultation des variables attendues (via modale)
- Les actions de gestion (ajout, édition, archivage) sont **désactivées**
- Affichage des champs :
  - Titre du modèle
  - Année
  - Format
  - Statut d’utilisation
- Les champs de filtre sont accessibles à tous les rôles

---

### Exigences techniques

- **Framework** : Angular 17
- **Composants utilisés** :
  - `ConsulterModeleTousComponent`
  - `RouterLink` pour la navigation
- **Droits et sécurité** :
  - Vérification du rôle via `AuthService.isConsultant()`
  - Aucune possibilité d’accéder à `/gerer-modeles`
  - Les actions POST, PUT et DELETE sont bloquées côté frontend et backend
- **Accessibilité (WCAG)** :
  - Boutons désactivés avec `aria-disabled="true"` et `title="Réservé au gestionnaire"`
  - Focus clavier activé sur les lignes du tableau
  - Icônes annotées (`aria-label`, `aria-hidden`)
  - Modale accessible (`role="dialog"`, `aria-labelledby`, `aria-describedby`)
- **UX & Design** :
  - Boutons non autorisés grisées (`disabled-style`)
  - Badge “Connecté en tant que : Consultant”
  - Aide ❔ en haut à droite ouvrant une modale d’explication

---

## Écran de consultation des modèles – Exploitant

### Objectif de l’écran

Permet aux utilisateurs ayant le rôle Exploitant de consulter les modèles de conventions disponibles.

Cette interface reprend celle du Consultant, avec quelques différences fonctionnelles et visuelles,
notamment l’accès à l’onglet “Visualiser” pour suivre les tentatives de génération de conventions.
---

### Aperçu de la page
<div>
  <img src="./assets/images/page-consulter-exploitant.png" alt="Page de consultation – Exploitant GenioService" width="600"/>
</div>

---

### Fonctionnement de l’écran

- L’exploitant accède à une interface identique à celle du consultant, à l’exception de deux éléments :
  - Un **badge vert** en haut à droite : Connecté en tant que : Exploitant
  - Un **onglet supplémentaire “Visualiser”** affiché dans la barre de navigation
  - Les actions affichées dans le tableau sont toutes **grisés et inactives** (lecture seule) :
    - 👁️ Voir le modèle : **actif**
    - ➕ Ajouter : **désactivé**
    - ✏️ Modifier : **désactivé**
    - 🗃️ Archiver : **désactivé**
- En cliquant sur l’onglet “Visualiser”, l’exploitant accède **à la page d’historique des générations.**
- Aucune cloche de notification, aucun bouton de gestion des utilisateurs n’est visible (réservé au Gestionnaire).

---

### Exigences fonctionnelles
- Rôle requis : Exploitant
- Le tableau permet :
  - Filtrage par nom, année, texte libre
  - Visualisation des détails d’un modèle (modale)
- Les boutons ➕ ✏️ 🗃️ sont visibles mais non interactifs
- L’onglet **Visualiser** est exclusif au rôle Exploitant et Gestionnaire
- 
---

### Exigences techniques

- **Framework** : Angular 17
- **Composants Angular** :
  - ConsulterModeleTousComponent
  - RouterLink vers /historique-conventions
  - AuthService.isExploitant() pour activer le rôle
  
- **Sécurité** :
  - Droits contrôlés en frontend et backend
  - Actions d’édition désactivées (grisés via disabled-style)
  - Blocage complet de /gerer-modeles

- **UX & accessibilité** :
  - Boutons inactifs signalés avec aria-disabled="true"
  - Icônes grisées avec title="Réservé au gestionnaire"
  - Clavier uniquement sur la visualisation (fa-eye)
  - Focus rétabli après navigation
  - Affichage clair du rôle connecté
  
- Design :
  - Badge Connecté en tant que : Exploitant vert
  - Onglet “Visualiser” visible uniquement pour les exploitants
  - Pas de cloche, pas de gestion utilisateurs

---

## Écran de consultation des modèles – Gestionnaire

### Objectif de l’écran

Permet aux utilisateurs ayant le rôle Gestionnaire de consulter les modèles de conventions disponibles,
avec des droits complets sur chaque ligne (ajout, modification, archivage).
Cette page est une version enrichie de la vue “Consultant”, avec des outils d’administration intégrés.

---

### Aperçu de la page
<div>
  <img src="./assets/images/page-consulter-modele-gestionnaire.png" alt="Page de consultation – Gestionnaire GenioService" width="600"/>
</div>


---

### Fonctionnement de l’écran

- La page est identique à celle du Consultant en apparence, mais avec :
  - Une **cloche de notification** pour visualiser les demandes d’habilitation utilisateurs.
  - Un **bouton “Gérer les utilisateurs”** pour accéder à l’administration des comptes.
  - **Toutes les actions activées** dans la colonne “Actions” du tableau :
     - 👁️ Voir le modèle
     - ➕ Ajouter un modèle (redirige vers l’onglet “Ajouter”)
     - ✏️ Modifier un modèle (redirige vers l’onglet “Modifier” avec le modèle préchargé)
     - 🗃️ Archiver un modèle (redirige vers l’onglet “Archiver” avec confirmation)
- En cliquant sur ces icônes, l’utilisateur est redirigé vers l’interface de gestion des modèles avec l’onglet correspondant actif (ajouter, modifier, ou archiver).
- La cloche en haut à droite indique le nombre de demandes d’habilitation en attente :

  - ✅ badge vert si aucune demande
  - 🔴 badge rouge si une ou plusieurs demandes
  - Un message temporaire s’affiche à l’ouverture de la cloche 
- Le bouton “Gérer les utilisateurs” permet d’accéder à la page dédiée (non accessible aux autres rôles).
 

---

### Exigences fonctionnelles
- Rôle requis : Gestionnaire
- Le tableau permet :
	- Filtrage par nom, année, texte libre
	- Consultation des détails
	- Ajout direct
	- Modification immédiate
	- Archivage immédiat
- Les boutons d’action sont interactifs uniquement pour les gestionnaires :
	- Désactivés ou absents pour les autres rôles
	- Accessibles au clavier (avec Tab + Enter)
- En tête enrichi :
	- Cloche d’habilitation visible uniquement pour les gestionnaires
	- Bouton “Gérer les utilisateurs” accessible avec focus clavier

---

### Exigences techniques

- **Framework** : Angular 17
- **Composants Angular** :
    - ConsulterModeleTousComponent
    - RouterLink avec queryParams ({ onglet: 'ajouter' | 'modifier' | 'archiver' })
    - AuthService pour vérifier le rôle utilisateur
- **Sécurité** :
  - Vérification du rôle en frontend et backend
  - Redirection sécurisée selon droits
- **UX & accessibilité** :
  - Boutons annotés (aria-label, aria-disabled)
  - Clavier compatible sur toutes les actions
  - Focus automatique sur tableau à chaque navigation
  - Icône de notification : aria-haspopup, aria-live
- Design :
    - Icônes : 👁️ fa-eye, ➕ fa-plus, ✏️ fa-edit, 🗃️ fa-archive
    - Badge dynamique sur cloche (vert/rouge)
    - Messages temporaires (toast ou infobulle)

---

## Écran de visualisation de l’historique des conventions – Exploitant

### Objectif de l’écran

Permet aux utilisateurs de type **Exploitant** d’accéder à l’historique des tentatives de génération de conventions, avec un détail sur les étapes de traitement (Flux, JSON, DOCX) et la possibilité d’en consulter les erreurs.

---

### Aperçu de la page
<div>
  <img src="./assets/images/page-historique-exploitant.png" alt="Écran de consultation - Consultant" width="600"/>
</div>

---

### Fonctionnement de l’écran

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

### Exigences fonctionnelles

- **Rôle requis** : Exploitant (affiché en haut à droite)
- Accès en lecture seule aux historiques
- Les statuts intermédiaires sont :
  - **Flux** : transformation des données d'entrée
  - **JSON** : validation du contenu des champs
  - **DOCX** : génération du document final
- La logique métier affiche KO si une étape échoue, avec détails si présents
- Aucun bouton de modification n’est présent

---

### Accessibilité

- Icônes annotées (`aria-label`)
- Focus clavier activé sur les lignes
- Modales accessibles (`role=dialog`, `aria-labelledby`)
- Navigation paginée avec touches (`Enter`, `Tab`)
- Texte alternatif pour icônes et boutons

---

### Exigences techniques

- **Framework** : Angular 17
- **Contrôle d’accès** : via `AuthService.isExploitant()`
- **Source des données** : `GET /api/genio/historique`
- Classe CSS dynamique pour le statut (`status-error`, `status-success`)
- Pagination configurable (5, 10, 15 lignes)
- Détail d’erreur affiché dans une modale
- Analyse automatisée des erreurs selon le contenu de `details`

---

### Aide intégrée

- Bouton ❔ général : explication du fonctionnement de l'écran
- Boutons ❔ spécifiques pour :
  - **Flux** : erreurs de format, d’ID modèle manquant, etc.
  - **JSON** : erreurs de validation de champs
  - **DOCX** : erreurs d’export ou fichier manquant
- Ces aides ouvrent une modale explicative contextualisée

---

## Écran de visualisation de l’historique des conventions – Gestionnaire

### Objectif de l’écran

Permet aux utilisateurs ayant le rôle **Gestionnaire** de consulter l’ensemble des tentatives de génération de conventions enregistrées dans le système.

Cette interface est **identique à celle de l’Exploitant** sur le fond et les fonctionnalités, mais avec une **affichage distinct du rôle** et l’accès parallèle aux fonctions de gestion.

---

### Aperçu de la page
<div>
  <img src="./assets/images/page-historique-gestionnaire.png" alt="Écran de visualisation – Gestionnaire GenioService" width="600"/>
</div>

---

### Fonctionnement de l’écran

- Le tableau liste toutes les **tentatives de génération de conventions**, paginées.
- Chaque ligne présente :
  - Le **Nom de l’étudiant**
  - La **Promotion**
  - L’**Année**
  - Le **statut de chaque étape** :
    - ✔️ OK ou ❌ KO pour les phases **Flux**, **JSON**, **DOCX**
  - Le **statut global** : `SUCCÈS` ou `ÉCHEC`
  - La **date** de la tentative
  - Un bouton 👁️ pour accéder au **détail de l’erreur**
- Les **filtres disponibles** en haut de page :
  - Nom d’étudiant
  - Année
  - Promotion
- Un bouton **❔ d’aide globale** est affiché pour expliquer le fonctionnement.
- Des **icônes d’aide spécifiques** sont également visibles dans l’en-tête de chaque colonne (Flux, JSON, DOCX).
- Aucune action d’édition ou de suppression n’est présente.

---

### Exigences fonctionnelles

- **Rôle requis** : Gestionnaire
- Consultation complète de l’historique des générations :
  - Détails par tentative
  - Statuts intermédiaires par étape
  - Affichage chronologique
- Lecture seule : aucune modification, suppression ou export
- Modale détaillée disponible via l’icône 👁️

---

### Accessibilité

- Navigation au **clavier** (touches `Tab`, `Enter`)
- Icônes avec attributs `aria-label` et `aria-hidden`
- Boutons d’ouverture de modale accessibles
- Filtrage accessible aux lecteurs d’écran
- Modales conformes WCAG (`role="dialog"`, `aria-labelledby`, etc.)

---

### Exigences techniques

- **Framework** : Angular 17
- **Composant principal** : `HistoriqueConventionsComponent`
- **Contrôle d’accès** :
  - Vérification via `AuthService.isGestionnaire()`
  - Interface identique à l’exploitant mais badge différent
- **Données** :
  - Récupérées via `GET /api/genio/historique`
  - Formatées pour affichage paginé
  - Champs analysés : nom, promotion, année, statuts
- **CSS dynamique** :
  - `status-success` (vert) pour SUCCÈS
  - `status-error` (rouge) pour ÉCHEC
- **Design & UX** :
  - Icônes d’aide dans l’en-tête de colonnes
  - Bouton général ❔ pour guide d’utilisation
  - Pagination fluide et focus maintenu

---

### Aide intégrée

- **Bouton ❔ général** : affichage d’une modale expliquant l’usage de la page
- **Boutons ❔ contextuels** dans chaque colonne :
  - **Flux** : transformation des données en entrée
  - **JSON** : validation des champs requis
  - **DOCX** : génération du document final
- Ces icônes ouvrent des **modales d’aide spécifiques et contextualisées**

---

## Écran de gestion des modèles – Gestionnaire

### Objectif de l’écran

Permet aux utilisateurs ayant le rôle **Gestionnaire** d’ajouter, modifier ou archiver des modèles de conventions au format `.docx`.  
C’est la seule interface ayant un contrôle complet sur le référentiel de modèles disponibles dans le système.

---

### Accès et rôles

- **Rôle requis** : `Gestionnaire`
- L'accès est contrôlé dans Angular via : `AuthService.isGestionnaire()`
- Les rôles **Exploitant** et **Consultant** n’ont pas accès à cette page
- Si un utilisateur non autorisé tente d’y accéder, il est redirigé

---

### Fonctionnement général

L’écran se divise en **trois sous-onglets** :

1. **Ajouter un modèle**  
   Permet d’importer un nouveau modèle `.docx` :
  - Champ obligatoire : année du modèle (format `YYYY`)
  - Upload par glisser-déposer ou bouton “Choisir un fichier”
  - Type accepté : `.docx` uniquement
  - Taille maximale : 4 Mo
  - Un champ facultatif permet de saisir le titre du modèle

2. **Modifier un modèle**  
   Permet de :
  - Rechercher un modèle existant
  - Modifier son titre
  - Visualiser les métadonnées
  - Mettre à jour la description ou l’année (si autorisé)

3. **Archiver un modèle**  
   Permet de désactiver un modèle existant :
  - Le modèle archivé devient invisible aux autres rôles
  - L’action est irréversible sauf réactivation manuelle via backend

---

### Aperçu de l’interface
<div>
  <img src="./assets/images/page-gerer-modeles.png" alt="Écran Gérer les modèles – Gestionnaire" width="600"/>
</div>
---

### Exigences fonctionnelles

- L’écran affiche dynamiquement les onglets selon le rôle
- Les actions sont accessibles uniquement au **Gestionnaire**
- Les champs de formulaire sont validés côté client
- Un feedback clair est affiché :
  - Message de succès si action réussie
  - Message d’erreur si format ou fichier invalide
- En cas d’import incorrect :
  - Une modale s’ouvre avec le détail des erreurs
  - Les erreurs de variable (manquante, malformée) sont explicitées

---

### Exigences techniques

- **Framework** : Angular 17
- **Composants** :
  - `AjouterModeleComponent`
  - `ModifierModeleComponent`
  - `SupprimerModeleComponent`
- **Services utilisés** :
  - `AuthService` pour la gestion du rôle
  - `HttpClient` pour les appels `/api/modeles`
- **Sécurité** :
  - Vérification du rôle côté frontend et backend
  - Fichier `.docx` analysé pour détecter les variables attendues
- **Accessibilité** :
  - `aria-label`, `aria-live`, `aria-modal` sur toutes les zones interactives
  - Focus clavier et navigation au `Tab` fonctionnels
- **Responsive** :
  - Design adapté desktop et tablette
  - Affichage flexible selon taille de l’écran

---

### Aide intégrée

- Un bouton ❔ d’aide est affiché dans chaque sous-onglet
- Une modale explique les attentes de chaque section
- Un encart de notification indique les demandes d’habilitation en attente

---

## Sous-onglet : Ajouter un modèle

Ce sous-onglet permet à un **Gestionnaire** d’importer un nouveau modèle de convention au format `.docx`.  
Le fichier est analysé automatiquement pour valider sa structure, l’année et les variables attendues.
---

### Aperçu de l’interface

<div>
  <img src="./assets/images/page-gerer-modeles.png" alt="Écran Gérer les modèles – Gestionnaire" width="600"/>
</div>

---

### Fonctionnement

1. **Année du modèle** :
  - Champ obligatoire au format `YYYY`
  - Validation stricte : entre `2020` et `{{currentYear + 5}}`
  - Vérification d’unicité côté backend (`/check-nom-exists`)

2. **Fichier modèle** :
  - Accepté : `.docx` uniquement
  - Taille max : `4 Mo`
  - Upload par drag-and-drop ou bouton "Choisir un fichier"
  - Validation :
    - Analyse automatique via `/test-generation`
    - Vérification des **variables attendues** (ex. `NOM_ETUDIANT`, `TUT_IUT`, etc.)
    - Retour visuel ✔️ / ❌ par variable

3. **Titre du modèle** :
  - Auto-généré si vide
  - Modifiable en double-cliquant ou via l’icône crayon 🖉

4. **Soumission** :
  - Bouton "Valider le modèle" activé seulement si tous les champs sont valides
  - Appel à l’API `POST /conventionServices`

---

### Cas d’erreurs

- **Année invalide** : modale explicative
- **Modèle déjà existant pour l’année** : blocage
- **Fichier sans variables** : erreur "Ce fichier ne semble pas être un modèle"
- **Fichier partiel** : liste des variables manquantes affichée
- **Autre erreur backend** : message générique ou message retourné affiché

---

### Exigences fonctionnelles

- Tous les champs sont requis pour valider
- Vérification dynamique de l’année et du fichier
- Message clair en cas de succès ou d’erreur
- Accessibilité :
  - `aria-label`, `aria-invalid`, `aria-live`
  - Comportement compatible clavier
- Responsive sur tous formats d’écran

---

### Exigences techniques

- **Framework** : Angular 17
- **Services utilisés** :
  - `HttpClient` (`/test-generation`, `/conventionServices`, `/check-nom-exists`)
- **Comportement modulaire** avec :
  - Composant `AjouterModeleComponent`
  - Fichiers CSS : `ajouter-modele.component.css`, `modal-box.css`
- **Gestion d'état** :
  - Propriétés : `isAnneeValid`, `isFileValid`, `titre`, `error`, `message`
  - Contrôle de validité avant envoi avec `ngSubmit`

---

### Variables attendues analysées automatiquement

Voici la liste complète des variables attendues (via `docxParser`) :
annee, NOM_ORGANISME, ADR_ORGANISME, NOM_REPRESENTANT_ORG,
QUAL_REPRESENTANT_ORG, NOM_DU_SERVICE, TEL_ORGANISME, MEL_ORGANISME,
LIEU_DU_STAGE, NOM_ETUDIANT1, PRENOM_ETUDIANT, SEXE_ETUDIANT,
DATE_NAIS_ETUDIANT, ADR_ETUDIANT, TEL_ETUDIANT, MEL_ETUDIANT,
SUJET_DU_STAGE, DATE_DEBUT_STAGE, DATE_FIN_STAGE, STA_DUREE,
_STA_JOURS_TOT, _STA_HEURES_TOT, TUT_IUT, TUT_IUT_MEL,
PRENOM_ENCADRANT, NOM_ENCADRANT, FONCTION_ENCADRANT,
TEL_ENCADRANT, MEL_ENCADRANT, NOM_CPAM, Stage_Professionnel, STA_REMU_HOR

---

### Astuce UX

Un clic sur l’icône **❔** à côté des champs "Année" ou "Fichier" ouvre une **modale d’aide contextuelle**  
pour expliquer les erreurs de validation ou les variables manquantes.

---

### Accès restreint

- **Rôle requis** : GESTIONNAIRE uniquement
- Ce sous-onglet est masqué pour les rôles EXPLOITANT ou CONSULTANT

---

## Sous-onglet : Modifier un modèle

Ce sous-onglet permet au **Gestionnaire** de rechercher, visualiser et modifier un modèle existant.

---

### Aperçu de l’interface
<div>
  <img src="./assets/images/page-modifier-modele.png" alt="Modifier un modèle – GenioService" width="600"/>
</div>


---

### Fonctionnement

1. **Filtres de recherche** :
  - Par **nom de modèle** (texte libre)
  - Par **année** (champ numérique avec datalist des années existantes)


2. **Affichage des modèles** :
  - Tableau avec colonnes : **Titre**, **Année**, **Format**, **Dernière modification**
  - Pagination : choix du nombre d’entrées par page (5, 10, 15)

3. **Modification d’un modèle** :
  - Clic sur l’icône pour ouvrir la **modale d’édition**
  - Champs modifiables :
    - **Titre** (double clic ou icône crayon)
    - **Description de modification** (textarea facultatif)
    - **Remplacement de fichier** (optionnel)
  - Validation :
    - Vérification du fichier `.docx` et des **variables attendues**
    - Affichage d’un message d’erreur ou de succès après enregistrement

4. **Enregistrement** :
  - Requête `PUT /conventionServices/{id}` pour mettre à jour les métadonnées
  - Si un nouveau fichier est sélectionné : `PUT /conventionServices/{id}/file` (FormData)

---

### Exigences fonctionnelles

- Seul le rôle **GESTIONNAIRE** a accès à ce sous-onglet
- Le tableau est mis à jour dynamiquement après modification
- Le bouton "Réinitialiser" remet tous les filtres à zéro
- La modale peut être fermée en cliquant à l'extérieur ou via le bouton "Annuler"
- Le fichier remplacé doit être un `.docx` valide avec les variables attendues

---

### Exigences techniques

- **Composant** : `ModifierModeleComponent` (Angular 17)
- **Fichiers CSS** :
  - `modifier-modele.component.css`
  - `modal-box.css`
  - `tables-common.css`
- **Données** :
  - Chargées depuis `GET /conventionServices`
  - Enrichies avec parsing des dates de dernière modification
- **Pagination** :
  - `paginatedModeles` calculés à partir de `filteredModeles`
- **Filtres** :
  - Normalisation insensible à la casse et aux accents (`normalize()`)
  - `applyFilters()` déclenchée à chaque saisie
- **Accessibilité** :
  - `aria-label`, `aria-current`, `aria-live`, navigation clavier
  - Focus automatique sur début de tableau après changement de page
- **UX** :
  - Icône pour modifier
  - Bouton "Réinitialiser"
  - Message temporaire de confirmation ou erreur

---

### Analyse du fichier remplacé

- Si un fichier est sélectionné :
  - Il est envoyé à `/test-generation`
  - Le backend renvoie les variables détectées
  - Le composant compare avec les variables attendues :
    ```ts
    [
      'annee', 'NOM_ORGANISME', 'ADR_ORGANISME', '...','STA_REMU_HOR'
    ]
    ```
  - Si des variables sont manquantes, un message d’erreur s’affiche
  - Si tout est bon : fichier accepté

---

### Exemple de message de succès

> ✅ Modèle mis à jour avec succès !

---

### Exemple de message d’erreur

> ⚠️ Le document est un modèle mais, il manque 2 variables : `TUT_IUT`, `MEL_ORGANISME`.

---

### Accès restreint

- **Rôle requis** : GESTIONNAIRE
- L’interface est désactivée/invisible pour les autres rôles

---

## Sous-onglet : Archiver un modèle

Ce sous-onglet permet au **Gestionnaire** d’archiver définitivement un modèle de convention devenu obsolète.

---

### Aperçu de l’interface
<div>
  <img src="./assets/images/page-archiver-modele.png" alt="Archiver un modèle – GenioService" width="600"/>
</div>


---

### Fonctionnement

1. **Filtres de recherche** :
  - Champ de texte pour rechercher un modèle par **nom**
  - Champ numérique pour filtrer par **année**
  - Bouton **Réinitialiser** pour rétablir tous les filtres

2. **Affichage des modèles** :
  - Tableau contenant : **Titre**, **Année**, **Format**, **Dernière modification**
  - Choix du nombre d’entrées par page (5, 10, 15)
  - Pagination avec navigation par page

3. **Action : Archiver un modèle** :
  - Icône dans la colonne "Action"
  - Clic sur l’icône ouvre une **modale de confirmation**
  - La modale affiche le **nom du modèle à archiver**
  - Boutons :
    - **Annuler** : fermeture sans action
    - **Archiver** : suppression immédiate via appel API

---

### Exigences fonctionnelles

- Seul le rôle **GESTIONNAIRE** a accès à ce sous-onglet
- L'archivage supprime le modèle de la base visible côté interface
- Confirmation obligatoire via modale
- Affichage d’un message de succès ou d’erreur

---

### Exigences techniques

- **Composant** : `SupprimerModeleComponent` (Angular)
- **Fichier CSS** : `supprimer-modele.component.css` + `modal-box.css` + `tables-common.css`
- **Données** :
  - Chargées depuis `GET /conventionServices`
  - Supprimées via `DELETE /conventionServices/{id}`
- **Pagination** :
  - Gérée via `paginatedModeles`
  - Recalculée après suppression
- **Accessibilité** :
  - Focus sur tableau après navigation
  - Modale avec `aria-modal="true"` et rôles accessibles
- **UX** :
  - Icône de suppression = (archive)
  - Modale explicite
  - Message de succès temporaire

---

### Exemple de message de succès

> ✅ Le modèle `modeleConvention_2022` a bien été archivé.

---

### Exemple de message d’erreur

> ⚠️ Une erreur est survenue lors de la suppression.

---

### Accès restreint

- **Rôle requis** : GESTIONNAIRE
- Fonctionnalité masquée pour les autres utilisateurs

## Écran de gestion des utilisateurs – Gestionnaire

### Objectif de l’écran

Permet aux **Gestionnaires** d’administrer les comptes utilisateurs de la plateforme GenioService.  
L’écran permet de visualiser, filtrer, activer/désactiver, modifier ou supprimer des comptes.

C’est l’unique interface d’administration des rôles et statuts des utilisateurs.

---

### Aperçu de la page
<div>
  <img src="./assets/images/page-gestion-utilisateur.png" alt="Écran de gestion des utilisateurs – Gestionnaire" width="600"/>
</div>

---

### Fonctionnement de l’écran

- Liste paginée de tous les comptes utilisateurs enregistrés
- Filtres disponibles :
  - **Email** (champ texte)
  - **Rôle** : Consultant, Gestionnaire, Exploitant, ou “NONE” pour les comptes en attente
  - **Statut du compte** : Actif / Non actif
- Colonnes affichées :
  - Nom, Prénom, Email
  - Statut (Actif / Inactif)
  - Rôle attribué
  - Date de création du compte
- Pour chaque utilisateur :
  -  **Modifier** : ouvre une modale pour éditer le rôle et le statut
  - **Supprimer** : ouvre une modale de confirmation
- Une **modale d’aide** ❔ en haut à droite explique le fonctionnement général de la page
- Un fil d’Ariane dynamique indique depuis quel écran l’administration a été ouverte (`queryParams.source`)

---

### Exigences fonctionnelles

- **Rôle requis** : Gestionnaire uniquement
- Actions possibles :
  -  Activer ou désactiver un compte
  - Modifier le rôle de l’utilisateur (via menu déroulant)
  - Supprimer un compte définitivement
- La **modale de modification** permet :
  - De voir les infos statiques (Nom, Prénom, Email)
  - De basculer l’activation (toggle)
  - De modifier le rôle via `select`
- Un bouton **Réinitialiser** permet de vider tous les filtres

---

### Accessibilité

- Icônes annotées (`aria-label`, `aria-hidden`)
- Modales accessibles (`role="dialog"`, `aria-modal`, `aria-labelledby`)
- Navigation clavier : `Tab`, `Enter`, `Esc`
- Affichage dynamique du rôle connecté et de l'origine de navigation
- Tous les boutons ont des alternatives textuelles `sr-only` pour les lecteurs d’écran

---

### Exigences techniques

- **Framework** : Angular 17
- **Composant principal** : `GestionUtilisateursComponent`
- **Requêtes API** :
  - `GET /api/utilisateurs` (liste complète)
  - `PUT /api/utilisateurs/:id/admin-update` (modification)
  - `DELETE /api/utilisateurs/:id` (suppression)
- **Filtrage local** en JS sur :
  - Email
  - Rôle (`CONSULTANT`, `EXPLOITANT`, etc.)
  - Statut (`actif: true / false`)
- **Pagination** configurable (5, 10, 15)
- **Sécurité** :
  - Contrôle strict du rôle Gestionnaire
  - Backend vérifie les droits à chaque opération
- **Modales** gérées dynamiquement :
  - `showEditModal` pour la modification
  - `userToDelete` pour la confirmation de suppression
- **Design & UX** :
  - Badge vert pour les comptes actifs
  - Tri par date de création (du plus récent au plus ancien)
  - Message de confirmation après modification ou suppression
  - Réactivité immédiate de la table après chaque action

---

### Résumé des actions

| Action       | Accessible | Modal ? | Requête API                    |
|--------------|------------|---------|-------------------------------|
| Modifier     | Oui        | Oui     | `PUT /api/utilisateurs/:id/admin-update` |
| Supprimer    | Oui        | Oui     | `DELETE /api/utilisateurs/:id` |
| Activer      | Oui        | Oui     | Incluse dans la requête PUT   |
| Filtrer      | Oui        | Non     | Traitement local              |

---

### Éléments d’assistance

- ❔ Bouton d’aide avec modale : fonctionnement général
- Fichier breadcrumb (`breadcrumbItems`) généré dynamiquement selon `queryParams.source` :
  - `?source=historique` → fil d’Ariane : Accueil > Historique > Gérer les utilisateurs
  - `?source=consulter` → Accueil > Modèles disponibles > Gérer les utilisateurs
  - `?source=gerer` → Accueil > Administration des modèles > Gérer les utilisateurs

---

## Page – Plan du site

### Objectif de l’écran

Permet aux utilisateurs (connectés ou non) d’avoir une **vue d’ensemble de la structure de l’application GenioService**, incluant :
- L’ensemble des pages accessibles (accueil, connexion, inscription…)
- Les actions spécifiques possibles selon les rôles
- Les restrictions d’accès affichées clairement via une modale

Cette page améliore la compréhension de l’architecture et des droits associés à chaque rôle (Consultant, Exploitant, Gestionnaire).

---

### Aperçu de la page
<div>
  <img src="./assets/images/page-plan-du-site.png" alt="Page du plan du site – GenioService" width="600"/>
</div>

---

### Fonctionnement de l’écran

- La page affiche une arborescence complète de toutes les sections du site :
  - **Accueil** : redirection vers les grandes sections
  - **Authentification** : pages de connexion, inscription, mot de passe oublié, réinitialisation
  - **Pages par rôle** :
    - Consultant : uniquement “Consulter les modèles”
    - Exploitant : consultation + visualisation de l’historique
    - Gestionnaire : accès à toutes les fonctionnalités (consultation, historique, gestion des utilisateurs, modèles, notifications)
  - **Bas de page** : liens vers les mentions légales, à propos, plan du site, etc.
- Une **légende** en haut permet de comprendre les icônes :
  - 🔵 lien vers une page
  - ⬛ action utilisateur (clic, ouverture)
  - 🔒 nécessite une authentification
- Lorsqu’une action réservée à un rôle est cliquée par un utilisateur non autorisé, une **modale “Accès restreint”** s’affiche.
- Le bouton “Fermer” referme la modale et remet l’utilisateur sur la même page.

---

### Exigences fonctionnelles

- **Accès ouvert à tous** (même sans connexion)
- Les actions accessibles dépendent du rôle utilisateur (simulées ici avec des boutons et une modale)
- Les boutons de type `fake-link` déclenchent une modale expliquant l’absence de droit
- L’affichage s’adapte automatiquement selon l’état de connexion via la propriété `userRole` (non connectée ou rôle défini)
- L’ordre des sections suit l’architecture réelle du site

---

### Exigences techniques

- **Framework** : Angular 17
- **Composant** : `PlanDuSiteComponent` (standalone)
- **Imports** :
  - `NgIf`, `RouterLink` pour la logique de rôle et navigation
  - `BreadcrumbComponent` pour le fil d’Ariane
- **Affichage conditionnel** :
  - `modalVisible` : contrôle l'affichage de la modale d’accès restreint
  - Méthodes `showModal()` et `closeModal()` déclenchent/ferment la modale
- **Accessibilité** :
  - Modale : `role="dialog"`, `aria-modal="true"`, `aria-labelledby`
  - Icônes annotées via `aria-hidden`
  - Texte lisible par lecteur d’écran (`sr-only`)
- **Styles appliqués** :
  - `plan-du-site.component.css`
  - `header.css`, `auth-shared.css`, `modal-box.css`

---

### Éléments supplémentaires

- Ce composant ne nécessite pas d’appel API.
- Peut être affiché à tout moment, même sans connexion.
- Aide les utilisateurs à comprendre **ce qu’ils peuvent faire selon leur rôle**.
- Cette page sert aussi de **point de test UX** pour la navigation et l'accessibilité.

---

## Page – À propos de GenioService

### Objectif de l’écran

Présenter la **finalité globale** de l’application GenioService, ses **fonctionnalités principales** et les **bénéfices concrets** pour les utilisateurs. Cette page a pour but de familiariser les utilisateurs avec l’outil avant même la connexion, et de rassurer sur sa sécurité, sa simplicité et sa valeur ajoutée.

---

### Aperçu de la page

<div>
  <img src="./assets/images/page-a-propos.png" alt="Page À propos de GenioService" width="600"/>
</div>

---

### Fonctionnement de l’écran

- Accessible sans authentification, directement depuis le **footer** ou l’URL `/a-propos`.
- Le contenu est organisé en sections :
  - **Présentation de GenioService** : définition de la plateforme
  - **Fonctionnalités principales** : liste des actions disponibles (consulter, ajouter, modifier…)
  - **Bénéfices** : sécurité, gain de temps, traçabilité
  - **Équipe projet** : courte description de l’équipe derrière l’outil
  - **Contact** : lien mailto pour écrire à l’équipe GenioService
- Chaque fonctionnalité (comme "Archiver", "Historique") est décrite de manière claire et orientée utilisateur.
- Les éléments visuels (icônes, balisage HTML) sont pensés pour une lecture fluide et accessible.

---

### Exigences fonctionnelles

- Page publique (pas besoin d’être connecté)
- Accessible depuis :
  - Le **footer** de chaque page
  - L'URL `/a-propos`
- Texte informatif uniquement, sans formulaire ni action utilisateur
- Lien fonctionnel vers l’adresse mail de contact
- Intégration du **fil d’Ariane** en haut de page

---

### Exigences techniques

- **Framework** : Angular 17
- **Composant** : `AboutComponent` (standalone)
- **Imports** :
  - `RouterLink` pour le breadcrumb
  - `NgIf` pour affichage conditionnel
  - `BreadcrumbComponent`
- **Structure HTML** :
  - `main.about-container`
  - `section.about-section` pour chaque bloc thématique
- **Accessibilité** :
  - Balises sémantiques `h2`, `ul`, `p`
  - Icônes marquées `aria-hidden="true"`
  - `aria-labelledby` lié à l’identifiant `#page-title` pour contextualiser la navigation
- **Contact mail** :
  - Lien `mailto:genioservice3@gmail.com` intégré avec protection typographique (`&#64;`)

---

### Métadonnées

- **Dernière mise à jour** : 29 mai 2025
- **Rédactrice** : Elsa HADJADJ
- **Version** : 1.0  

## Page – Contact

### Objectif de l’écran

Permettre aux visiteurs du site, connectés ou non, de **contacter le développeur** du projet GenioService en cas de question, retour, ou simple message. La page donne aussi un **contexte clair sur le projet universitaire**, son cadre pédagogique et ses technologies.

---

### Aperçu de la page
<div>
  <img src="./assets/images/page-contact.png" alt="Page Contact – GenioService" width="600"/>
</div>

---

### Fonctionnement de l’écran

- Accessible **publiquement** via le footer ou `/contact`.
- Deux sections principales :
  1. **Me contacter** :
    - Adresse email cliquable (mailto)
    - Lien LinkedIn vers le profil du développeur
    - Contexte : projet de 3e année du BUT Informatique, tutoré par M. Marcinkowski
    - Texte d’introduction encourageant le contact (ton cordial et humain)
  2. **À propos du projet** :
    - Détail sur le stack technique (Angular + Spring Boot)
    - Objectif purement pédagogique
    - Application des bonnes pratiques de développement web

---

### Exigences fonctionnelles

- Page disponible sans authentification
- Lien mail : `mailto:genioservice3@gmail.com`
- Lien LinkedIn sécurisé (avec `target="_blank"` et `rel="noopener"`)
- Présence d’un fil d’Ariane (`Accueil > Contact`)
- Aucun formulaire ou interaction utilisateur : **simple page informative**

---

### Exigences techniques

- **Framework** : Angular 17
- **Composant** : `ContactComponent` (standalone)
- **Imports** :
  - `RouterLink` pour la navigation
  - `NgIf` pour la logique conditionnelle éventuelle
  - `BreadcrumbComponent`
- **Accessibilité** :
  - `aria-labelledby="page-title"` pour le `main`
  - Icônes décoratives avec `aria-hidden="true"`
  - Mail et lien LinkedIn balisés avec attributs d’accessibilité
- **Email protégé** : `genioservice3&#64;gmail.com` (évite le scraping automatique)
- **Responsive** : mise en page fluide en desktop et mobile
- **Sécurité** :
  - Ouverture du lien LinkedIn dans un nouvel onglet
  - Aucune donnée utilisateur saisie ici (pas de backend requis)

---

## Page – Données personnelles & cookies

### Objectif de l’écran

Informer l’utilisateur des engagements de GenioService concernant la protection des données personnelles, les droits des utilisateurs, la nature des données collectées et l’usage des cookies. Cette page est obligatoire dans un cadre RGPD.

---

### Aperçu de la page

<div>
  <img src="./assets/images/page-donnees-personnelles.png" alt="Page Données personnelles et cookies – GenioService" width="600"/>
</div>

---

### Fonctionnement de l’écran

- Accessible publiquement depuis le footer (lien "Données personnelles").
- Structure en **6 sections explicites** :
  1. **Engagement de GenioService** : conformité au RGPD, pas de collecte sans consentement.
  2. **Vos droits** : suppression, opposition, limitation (droit à la vie privée).
    - Coordonnées de la DPO (Elsa Hadjadj) fournies (email et adresse postale).
    - Obligation de joindre une pièce d’identité pour toute demande.
  3. **Données collectées** : uniquement celles nécessaires à l’authentification et gestion.
    - Aucune donnée sensible stockée en clair.
  4. **Utilisation des cookies** : uniquement cookies techniques (aucun traceur pub).
  5. **Gérer vos préférences** : liens vers les paramètres de chaque navigateur.
  6. **Liens utiles** : vers le site de la CNIL pour plus d’infos.

---

### Exigences fonctionnelles

- Lien accessible dans le footer de toutes les pages.
- Tous les liens extérieurs ouvrent un nouvel onglet (`target="_blank"`, `rel="noopener"`).
- Mention claire de l’adresse du DPO avec deux canaux de contact (email + courrier postal).
- Mention des types de données collectées pour chaque fonctionnalité.
- Informations sur les droits de l’utilisateur (conformément à la CNIL).
- Mention explicite de l’absence de traceurs publicitaires.

---

### Exigences techniques

- **Framework** : Angular 17
- **Composant** : `DpoComponent` ou `DonnéesPersonnellesComponent` (standalone)
- **Imports** :
  - `NgIf`, `RouterLink`, `NgFor` si nécessaires
  - `BreadcrumbComponent`
- **Accessibilité** :
  - Utilisation de `aria-labelledby` pour le `<main>`
  - Icônes décoratives en `aria-hidden="true"`
  - Liens clairs et lisibles, sans pièges au clic
- **Sécurité** :
  - Pas de collecte de données actives (pas de formulaire)
  - Protection de l’adresse email avec entité HTML : `elsa.simha.hadjadj&#64;gmail.com`
- **Responsive** : contenu adapté aux tailles d’écran, bonne lisibilité mobile
- **Respect du RGPD** :
  - DPO nommément identifié
  - Modalités d’exercice des droits clairement listées
  - Liens vers la CNIL (source officielle)

---

### Complément

Cette page est essentielle pour :
- garantir la conformité **juridique** du site (RGPD),
- rassurer les utilisateurs sur la **sécurité** de leurs données,
- valoriser la **transparence** du projet GenioService.

---

## Page – Mentions légales

### Objectif de l’écran

Présenter les informations légales obligatoires liées à l'éditeur du site GenioService dans un cadre académique. Cette page garantit la transparence sur l’identité de l’auteur, les technologies utilisées, la propriété du code source, et les conditions d’usage du site.

---

### Aperçu de la page
<div>
  <img src="./assets/images/page-mentions-legales.png" alt="Page Mentions légales – GenioService" width="600"/>
</div>

---

### Fonctionnement de l’écran

- Accessible depuis le **footer** sur toutes les pages via le lien « Mentions légales ».
- Contenu réparti en **5 sections** :
  1. **Éditrice du site** : identité de l’auteure (Elsa Hadjadj), encadrement (Jérémy Marcinkowski), et localisation de l’établissement (IUT de Montreuil).
  2. **Technologies utilisées** : framework front-end (Angular), back-end (Spring Boot), responsive design, accessibilité.
  3. **Dépôts GitHub** : liens directs vers les dépôts frontend et backend publics.
  4. **Propriété intellectuelle** : licence Creative Commons **CC BY-NC-SA 4.0** (usage non commercial, attribution requise).
  5. **Hébergement** : mention que le site est exécuté localement à des fins pédagogiques (pas d’hébergeur professionnel impliqué).

---

### Exigences fonctionnelles

- Affichage clair et accessible des informations légales, à jour.
- Liens extérieurs sécurisés (`target="_blank"` et `rel="noopener"`).
- Mention explicite du cadre universitaire (BUT Informatique – IUT de Montreuil).
- Mention de la licence de partage du code source.
- Accès depuis toutes les pages via le pied de page.

---

### Exigences techniques

- **Composant Angular** : `MentionsLegalesComponent` (standalone)
- **Technos utilisées** :
  - Frontend : Angular (HTML5, CSS3, TypeScript)
  - Backend : Spring Boot (Java, REST API)
- **Imports nécessaires** :
  - `RouterLink`, `NgIf`, `BreadcrumbComponent`
- **Accessibilité** :
  - `<main>` avec `aria-labelledby`
  - Icônes décoratives en `aria-hidden`
- **Responsive** : lisibilité sur petit écran (mobile-first)
- **Sécurité** :
  - Aucun script ou fonctionnalité active sur cette page
  - Liens vérifiés et protégés par `rel="noopener"`

---

### Complément

Cette page est obligatoire en France, même dans un cadre académique, dès lors qu’un site est mis à disposition d’un public. Elle clarifie la responsabilité éditoriale et le périmètre d’utilisation du projet GenioService.

---