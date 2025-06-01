# Déploiement en production

## Prérequis

Vous devez préalablement clonner les deux repository avant de lancer le docker compose.

- [x] Pour le frontend : git clone https://github.com/DUT-Info-Montreuil/genio-frontend.git
- [x] Pour le backend : git clone https://github.com/DUT-Info-Montreuil/genio-backend.git

## Lancement du docker compose

Lancer la commande de démmarege et de construiction de l'image dockerfile

```bash
docker compose up --build
```