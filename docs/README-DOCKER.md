# Projet Tricol - Documentation Docker

## 📋 Description du Projet

Ce projet est composé de trois services principaux :

1. **PostgreSQL** - Base de données relationnelle
2. **Service d'Authentification** - API REST pour la gestion des utilisateurs et l'authentification
3. **Service de Gestion des Approvisionnements** - API REST pour la gestion des fournisseurs

Tous les services partagent la même base de données PostgreSQL (`tricol_db`) et communiquent via un réseau Docker privé.

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Réseau Docker: tricol-network            │
│                                                              │
│  ┌──────────────┐         ┌──────────────┐                 │
│  │   Service    │         │   Service    │                 │
│  │     Auth     │────────▶│     App      │                 │
│  │  Port: 8081  │         │  Port: 3000  │                 │
│  └──────┬───────┘         └──────┬───────┘                 │
│         │                        │                          │
│         │                        │                          │
│         ▼                        ▼                          │
│  ┌─────────────────────────────────────┐                   │
│  │         PostgreSQL                   │                   │
│  │         Port: 5442 (externe)        │                   │
│  │         Port: 5432 (interne)        │                   │
│  │         Base de données: tricol_db  │                   │
│  └─────────────────────────────────────┘                   │
└─────────────────────────────────────────────────────────────┘
```

---

## 📦 Prérequis

Avant de commencer, assurez-vous d'avoir installé :

- **Docker** (version 20.10 ou supérieure)
- **Docker Compose** (intégré dans Docker ou installé séparément)
- **Java 17** et **Maven** (pour la compilation locale si nécessaire)

### Vérifier l'installation

```bash
docker --version
docker compose version
```

---

## 🚀 Démarrage Rapide

### 1. Cloner le projet (si ce n'est pas déjà fait)

```bash
cd /home/happy/Bureau/tricol
```

### 2. Compiler les applications Java

Avant de lancer Docker, vous devez compiler les deux applications Spring Boot :

```bash
# Compiler le service d'authentification
cd Authontification
./mvnw clean package -DskipTests
cd ..

# Compiler le service de gestion des approvisionnements
cd Gestion-des-Approvisionnements-pour-Tricol---Module-Fournisseurs-spring-core-
./mvnw clean package -DskipTests
cd ..
```

### 3. Lancer tous les services avec Docker Compose

```bash
docker compose up -d
```

Cette commande va :
- Créer un réseau Docker nommé `tricol-network`
- Démarrer le conteneur PostgreSQL
- Créer la base de données `tricol_db` avec les permissions appropriées
- Attendre que PostgreSQL soit prêt (healthcheck)
- Démarrer le service d'authentification (port 8081)
- Démarrer le service de gestion des approvisionnements (port 3000)

### 4. Vérifier que tout fonctionne

```bash
# Vérifier l'état des conteneurs
docker compose ps

# Vérifier les logs
docker compose logs -f
```

---

## 🔧 Configuration

### Ports exposés

| Service | Port Interne | Port Externe | URL d'accès |
|---------|-------------|--------------|-------------|
| PostgreSQL | 5432 | 5442 | `localhost:5442` |
| Service Auth | 8080 | 8081 | `http://localhost:8081` |
| Service App | 8080 | 3000 | `http://localhost:3000` |

### Base de données

