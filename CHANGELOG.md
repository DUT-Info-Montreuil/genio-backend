# Liste des évolutions et défauts corrigés

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

---