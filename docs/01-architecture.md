# Documentation Architecture du Projet GenioService

---

## 1. Présentation générale

GenioService est une application Java basée sur Spring Boot (version 3.2.12) destinée à la gestion des conventions. Elle utilise Java 17 et suit les bonnes pratiques pour la modularité, la sécurité, les tests et la documentation.

---

## 2. Architecture Technique

### Structure du projet Maven

- **GroupId** : `com.genio.service`
- **ArtifactId** : `GenioService`
- **Version** : `1.0.0-RELEASE`
- **Parent** : `spring-boot-starter-parent` (version 3.2.12)

Le projet utilise Maven pour la gestion des dépendances, le cycle de vie de construction, les tests et le packaging.

### Technologies et frameworks clés

- **Java 17** : version minimale requise pour la compilation et exécution.
- **Spring Boot 3.2.12** : framework principal pour le développement web, sécurité, accès aux données, mailing et gestion des configurations.
- **Hibernate Validator 8.0.2** : validation des données via annotations (`@NotBlank`, `@Pattern`, etc.).
- **Jakarta Mail (version 2.0.x - 2.1.x)** : gestion de l’envoi d’emails.
- **Apache POI 5.4.0** : manipulation des fichiers Microsoft Office (extraction et génération de fichiers DOCX).
- **MySQL Connector/J 9.0.0** : connecteur JDBC pour la base de données MySQL.
- **Lombok 1.18.38** : réduction du code boilerplate grâce aux annotations (`@Getter`, `@Setter`, etc.).
- **Spring Security** : gestion de la sécurité, authentification et autorisation.
- **Spring Data JPA** : abstraction pour accès base de données avec Hibernate.
- **Spring Boot Starter Mail** : intégration simple du serveur mail.
- **Log4j2 2.24.3** : système de logging performant et flexible.
- **Spring Boot Starter Test + Mockito 5.7.0** : framework de tests unitaires et mocks.
- **Swagger (Springdoc OpenAPI)** : génération automatique de documentation API.
- **H2 Database** : base de données en mémoire pour les tests.
- **JaCoCo 0.8.8** : couverture de tests.

### Gestion des dépendances

- Exclusion des logs par défaut de Spring Boot (Logback) au profit de Log4j2.
- Inclusion de `spring-boot-starter-web` pour serveur web REST.
- Gestion des emails via `spring-boot-starter-mail` et Jakarta Mail.
- Sécurité avec `spring-boot-starter-security`.
- Persistance avec `spring-boot-starter-data-jpa`.
- Validation des données avec `spring-boot-starter-validation`.
- Tests avec `spring-boot-starter-test` et `mockito-core`.
- Swagger via `springdoc-openapi-starter-webmvc-ui`.

### Build et packaging

- **Spring Boot Maven Plugin** : facilite le packaging exécutable en `.jar`.
- **JaCoCo Maven Plugin** : collecte la couverture de tests pendant la phase `verify`.

### Bonnes pratiques et qualité

- Utilisation de Lombok pour réduire le code répétitif.
- Tests unitaires et d’intégration via Spring Boot Test et Mockito.
- Documentation Swagger automatiquement générée pour faciliter l’intégration.
- Sécurisation grâce à Spring Security et validation rigoureuse des données.
- Gestion des emails pour notifications et réinitialisation sécurisée des mots de passe.

---

## 3. Architecture Applicative

### Organisation du code

- **Package `com.genio.controller`** : contient les contrôleurs REST exposant les API.
- **Package `com.genio.service.impl`** : implémentations des services métiers.
- **Package `com.genio.dto`** : objets de transfert de données (DTO) entre couches.
- **Package `com.genio.model`** : entités JPA représentant les données persistées.
- **Package `com.genio.repository`** : interfaces pour accès aux données via Spring Data JPA.

### Couches applicatives

| Couche            | Rôle principal                                               | Technologies / Annotations            |
|-------------------|--------------------------------------------------------------|-------------------------------------|
| Controller        | Gérer les requêtes HTTP, valider, retourner des réponses JSON | Spring MVC REST (`@RestController`) |
| Service           | Implémenter la logique métier, validations, transactions      | `@Service`                         |
| Repository        | Accès à la base de données, opérations CRUD                   | Spring Data JPA (`JpaRepository`)  |
| Modèle / DTO      | Représenter les entités et objets de transfert                | JPA Entities, POJO DTOs             |

### Cycle de vie d’une requête typique

1. Le client envoie une requête HTTP REST (ex : POST `/auth/login`).
2. La couche **Controller** reçoit la requête, extrait les paramètres, valide.
3. Le **Service** applique la logique métier et interagit avec la couche **Repository** si nécessaire.
4. La couche **Repository** réalise les opérations en base de données.
5. La réponse remonte successivement jusqu’au **Controller** qui renvoie la réponse HTTP JSON au client.

### Composants supplémentaires importants

- **TokenService** : gestion des tokens d’authentification et réinitialisation.
- **MailService** : envoi d’emails (notifications, réinitialisations).
- **SecurityConfig** : configuration Spring Security (authentification, autorisation).
- **ExceptionHandlers** : gestion centralisée des erreurs et exceptions métiers.
- **SwaggerConfig** : documentation interactive des API REST.

---

## 4. Conclusion

L’architecture de GenioService est moderne, modulaire et maintenable.  
Elle garantit la sécurité, la qualité du code et facilite les tests tout en utilisant une stack technologique reconnue.

---
