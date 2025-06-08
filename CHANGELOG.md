# Liste des évolutions et défauts corrigés

## v1.0.1 - 08/06/2025 - Mise à jour environnement & RGPD

### Front-End

* [Configuration] Ajout d’une barrière d’environnement pour personnaliser l’URL backend (remplacement de l’URL en dur `http://localhost:8080` par une variable d’environnement configurable).
* [RGPD] Ajout de la case à cocher obligatoire RGPD dans le formulaire d’inscription.
* [Green IT] Intégration de bonnes pratiques d’éco-conception numérique (optimisation des images, chargement paresseux, réduction des requêtes).
* [Gestion_Erreurs] Messages d’erreur améliorés et gestion esthétique avec croix ✘ pour fermeture manuelle.
* [UX] Ajustements mineurs pour la cohérence des messages et feedback utilisateurs.
* [Modale] Harmonisation des boutons :
  * Centrage vertical parfait du texte (plus de décalage vers le bas) ;
  * Couleurs unifiées pour `.primary`, `.secondary`, `.danger`, `.close-modal-btn` ;
  * Suppression du fond gris inattendu à la fermeture ;
* [Footer] Affichage dynamique de la version depuis `package.json` (automatique) dans le pied de page.

### Back-End

* [API] Adaptation pour prendre en compte la configuration dynamique de l’URL backend via profils d’environnement.
* [Sécurité] Validation RGPD côté backend des consentements utilisateurs.
* [Logs] Amélioration des logs pour tracer les accès et modifications liées à la RGPD.

### Technique

* [Docker] Mise en place complète de la configuration :
  * Déploiement avec reverse proxy ;
  * Intégration des variables d’environnement Angular dans le build ;
  * Volume pour la base de données configuré.
* [Compilation] Compilation Angular avec prise en compte des variables d’environnement.

### Tests Unitaires

* [Couverture] Tests sur les nouvelles fonctionnalités d’environnement et RGPD.
* [Stabilité] Aucune régression détectée lors des tests locaux.
* [Validation] Tests manuels sur les modales, le footer dynamique, et les interactions utilisateur.

---

## v1.0.0 - 29/05/2025 - Release

### Front-End

* [Page_Accueil] Création d'une page d’accueil avec navigation vers login / inscription ;
* [Page_Connexion] Interface sécurisée avec messages d’erreur clairs ;
* [Page_Inscription]
  * Vérification en temps réel de l’unicité de l’email ;
  * Affichage dynamique des règles de mot de passe ;
  * Gestion esthétique des erreurs (croix ✘, messages fermables) ;
* [Gestion_Utilisateurs] Interface pour activer / désactiver / modifier les rôles ;
* [Gestion_Modèles] Interface complète pour :
  * Ajouter un modèle (.docx) ;
  * Modifier un modèle existant ;
  * Archiver un modèle ;
  * Visualiser les modèles par année ;
* [Design] Structure propre et responsive avec composants Angular ;
* [Accessibilité] Respect des critères WCAG (clavier, focus, contraste, ARIA) ;

### Back-End

* [Spring Boot] Socle Java 17 + Spring Boot 3.2.5 ;
* [Sécurité] Mise en place de Spring Security (CSRF, rôles, filtres personnalisés) ;
* [API_REST] Contrôleurs REST complets :
  * `/api/utilisateurs` (CRUD complet + rôles) ;
  * `/api/modeles` (CRUD avec DOCX + année) ;
* [Service] `GenioServiceImpl` :
  * Injection de données dans modèles .docx ;
  * Historisation des erreurs et conventions ;
  * Validation métier modulaire (via `ValidationStrategy`) ;
  * Archivage intelligent ;
* [Gestion_Erreurs] Centralisation via `ExceptionHandler` global (erreurs techniques et métiers) ;

### Technique

* [Logger] Intégration complète de Log4j2 (logs structurés) ;
* [Swagger] Documentation des API via Swagger/OpenAPI (auto-généré) ;

### Tests Unitaires

* [Couverture] 81.6% de couverture via JaCoCo ;
* [Tests_Service] Cas métiers couverts :
  * Génération de conventions ;
  * Manque de données (ex : tuteur absent) ;
  * Cas d’échec technique ou métier ;
* [Tests_Controller] MockMvc avec SpringBootTest et WebMvcTest ;
* [Annotations] `@ParameterizedTest`, `@Transactional`, `@Rollback`, `@MockBean` ;
* [Nettoyage] Suppression des stubs inutiles (`UnnecessaryStubbingException` corrigé) ;

### Base de Données

* [Entity_Modele] Stockage :
  * Fichier binaire ;
  * SHA256 hash ;
  * Nom, titre, année, statut d’archivage ;
* [Contraintes] Clé unique sur `fichierHash` ;
* [Requêtes] `findByAnnee`, `existsById`, `findLatestNonArchived()` ;

### Documentation

* [README] Présentation de l’application, GitHub, version, audit SonarCube ;
* [Sommaire] Liens cliquables vers les sections du projet ;
* [Docs] Fichiers `docs/*.md` : architecture, sécurité, accessibilité, RGPD, UX, tests, etc. ;
* [Audit_SonarCube] Image du Quality Gate et détails :
  * ✅ 0 bug, 0 vulnérabilité, 0 code smell ;
  * ✅ 295 tests unitaires ;
  * ✅ 0.7% duplication de code ;