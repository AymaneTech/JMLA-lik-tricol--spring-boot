# 📦 Tricol - Module Gestion des Fournisseurs

![Version](https://img.shields.io/badge/version-0.0.1--SNAPSHOT-blue.svg)
![Java](https://img.shields.io/badge/Java-17-orange.svg)
![Spring](https://img.shields.io/badge/Spring-6.1.18-green.svg)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED.svg)
![License](https://img.shields.io/badge/license-MIT-blue.svg)

## 📋 Description

Application web de gestion des fournisseurs pour Tricol, développée avec Spring Framework (sans Spring Boot). Ce module permet la gestion complète des fournisseurs : création, modification, suppression et consultation.

## 🚀 Fonctionnalités (Version v0)

- ✅ **Affichage de tous les fournisseurs** - Consultation de la liste complète des fournisseurs
- ✅ **Ajout d'un fournisseur** - Création d'une nouvelle fiche fournisseur
- ✅ **Modification d'un fournisseur** - Mise à jour des informations d'un fournisseur existant
- ✅ **Suppression d'un fournisseur** - Suppression définitive d'un fournisseur
- ✅ **Recherche par nom** - Recherche de fournisseurs par nom

## 🛠️ Technologies Utilisées

### Backend
- **Java 17** - Langage de programmation
- **Spring Framework 6.1.18** - Framework principal
  - Spring MVC - Pour l'architecture REST
  - Spring Data JPA - Pour la persistance des données
  - Spring ORM - Pour l'intégration Hibernate
- **Hibernate 6.5.2** - ORM (Object-Relational Mapping)
- **PostgreSQL 42.7.3** - Base de données relationnelle
- **HikariCP 5.1.0** - Pool de connexions
- **Jakarta Validation API** - Validation des données
- **Jackson** - Sérialisation/Désérialisation JSON

### Outils de Build et Déploiement
- **Maven 3.x** - Gestion des dépendances et build
- **Jetty 11.0.15** - Serveur d'application (développement)
- **Tomcat 10.1** - Serveur d'application (production/Docker)
- **Docker & Docker Compose** - Containerisation et orchestration
- **Package WAR** - Format de déploiement

## 📁 Structure du Projet

```
src/
├── main/
│   ├── java/org/ismail/Tricol/
│   │   ├── controller/
│   │   │   └── FournisseurController.java      # Contrôleur REST
│   │   ├── model/
│   │   │   └── Fournisseur.java                # Entité JPA
│   │   ├── repository/
│   │   │   └── FournisseurRepository.java      # Interface JPA Repository
│   │   ├── service/
│   │   │   └── FournisseurService.java         # Logique métier
│   │   ├── ServletInitializer.java             # Configuration WAR
│   │   └── TricolApplication.java              # Classe principale
│   ├── resources/
│   │   └── application.properties              # Configuration application
│   └── webapp/WEB-INF/
│       ├── applicationContext.xml              # Configuration Spring
│       └── web.xml                             # Configuration Web
├── test/
│   └── java/org/ismail/Tricol/
│       └── TricolApplicationTests.java         # Tests unitaires
init-db.sql                                     # Script d'initialisation BDD
testEndPoints.http                              # Collection de tests API
```

## ⚙️ Prérequis

### Pour le développement local
- **Java JDK 17** ou supérieur
- **Maven 3.6+**
- **PostgreSQL 12+**
- **Git**

### Pour le déploiement Docker (Recommandé)
- **Docker 20.10+**
- **Docker Compose 1.29+**
- **Git**

## 🐳 Déploiement avec Docker (Version v0.1) - RECOMMANDÉ

### Installation de Docker et Docker Compose

#### Sur Kali Linux / Debian / Ubuntu

```bash
# Mettre à jour les paquets
sudo apt update

# Installer Docker Compose
sudo apt install docker-compose

# Vérifier les installations
docker --version
docker-compose --version

# S'assurer que Docker est démarré
sudo systemctl start docker
sudo systemctl enable docker

# Ajouter votre utilisateur au groupe docker (évite d'utiliser sudo)
sudo usermod -aG docker $USER
newgrp docker
```

### Architecture Docker

L'application utilise une architecture multi-conteneurs :

| Service | Image | Port | Description |
|---------|-------|------|-------------|
| **tricol-app-v0.1** | tomcat:10.1-jdk17 | 3000:8080 | Application Spring sur Tomcat |
| **tricol-postgres** | postgres:15-alpine | 5442:5432 | Base de données PostgreSQL |

Les deux conteneurs communiquent via un réseau Docker Bridge privé (`tricol-network`).

### Configuration

Les variables d'environnement sont définies dans le fichier `.env` :

```env
# PostgreSQL
POSTGRES_DB=tricol_db
POSTGRES_USER=tricol_user
POSTGRES_PASSWORD=tricol_pass123

# Spring app
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/tricol_db
SPRING_DATASOURCE_USERNAME=tricol_user
SPRING_DATASOURCE_PASSWORD=tricol_pass123

# Ports
APP_PORT=3000
POSTGRES_PORT=5442
```

### Démarrage Rapide avec Docker

```bash
# 1. Cloner le projet et se positionner sur la branche v0.1 (ou v0)
git clone https://github.com/ismailbaoud/Gestion-des-Approvisionnements-pour-Tricol---Module-Fournisseurs-spring-core-.git
cd Gestion-des-Approvisionnements-pour-Tricol---Module-Fournisseurs-spring-core-

# 2. Compiler le projet (génère le fichier WAR)
mvn clean package -DskipTests

# 3. Construire et démarrer les conteneurs
docker-compose up -d --build

# 4. Vérifier que les conteneurs sont en cours d'exécution
docker-compose ps

# 5. Consulter les logs en temps réel
docker-compose logs -f tricol-app-v0.1

# 6. Tester l'application (attendre ~30 secondes pour le démarrage complet)
curl http://localhost:3000/api/v0/fournisseurs
```

**L'application sera accessible sur** : `http://localhost:3000`

### Commandes Docker Utiles

#### Gestion des conteneurs

```bash
# Démarrer les conteneurs
docker-compose up -d

# Arrêter les conteneurs
docker-compose down

# Arrêter et supprimer les volumes (⚠️ efface la base de données)
docker-compose down -v

# Redémarrer les conteneurs
docker-compose restart

# Reconstruire l'image après modification du code
mvn clean package -DskipTests
docker-compose up -d --build
```

#### Consultation des logs

```bash
# Logs de tous les services
docker-compose logs -f

# Logs de l'application uniquement
docker-compose logs -f tricol-app-v0.1

# Logs de PostgreSQL uniquement
docker-compose logs -f tricol-postgres

# Dernières 100 lignes
docker-compose logs --tail=100 tricol-app-v0.1
```

#### Accès aux conteneurs

```bash
# Shell dans le conteneur de l'application
docker exec -it tricol-app-v0.1 bash

# Shell dans le conteneur PostgreSQL
docker exec -it tricol-postgres bash

# Accès direct à PostgreSQL
docker exec -it tricol-postgres psql -U tricol_user -d tricol_db
```

#### Commandes PostgreSQL dans le conteneur

```bash
# Se connecter à PostgreSQL
docker exec -it tricol-postgres psql -U tricol_user -d tricol_db

# Lister les tables
\dt

# Voir les fournisseurs
SELECT * FROM fournisseur;

# Quitter
\q
```

#### Nettoyage

```bash
# Supprimer les conteneurs arrêtés
docker-compose rm

# Nettoyer les images non utilisées
docker image prune -a

# Nettoyer tout (⚠️ attention)
docker system prune -a --volumes
```

### Résolution des Problèmes Docker

#### Les conteneurs ne démarrent pas

```bash
# Vérifier les logs pour voir l'erreur
docker-compose logs

# Nettoyer et redémarrer
docker-compose down -v
mvn clean package -DskipTests
docker-compose up -d --build
```

#### Erreur "no main manifest attribute"

```bash
# Recompiler le projet
mvn clean package -DskipTests

# Vérifier que le fichier WAR existe
ls -lh target/*.war

# Reconstruire l'image
docker-compose build --no-cache
docker-compose up -d
```

#### Erreur de connexion à la base de données

```bash
# Vérifier que PostgreSQL est démarré
docker-compose ps

# Vérifier les logs de PostgreSQL
docker-compose logs tricol-postgres

# La base de données peut prendre 10-15 secondes pour être prête
# Attendre et vérifier les logs de l'application
docker-compose logs -f tricol-app-v0.1
```

#### Port 3000 ou 5442 déjà utilisé

Modifier le fichier `.env` :

```env
APP_PORT=8080      # Au lieu de 3000
POSTGRES_PORT=5433 # Au lieu de 5442
```

Puis dans `docker-compose.yml`, modifier :

```yaml
ports:
  - "${APP_PORT}:8080"           # Pour l'app
  - "${POSTGRES_PORT}:5432"      # Pour PostgreSQL
```

#### Le conteneur s'arrête immédiatement

```bash
# Voir les logs du conteneur qui a échoué
docker logs tricol-app-v0.1

# Vérifier les logs de sortie
docker-compose logs tricol-app-v0.1

# Problème courant : JVM cgroups - utiliser une image compatible
```

### Avantages du Déploiement Docker

✅ **Portabilité** : Fonctionne sur n'importe quelle machine avec Docker  
✅ **Isolation** : Pas de conflit avec d'autres services  
✅ **Rapidité** : Déploiement en quelques commandes  
✅ **Reproductibilité** : Même environnement pour tous  
✅ **Scalabilité** : Facile d'ajouter des réplicas  

## 🔧 Installation et Configuration (Mode Manuel)

### 1. Cloner le Projet

```bash
git clone <repository-url>
cd Gestion-des-Approvisionnements-pour-Tricol---Module-Fournisseurs-spring-core-
git checkout v0
```

### 2. Configuration de la Base de Données

#### Créer la base de données PostgreSQL

```bash
# Se connecter à PostgreSQL
sudo -u postgres psql

# Exécuter le script d'initialisation
\i init-db.sql
```

Ou manuellement :

```sql
CREATE USER tricol_user WITH PASSWORD 'tricol_pass123';
CREATE DATABASE tricol_db OWNER tricol_user;
GRANT ALL PRIVILEGES ON DATABASE tricol_db TO tricol_user;
```

#### Configurer les paramètres de connexion

Modifier le fichier `src/main/resources/application.properties` si nécessaire :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tricol_db
spring.datasource.username=tricol_user
spring.datasource.password=tricol_pass123
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3. Compiler le Projet

```bash
# Nettoyer et compiler
mvn clean install

# Ou compiler sans les tests
mvn clean install -DskipTests
```

## 🚀 Démarrage de l'Application

### Option 1 : Docker (Recommandé) ⭐

Voir la section [🐳 Déploiement avec Docker](#-déploiement-avec-docker-version-v01---recommandé) ci-dessus.

### Option 2 : Mode Développement avec Jetty

```bash
# Démarrer sur le port 8080 (par défaut)
mvn jetty:run

# Démarrer sur un port personnalisé
mvn jetty:run -Djetty.http.port=9090
```

L'application sera accessible sur : `http://localhost:8080` (ou le port configuré)

### Option 3 : Déploiement en Production (WAR)

```bash
# Générer le fichier WAR
mvn clean package

# Le fichier WAR sera généré dans : target/Tricol-0.0.1-SNAPSHOT.war
# Déployer sur Tomcat, Jetty ou tout autre serveur d'applications Java EE
```

## 📡 API REST - Endpoints

Base URL : 
- **Docker** : `http://localhost:3000/api/v0`
- **Local** : `http://localhost:8080/api/v0`

### Fournisseurs

| Méthode | Endpoint | Description | Body |
|---------|----------|-------------|------|
| `GET` | `/fournisseurs` | Liste tous les fournisseurs | - |
| `GET` | `/fournisseurs/{id}` | Récupère un fournisseur par ID | - |
| `GET` | `/fournisseurs/nom/{nom}` | Recherche par nom | - |
| `POST` | `/fournisseurs` | Crée un nouveau fournisseur | JSON |
| `PUT` | `/fournisseurs/{id}` | Modifie un fournisseur existant | JSON |
| `DELETE` | `/fournisseurs/{id}` | Supprime un fournisseur | - |

### Exemples de Requêtes

#### Récupérer tous les fournisseurs
```http
GET http://localhost:3000/api/v0/fournisseurs
```

#### Créer un nouveau fournisseur
```http
POST http://localhost:3000/api/v0/fournisseurs
Content-Type: application/json

{
  "nom": "Alami",
  "prenom": "Hassan",
  "email": "hassan@example.com",
  "societe": "TechCorp",
  "adresse": "123 Rue Mohammed V",
  "contact": "Manager",
  "telephone": "0612345678",
  "ville": "Casablanca",
  "ice": "001234567890123"
}
```

#### Modifier un fournisseur
```http
PUT http://localhost:3000/api/v0/fournisseurs/1
Content-Type: application/json

{
  "nom": "Alami",
  "prenom": "Hassan",
  "email": "hassan.updated@example.com",
  "societe": "TechCorp SA",
  "adresse": "456 Rue Updated",
  "contact": "Director",
  "telephone": "0698765432",
  "ville": "Rabat",
  "ice": "009876543210987"
}
```

#### Supprimer un fournisseur
```http
DELETE http://localhost:3000/api/v0/fournisseurs/1
```

#### Rechercher par nom
```http
GET http://localhost:3000/api/v0/fournisseurs/nom/Alami
```

## 🧪 Tests

### Exécuter les Tests Unitaires

```bash
mvn test
```

### Tests Manuels des Endpoints

Utiliser le fichier `testEndPoints.http` avec un client REST (IntelliJ IDEA HTTP Client, VS Code REST Client, Postman, etc.)

## 📊 Modèle de Données

### Entité Fournisseur

| Champ | Type | Contraintes | Description |
|-------|------|-------------|-------------|
| `id` | Long | Primary Key, Auto-increment | Identifiant unique |
| `nom` | String | NOT NULL | Nom du fournisseur |
| `prenom` | String | NOT NULL | Prénom du contact |
| `email` | String | NOT NULL, Email | Email de contact |
| `societe` | String | NOT NULL | Nom de la société |
| `adresse` | String | NOT NULL | Adresse postale |
| `contact` | String | NOT NULL | Nom du contact |
| `telephone` | String | NOT NULL | Numéro de téléphone |
| `ville` | String | - | Ville |
| `ice` | String | - | Identifiant Commun de l'Entreprise |
| `version` | Integer | Optimistic Locking | Version pour la gestion concurrentielle |

## 🔒 Validation des Données

- **Email** : Format email valide requis
- **Champs obligatoires** : nom, prenom, email, societe, adresse, contact, telephone
- **Validation** : Effectuée côté serveur avec Jakarta Validation API

## 🐛 Dépannage

### Avec Docker

Voir la section [Résolution des Problèmes Docker](#résolution-des-problèmes-docker) ci-dessus.

### Mode Manuel (sans Docker)

#### Problème de connexion à la base de données

```bash
# Vérifier que PostgreSQL est démarré
sudo systemctl status postgresql

# Vérifier les credentials dans application.properties
```

#### Erreur de port déjà utilisé

```bash
# Changer le port Jetty
mvn jetty:run -Djetty.http.port=9090
```

#### Erreur de compilation Maven

```bash
# Nettoyer le cache Maven
mvn clean
rm -rf ~/.m2/repository
mvn install
```

## 📝 Logs

### Avec Docker
```bash
docker-compose logs -f tricol-app-v0.1
```

### Mode Manuel
Les logs de l'application sont disponibles dans :
- Console de sortie standard
- Fichiers générés : `jetty.log`, `jetty9090.log`

## 🗺️ Roadmap

### Version v0.1 (Actuelle avec Docker)
- [x] CRUD complet des fournisseurs
- [x] API REST
- [x] Validation des données
- [x] Configuration Spring sans Spring Boot
- [x] Déploiement Docker avec Docker Compose
- [x] Base de données PostgreSQL containerisée

### Versions Futures
- [ ] Authentification et autorisation (JWT)
- [ ] Pagination et tri
- [ ] Recherche avancée avec filtres
- [ ] Export des données (PDF, Excel)
- [ ] Interface utilisateur (Frontend React/Angular)
- [ ] Documentation API avec Swagger/OpenAPI
- [ ] Tests d'intégration et E2E
- [ ] CI/CD Pipeline
- [ ] Monitoring avec Prometheus/Grafana
- [ ] Kubernetes deployment

## 👥 Auteur

**Ismail**
- Organisation : Tricol

## 📄 License

Ce projet est sous licence MIT - voir le fichier LICENSE pour plus de détails.

## 🤝 Contribution

Les contributions sont les bienvenues ! Pour contribuer :

1. Fork le projet
2. Créer une branche feature (`git checkout -b feature/AmazingFeature`)
3. Commit les changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

## 📞 Support

Pour toute question ou problème, veuillez ouvrir une issue sur le repository GitHub.

---

**Note** : Ce README correspond à la version v0.1 du projet avec support Docker. Pour un déploiement rapide et facile, utilisez Docker Compose comme indiqué dans la section [🐳 Déploiement avec Docker](#-déploiement-avec-docker-version-v01---recommandé).

**Quick Start Docker** :
```bash
mvn clean package -DskipTests && docker-compose up -d --build
```
