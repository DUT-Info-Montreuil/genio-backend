# Déploiement en production

## Prérequis

Vous devez préalablement clonner les deux repository avant de lancer le docker compose.

- [x] Pour le frontend : git clone <https://github.com/DUT-Info-Montreuil/genio-frontend.git>
- [x] Pour le backend : git clone <https://github.com/DUT-Info-Montreuil/genio-backend.git>

## Organisation des projets

Par exemple pour la version 1.0.1 :

```plaintext
genio-service/
├── genio-backend/
│   │   └── target/GenioService-1.0.1-RELEASE.jar
├── genio-frontend/
│   ├── nginx.conf
│   └── dist/genio-ui/
├── config/
│   ├── 01_schema.sql
│   ├── 02_init.sql
│   ├── log4j2.xml
│   ├── nginx.conf
│   └── application-external.properties.template
├── docker-compose.yml
├── frontend.dockefile
├── backend.dockefile
```

> Les fichiers sont présents dans le dossier DOCKER du projet `genio-backend`.

## Lancement du docker compose

> ⚠️ **Note à l’attention des développeurs**
>
> Les fichiers Docker sont préremplis avec des valeurs par défaut afin de permettre une exécution rapide avec un minimum de modifications.
> Cependant, **il est essentiel d’adapter ces fichiers en fonction de votre infrastructure**, notamment :
>
> - Le fichier `nginx.conf`, qui devra être ajusté selon votre configuration réseau.
> - Les fichiers `.env` ou `docker-compose.override.yml`, si utilisés.
>
> Dans ce projet, nous utilisons un **reverse proxy** tel que **Nginx Proxy Manager** pour exposer l'application frontend.
>
> - L’application est accessible via l’URL : `https://genioservice.<domain>/genio/`
> - Le frontend communique avec le backend via l’URL : `https://genioservice.<domain>/genio/api`
>
> Pensez à vérifier et modifier les chemins et ports si nécessaire pour qu’ils correspondent à votre environnement.

### Procédure de déploiement

#### 1. Copier les fichiers nécessaires à la racine du projet Docker (`project-root`)

- [x] `docker-compose.yml`
- [x] `frontend.dockerfile`
- [x] `backend.dockerfile` (dans le dossier `genesio-service`)

#### 2. 🛠️ Modifier le fichier d’environnement du frontend

- [ ] Éditer `environnement.prod.ts` pour définir l'URL de l'API :

  ```ts
  apiUrl: 'https://genioservice.<votre_domaine>/genio/api'
  ```

#### 3. Configurer l’accès à la base de données

- Renommer .env.template en .env

- [ ] Modifier .env pour y renseigner :
  - Le nom de la base de données
  - Le nom d'utilisateur
  - Le mot de passe

- [ ]  Vérifier la cohérence des paramètres avec :
  - Le fichier application-external.properties
  - Le script SQL d'initialisation (`01-init.sql` et `02-schema.sql`)

#### 4.  Modifier le docker-compose.yml si nécessaire

Adapter les ports ou les identifiants de la base de données selon votre environnement

- [ ] Ports utilisés par défaut :
  - MySQL : 3307:3306
  - Backend : 8101:8081
  - Frontend : 8100:8080

#### 5. Préparer les dossiers requis

- Cloner les dépôts avec les noms exacts :
  - genio-backend
  - genio-frontend
- Copier le dossier config depuis le dossier docker vers genio-service
- Vérifier et adapter le contenu du dossier config :
  - Script SQL d’initialisation
  - Configuration serveur
- Fichier application-external.properties.template :
  - Le renommer en application-external.properties
  - Mettre à jour les informations de production (DB, utilisateur, mot de passe)

#### 6. Configurer Nginx

 Adapter le fichier nginx.conf en fonction de votre infrastructure (domaine, proxy, certificats, etc.)

#### 7. Configurer le build du backend

- [ ] Modifier le fichier backend.dockerfile pour utiliser la bonne version du JAR :
  - Exemple : GenioService-1.0.1-RELEASE.jar

#### 8.Lancer la construction et le déploiement

```bash
docker compose up --build -d
```

## Ouverture de l'application

Dans un navigateur : `http://genio.<domain>/genio/`
