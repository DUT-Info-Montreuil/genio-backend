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

- [x] Pour le frontend : git clone https://github.com/DUT-Info-Montreuil/genio-frontend.git
- [x] Pour le backend : git clone https://github.com/DUT-Info-Montreuil/genio-backend.git

## Lancement du docker compose

Lancer la commande de démmarege et de construiction de l'image dockerfile

```bash
docker compose up --build
```