# Déploiement en production

> ⚠️ **Note importante :**
>
> La configuration Docker est en cours de finalisation.
> À ce stade, l’application fonctionne parfaitement en local via IntelliJ avec la base MySQL, mais le déploiement via `docker compose` n’est pas encore opérationnel.
>
> La construction des images et le paramétrage de la communication entre les services (backend, frontend, base de données) nécessitent encore des ajustements.
>
> Faute de temps, cette étape n’a pas pu être terminée avant la remise.
> Une démonstration locale de l’application est possible à tout moment.

## Prérequis

Vous devez préalablement clonner les deux repository avant de lancer le docker compose.

- [x] Pour le frontend : git clone <https://github.com/DUT-Info-Montreuil/genio-frontend.git>
- [x] Pour le backend : git clone <https://github.com/DUT-Info-Montreuil/genio-backend.git>

## Organisation des projets

Par exemple pour la version 1.0.0 :

```plaintext
project-root/
├── genio-backend/
│   │   └── target/GenioService-1.0.0-RELEASE.jar
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

> Les fichiers sont présents dans le dossier DOCKER des projets genio-frontend et genio-backend.

## Lancement du docker compose

- [x] Copier à la racine du projet docker, ie. project-root le fichier `docker-compose.yml`
- [x] Copier à la racine du projet docker, ie. project-root le fichier `frontend.dockerfile`
- [x] Copier à la racine du projet docker, ie. project-root le fichier `backend.dockerfile`
- [ ] Modifier si besoin le fichier `docker-compose.yml` pour changer les informations d'authentification pour la base de données.
- [x] Les repos clonnés doivent avoir comme nom de dossier : genio-backend et genio-frontend
- [x] Le dossier `config` contient les fichiers initialisation de la base de données et de configuration pour le serveur web et l'application JAVA
- [x] Le fichier `application-external.properties.template` doit être renommé et mis à jour avec les informations de production

Lancer la commande de démmarege et de construiction de l'image dockerfile

```bash
docker compose up --build -d
```

## Ouverture de l'application

Dans un navigateur : <http://localhost:8100/genio/>
