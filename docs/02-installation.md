# GenioService - Documentation Installation Développeur

## 1. Pré-requis

- Java 17+
- Maven 3.8+
- Node.js 18+
- MySQL 9.0.1+
- Git
- Angular CLI

---

## 2. Création de la base de données

### 2.1 Créez la base MySQL

```sql
CREATE DATABASE genioservice_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2.2 Créez un utilisateur MySQL dédié

```sql
CREATE USER 'genio'@'localhost' IDENTIFIED BY 'motdepasse';
GRANT ALL PRIVILEGES ON genioservice_db.* TO 'genio'@'localhost';
FLUSH PRIVILEGES;
```

---

## 3. Préparation du projet GenioApp

### 3.1 Créez un dossier pour le projet

```bash
mkdir GenioApp
cd GenioApp
```

### 3.2 Créez deux sous-dossiers pour le backend et le frontend

```bash
mkdir backend
mkdir frontend
```

---

## 4. Cloner le backend

### 4.1 Clonez le dépôt backend

```bash
cd backend
git clone https://github.com/DUT-Info-Montreuil/genio-backend.git
```

---

## 5. Configuration de l'environnement backend

### 5.1 Copier le fichier de configuration

Un template est fourni dans le backend. Copiez le fichier `application-external.properties.template` situé dans le dossier `utils/` et renommez-le en `application-external.properties`.

```bash
cp utils/application-external.properties.template utils/application-external.properties
```

### 5.2 Modifier les variables du fichier `application-external.properties`

Ouvrez le fichier `utils/application-external.properties` et personnalisez les lignes suivantes :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/genioservice_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=genio
spring.datasource.password=motdepasse

server.port=8080

spring.mail.host=smtp-relay.brevo.com
spring.mail.port=587
spring.mail.username=your_email@example.com
spring.mail.password=your_email_password

modele.conventionServices.directory=src/main/resources/conventionServices
```

> ⚠️ Ce fichier **ne doit jamais être commité**. Il est déjà ignoré dans `.gitignore`.

---

## 6. Lancer le backend

### 6.1 En ligne de commande

Utilisez cette commande Maven depuis la racine du backend :

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.config.location=file:utils/application-external.properties"
```

### 6.2 Depuis IntelliJ avec la flèche verte ("chillish")

1. Ouvrez le fichier `GenioServiceApplication.java`
2. Cliquez sur l’icône verte à gauche ou créez une configuration via **Add Configuration**
3. Dans la section `VM options`, ajoutez :

```text
-Dspring-boot.run.arguments=--spring.config.location=file:utils/application-external.properties
```

---

## 7. Cloner le frontend

### 7.1 Accédez au dossier frontend

```bash
cd ../frontend
```

### 7.2 Clonez le dépôt frontend Angular

```bash
git clone https://github.com/DUT-Info-Montreuil/genio-frontend.git .
```

---

## 8. Installer les dépendances frontend et lancer l’application

### 8.1 Installer les dépendances

```bash
npm install
```

### 8.2 Lancer le serveur Angular

```bash
npm start
```

---

## 9. Utilisation et développement

- L’API backend est exposée sur le port `8080`.
- Le frontend Angular communique avec le backend via ce port.
- Toute modification backend nécessite de relancer le serveur Spring Boot.
- Toute modification frontend est rechargée automatiquement.

---

## 10. Notes supplémentaires

### 10.1 Propriétés externes

- Toujours utiliser `utils/application-external.properties` pour isoler les secrets.

### 10.2 Logs

- Les logs affichent les requêtes SQL et erreurs importantes par défaut.

### 10.3 Tests

```bash
./mvnw test
```

---

## 11. Résolution des problèmes courants

### Erreur CORS

- Vérifiez la configuration Spring Security ou les annotations `@CrossOrigin`.

### Connexion base de données

- Vérifiez que MySQL est démarré.
- Contrôlez les identifiants dans le fichier de configuration externe.

### Port déjà utilisé

```properties
server.port=8081
```

---

## 12. Structure du projet

```
GenioApp/
├── backend/
│   ├── src/
│   ├── utils/
│   │   └── application-external.properties
│   └── ...
├── frontend/
│   ├── src/
│   └── ...
```

---

## 13. Commandes utiles backend

```bash
./mvnw clean install
./mvnw test
./mvnw dependency:tree
```

---

## 14. Commandes utiles frontend

```bash
npm install
npm start
ng test
ng build --configuration production
```

---

## 15. Sécurité et bonnes pratiques

### Exemple `.gitignore`

```
# Backend
/backend/utils/application-external.properties

# Frontend
/node_modules/
/dist/

# Global
.env
```

---

## 16. Liens utiles

- [Backend GitHub](https://github.com/DUT-Info-Montreuil/GenioService)
- [Frontend GitHub](https://github.com/DUT-Info-Montreuil/genio-frontend)
- [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Angular Docs](https://angular.io/docs)
- [Brevo SMTP Docs](https://help.brevo.com/hc/fr/articles/209464189)

---

## 17. Mentions légales

- Conforme RGPD
- Accessibilité partielle conforme WCAG
- Pas de cookies tiers

---

## 18. Licence

Projet sous licence MIT. Voir le fichier `LICENCE.md`.

---

## 19. Contacts

- Elsa Hadjadj
- elsa.simha.hadjadj@gmail.com

---

© 2025 - GenioService - Tous droits réservés