- **Nom de la base** : `tricol_db`
- **Utilisateur** : `tricol_user`
- **Mot de passe** : `tricol_pass123`
- **URL JDBC** : `jdbc:postgresql://localhost:5442/tricol_db` (depuis l'hôte)
- **URL JDBC** : `jdbc:postgresql://postgres:5432/tricol_db` (depuis les conteneurs)

### Variables d'environnement

Les variables d'environnement peuvent être configurées dans un fichier `.env` à la racine du projet :

```env
# JWT Configuration (pour le service Auth)
JWT_SECRET=your-secret-key-change-this-in-production
JWT_EXPIRATION=3600000
```

---

## 📡 Endpoints API

### Service d'Authentification (Port 8081)

#### 1. Enregistrer un utilisateur
```http
POST http://localhost:8081/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "userName": "John Doe",
  "role": "USER"
}
```

#### 2. Connexion
```http
POST http://localhost:8081/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

#### 3. Obtenir tous les utilisateurs
```http
GET http://localhost:8081/auth/users
```

### Service de Gestion des Approvisionnements (Port 3000)

Les endpoints sont accessibles via `http://localhost:3000/`

---

## 🛠️ Commandes Utiles

### Démarrage et Arrêt

```bash
# Démarrer tous les services
docker compose up -d

# Démarrer avec reconstruction des images
docker compose up -d --build

# Arrêter tous les services
docker compose down

# Arrêter et supprimer les volumes (⚠️ supprime les données)
docker compose down -v
```

### Logs et Débogage

```bash
# Voir les logs de tous les services
docker compose logs -f

# Voir les logs d'un service spécifique
docker compose logs -f auth
docker compose logs -f app
docker compose logs -f postgres

# Voir les 100 dernières lignes
docker compose logs --tail=100
```

### Gestion des Conteneurs

```bash
# Lister les conteneurs
docker compose ps

# Redémarrer un service spécifique
docker compose restart auth

# Reconstruire un service spécifique
docker compose up -d --build auth

# Entrer dans un conteneur
docker exec -it tricol-auth bash
docker exec -it tricol-postgres psql -U tricol_user -d tricol_db
```

### Nettoyage

```bash
# Supprimer tous les conteneurs arrêtés
docker container prune

# Supprimer toutes les images non utilisées
docker image prune -a

# Nettoyage complet (⚠️ attention)
docker system prune -a --volumes
```

---

## 🔍 Dépannage

### Problème : Les conteneurs ne démarrent pas

**Solution :**
```bash
# Vérifier les logs
docker compose logs

# Reconstruire depuis zéro
docker compose down -v
docker compose up -d --build
```

### Problème : Erreur 404 sur les endpoints

**Causes possibles :**
1. L'application n'a pas terminé son démarrage
2. Le fichier WAR n'a pas été compilé correctement

**Solution :**
```bash
# Attendre 10-15 secondes après le démarrage
sleep 15

# Vérifier que l'application est démarrée
docker compose logs auth | grep "Started"

# Recompiler et redéployer
cd Authontification
./mvnw clean package -DskipTests
cd ..
docker compose up -d --build auth
```

### Problème : Erreur de connexion à la base de données

**Solution :**
```bash
# Vérifier que PostgreSQL est en bonne santé
docker compose ps

# Le status doit être "healthy" pour postgres
# Si ce n'est pas le cas, redémarrer
docker compose restart postgres
```

### Problème : Port déjà utilisé

**Erreur :** `Bind for 0.0.0.0:8081 failed: port is already allocated`

**Solution :**
```bash
# Trouver le processus qui utilise le port
sudo lsof -i :8081

# Arrêter le processus ou changer le port dans docker-compose.yml
```

---

## 🗄️ Accès Direct à la Base de Données

### Via Docker

```bash
# Se connecter à PostgreSQL via le conteneur
docker exec -it tricol-postgres psql -U tricol_user -d tricol_db

# Lister les tables
\dt

# Voir les utilisateurs
SELECT * FROM users;

# Quitter
\q
```

### Via un client externe (DBeaver, pgAdmin, etc.)

- **Host:** localhost
- **Port:** 5442
- **Database:** tricol_db
- **Username:** tricol_user
- **Password:** tricol_pass123

---

## 📝 Structure du Projet

```
tricol/
├── docker-compose.yml              # Configuration Docker Compose
├── init-db.sql                     # Script d'initialisation de la base de données
├── README-DOCKER.md                # Ce fichier
├── .env                            # Variables d'environnement (à créer)
│
├── Authontification/               # Service d'authentification
│   ├── Dockerfile                  # Configuration Docker
│   ├── pom.xml                     # Configuration Maven
│   ├── src/                        # Code source Java
│   └── target/                     # Fichiers compilés (WAR)
│
└── Gestion-des-Approvisionnements-pour-Tricol---Module-Fournisseurs-spring-core-/
    ├── Dockerfile                  # Configuration Docker
    ├── pom.xml                     # Configuration Maven
    ├── src/                        # Code source Java
    └── target/                     # Fichiers compilés (WAR)
```

---

## 🔐 Sécurité

⚠️ **Important pour la production :**

1. Changez les mots de passe par défaut dans `docker-compose.yml`
2. Configurez une clé secrète forte pour JWT dans le fichier `.env`
3. Utilisez HTTPS en production
4. Ne commitez jamais le fichier `.env` dans Git
5. Limitez l'accès aux ports exposés avec un firewall

---

## 📚 Technologies Utilisées

- **Spring Boot 3.5.7** - Framework Java
- **PostgreSQL 15** - Base de données relationnelle
- **Docker & Docker Compose** - Conteneurisation
- **Apache Tomcat 10.1** - Serveur d'applications
- **Hibernate/JPA** - ORM
- **Maven** - Gestion de dépendances

---

## 👥 Contribution

Pour contribuer au projet :

1. Assurez-vous que le code compile sans erreurs
2. Testez localement avec Docker
3. Documentez les changements importants

---

## 📞 Support

En cas de problème :

1. Vérifiez d'abord les logs : `docker compose logs -f`
2. Consultez la section **Dépannage** de ce README
3. Assurez-vous que tous les prérequis sont installés

---

## 📅 Dernière mise à jour

**Date :** 25 octobre 2025  
**Version Docker Compose :** 3.8  
**Version Spring Boot :** 3.5.7

