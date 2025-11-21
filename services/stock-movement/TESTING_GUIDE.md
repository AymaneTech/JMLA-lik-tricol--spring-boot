# Guide d'utilisation du fichier tests.http

## 📋 Prérequis

1. **Service MouvementStock** doit être démarré sur `http://localhost:8082`
2. **Product Service** doit être accessible sur `http://localhost:8085`
3. **PostgreSQL** doit être démarré avec la base de données `tricol_db`

## 🗄️ Configuration PostgreSQL

### Créer la base de données (si nécessaire)
```sql
-- Se connecter à PostgreSQL
psql -U postgres

-- Créer l'utilisateur
CREATE USER tricol_user WITH PASSWORD 'tricol_pass123';

-- Créer la base de données
CREATE DATABASE tricol_db OWNER tricol_user;

-- Donner les privilèges
GRANT ALL PRIVILEGES ON DATABASE tricol_db TO tricol_user;
```

### Vérifier la connexion
```bash
psql -U tricol_user -d tricol_db -h localhost -p 5432
```

## 🚀 Lancer le service

```bash
cd /home/happy/Bureau/tricol/MouvementStock
./mvnw spring-boot:run
```

## 🔧 Outils compatibles

### Option 1: IntelliJ IDEA (Recommandé)
1. Ouvrir le fichier `tests.http`
2. Cliquer sur l'icône ▶️ (Run) à gauche de chaque requête
3. Les résultats s'affichent dans un panneau séparé

### Option 2: VSCode + REST Client Extension
1. Installer l'extension "REST Client" de Huachao Mao
2. Ouvrir `tests.http`
3. Cliquer sur "Send Request" au-dessus de chaque requête

### Option 3: Postman
Importer le fichier ou créer les requêtes manuellement

## 📝 Structure des tests

### 1️⃣ Tests de création (POST)
- ✅ Mouvement ENTREE
- ✅ Mouvement SORTIE
- ✅ Mouvement AJUSTEMENT
- ❌ Produit inexistant (erreur 404)
- ❌ Quantité négative (erreur 400)

### 2️⃣ Tests de lecture (GET)
- Liste complète avec pagination
- Filtrage par produit
- Filtrage par type
- Filtrage par commande

### 3️⃣ Tests de suppression (DELETE)
- Suppression normale
- Suppression d'un mouvement inexistant (erreur 404)

### 4️⃣ Scénarios complets
- Flux complet de mouvements pour un produit
- Mouvements liés à une commande

### 5️⃣ Tests de validation
- Champs null
- Types invalides
- Valeurs incorrectes

## 📊 Codes de réponse attendus

| Code | Signification | Cas d'usage |
|------|---------------|-------------|
| 200 | OK | GET réussi |
| 201 | Created | POST réussi |
| 204 | No Content | DELETE réussi |
| 400 | Bad Request | Données invalides |
| 404 | Not Found | Ressource inexistante |
| 500 | Internal Error | Erreur serveur |

## 🎯 Ordre recommandé d'exécution

1. **Vérifier PostgreSQL** est démarré et la base `tricol_db` existe
2. **Démarrer les services** (MouvementStock + Product)
3. **Créer un produit** dans Product Service (voir section BONUS)
4. **Créer des mouvements** (tests 1-3)
5. **Lister les mouvements** (tests 6-9)
6. **Filtrer par critères** (tests 10-17)
7. **Tester la suppression** (tests 18-19)
8. **Exécuter les scénarios complets**

## 🔍 Accès à PostgreSQL

### Via ligne de commande
```bash
psql -U tricol_user -d tricol_db -h localhost -p 5432
```

### Requêtes SQL utiles
```sql
-- Voir tous les mouvements
SELECT * FROM mouvement_stock;

-- Compter les mouvements par type
SELECT type_mvt, COUNT(*) 
FROM mouvement_stock 
GROUP BY type_mvt;

-- Mouvements pour un produit
SELECT * FROM mouvement_stock 
WHERE produit_id = 1 
ORDER BY date_mvt DESC;

-- Mouvements avec jointure (si tables liées)
SELECT m.*, m.type_mvt, m.quantite, m.date_mvt
FROM mouvement_stock m
WHERE m.produit_id = 1;

-- Total des mouvements par type
SELECT 
    type_mvt,
    COUNT(*) as nombre_mouvements,
    SUM(quantite) as quantite_totale
FROM mouvement_stock
GROUP BY type_mvt;

-- Historique complet d'un produit
SELECT 
    date_mvt,
    type_mvt,
    quantite,
    prix_achat,
    ref_commande
FROM mouvement_stock
WHERE produit_id = 1
ORDER BY date_mvt DESC;
```

### Vérifier la structure de la table
```sql
-- Décrire la table
\d mouvement_stock

-- Voir toutes les tables
\dt

-- Voir les index
\di
```

## 🐛 Dépannage

### Erreur de connexion PostgreSQL
```
org.postgresql.util.PSQLException: Connection refused
```
➡️ Vérifier que PostgreSQL est démarré:
```bash
sudo systemctl status postgresql
sudo systemctl start postgresql
```

### Erreur d'authentification
```
FATAL: password authentication failed for user "tricol_user"
```
➡️ Vérifier les credentials dans `application.properties`

### Base de données inexistante
```
FATAL: database "tricol_db" does not exist
```
➡️ Créer la base de données (voir section Configuration PostgreSQL)

### Erreur de connexion au service
```
Connection refused
```
➡️ Vérifier que le service est démarré sur le bon port

### Erreur 404 sur tous les endpoints
```
404 Not Found
```
➡️ Vérifier l'URL de base: `http://localhost:8082/api/v1/mouvements`

### Erreur 500 lors de la création
```
Internal Server Error
```
➡️ Vérifier que le Product Service est accessible et que le produit existe

## 📚 Exemples de requêtes curl (alternative)

```bash
# Créer un mouvement
curl -X POST http://localhost:8082/api/v1/mouvements \
  -H "Content-Type: application/json" \
  -d '{
    "produitId": 1,
    "typeMvt": "ENTREE",
    "quantite": 100,
    "prixAchat": 25.50,
    "refCommande": 12345
  }'

# Lister tous les mouvements
curl http://localhost:8082/api/v1/mouvements

# Mouvements par produit
curl http://localhost:8082/api/v1/mouvements/produit/1

# Mouvements par type
curl http://localhost:8082/api/v1/mouvements/type/ENTREE

# Supprimer un mouvement
curl -X DELETE http://localhost:8082/api/v1/mouvements/1
```

## 🗄️ Outils de gestion PostgreSQL

### pgAdmin 4 (GUI)
Interface graphique pour gérer PostgreSQL
- URL: http://localhost:5050 (si installé)

### DBeaver
Client SQL universel gratuit
- Supporte PostgreSQL
- Interface intuitive

### IntelliJ IDEA Database Tool
- View → Tool Windows → Database
- Ajouter une connexion PostgreSQL
- Host: localhost, Port: 5432
- Database: tricol_db

## ✅ Checklist avant de commencer

- [ ] PostgreSQL est démarré
- [ ] Base de données `tricol_db` existe
- [ ] Utilisateur `tricol_user` a les droits
- [ ] MouvementStock service est démarré
- [ ] Product Service est accessible
- [ ] Au moins un produit existe dans Product Service
- [ ] Outil de test REST est installé (IntelliJ/VSCode/Postman)
- [ ] Le fichier tests.http est ouvert

## 📊 Monitoring PostgreSQL

### Voir les connexions actives
```sql
SELECT * FROM pg_stat_activity 
WHERE datname = 'tricol_db';
```

### Voir la taille de la base
```sql
SELECT pg_size_pretty(pg_database_size('tricol_db'));
```

### Voir les tables et leur taille
```sql
SELECT 
    schemaname,
    tablename,
    pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) AS size
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;
```

Bon test ! 🚀
###
### MouvementStock Microservice - REST API Tests
### Base URL: http://localhost:8082/api/v1/mouvements
### Product Service should be running on http://localhost:8085
###

### Variables
@baseUrl = http://localhost:8082/api/v1/mouvements
@productServiceUrl = http://localhost:8085/api/v1/products

###############################################################################
### 1. CREATE - Créer un nouveau mouvement de stock (ENTREE)
###############################################################################

POST {{baseUrl}}
Content-Type: application/json

{
  "produitId": 1,
  "typeMvt": "ENTREE",
  "quantite": 100,
  "prixAchat": 25.50,
  "refCommande": 12345
}

###############################################################################
### 2. CREATE - Créer un mouvement de type SORTIE
###############################################################################

POST {{baseUrl}}
Content-Type: application/json

{
  "produitId": 1,
  "typeMvt": "SORTIE",
  "quantite": 20,
  "prixAchat": null,
  "refCommande": null
}

###############################################################################
### 3. CREATE - Créer un mouvement de type AJUSTEMENT
###############################################################################

POST {{baseUrl}}
Content-Type: application/json

{
  "produitId": 1,
  "typeMvt": "AJUSTEMENT",
  "quantite": 50,
  "prixAchat": null,
  "refCommande": null
}

###############################################################################
### 4. CREATE - Mouvement avec un produit qui n'existe pas (Test d'erreur)
###############################################################################

POST {{baseUrl}}
Content-Type: application/json

{
  "produitId": 99999,
  "typeMvt": "ENTREE",
  "quantite": 10,
  "prixAchat": 15.00,
  "refCommande": null
}

###############################################################################
### 5. CREATE - Mouvement avec quantité négative (Test de validation)
###############################################################################

POST {{baseUrl}}
Content-Type: application/json

{
  "produitId": 1,
  "typeMvt": "ENTREE",
  "quantite": -50,
  "prixAchat": 20.00,
  "refCommande": null
}

###############################################################################
### 6. GET ALL - Récupérer tous les mouvements (sans pagination)
###############################################################################

GET {{baseUrl}}

###############################################################################
### 7. GET ALL - Récupérer tous les mouvements avec pagination (page 0, size 10)
###############################################################################

GET {{baseUrl}}?page=0&size=10

###############################################################################
### 8. GET ALL - Avec pagination et tri par date décroissante
###############################################################################

GET {{baseUrl}}?page=0&size=5&sort=dateMvt,desc

###############################################################################
### 9. GET ALL - Avec pagination et tri par quantité croissante
###############################################################################

GET {{baseUrl}}?page=0&size=10&sort=quantite,asc

###############################################################################
### 10. GET BY PRODUCT - Récupérer tous les mouvements d'un produit spécifique
###############################################################################

GET {{baseUrl}}/produit/1

###############################################################################
### 11. GET BY PRODUCT - Produit sans mouvements
###############################################################################

GET {{baseUrl}}/produit/2

###############################################################################
### 12. GET BY PRODUCT - Produit inexistant (Test d'erreur)
###############################################################################

GET {{baseUrl}}/produit/99999

###############################################################################
### 13. GET BY TYPE - Récupérer tous les mouvements de type ENTREE
###############################################################################

GET {{baseUrl}}/type/ENTREE

###############################################################################
### 14. GET BY TYPE - Récupérer tous les mouvements de type SORTIE
###############################################################################

GET {{baseUrl}}/type/SORTIE

###############################################################################
### 15. GET BY TYPE - Récupérer tous les mouvements de type AJUSTEMENT
###############################################################################

GET {{baseUrl}}/type/AJUSTEMENT

###############################################################################
### 16. GET BY COMMANDE - Récupérer les mouvements liés à une commande
###############################################################################

GET {{baseUrl}}/commande/12345

###############################################################################
### 17. GET BY COMMANDE - Commande sans mouvements
###############################################################################

GET {{baseUrl}}/commande/99999

###############################################################################
### 18. DELETE - Supprimer un mouvement par son ID
###############################################################################

DELETE {{baseUrl}}/1

###############################################################################
### 19. DELETE - Supprimer un mouvement inexistant (Test d'erreur)
###############################################################################

DELETE {{baseUrl}}/99999

###############################################################################
### BONUS: Tests du Product Service (pour vérification)
###############################################################################

### GET - Récupérer un produit par ID
GET {{productServiceUrl}}/1

###

### GET - Lister tous les produits
GET {{productServiceUrl}}

###

### POST - Créer un nouveau produit (si le service le permet)
POST {{productServiceUrl}}
Content-Type: application/json

{
  "nom": "Chemise Blanche",
  "description": "Chemise en coton blanc",
  "prixUnitaire": 45.99,
  "quantiteStock": 100
}

###############################################################################
### Tests de Scénarios Complets
###############################################################################

### Scénario 1: Créer plusieurs mouvements pour le même produit
### Étape 1: ENTREE de 200 unités
POST {{baseUrl}}
Content-Type: application/json

{
  "produitId": 1,
  "typeMvt": "ENTREE",
  "quantite": 200,
  "prixAchat": 30.00,
  "refCommande": 11111
}

###

### Étape 2: SORTIE de 50 unités
POST {{baseUrl}}
Content-Type: application/json

{
  "produitId": 1,
  "typeMvt": "SORTIE",
  "quantite": 50,
  "prixAchat": null,
  "refCommande": null
}

###

### Étape 3: AJUSTEMENT à 120 unités
POST {{baseUrl}}
Content-Type: application/json

{
  "produitId": 1,
  "typeMvt": "AJUSTEMENT",
  "quantite": 120,
  "prixAchat": null,
  "refCommande": null
}

###

### Étape 4: Vérifier tous les mouvements du produit 1
GET {{baseUrl}}/produit/1

###############################################################################
### Scénario 2: Créer des mouvements liés à la même commande
###############################################################################

### Mouvement 1 pour commande 55555
POST {{baseUrl}}
Content-Type: application/json

{
  "produitId": 1,
  "typeMvt": "ENTREE",
  "quantite": 150,
  "prixAchat": 28.50,
  "refCommande": 55555
}

###

### Mouvement 2 pour commande 55555
POST {{baseUrl}}
Content-Type: application/json

{
  "produitId": 2,
  "typeMvt": "ENTREE",
  "quantite": 75,
  "prixAchat": 42.00,
  "refCommande": 55555
}

###

### Récupérer tous les mouvements de la commande 55555
GET {{baseUrl}}/commande/55555

###############################################################################
### Tests de Validation et Gestion d'Erreurs
###############################################################################

### Test 1: produitId null
POST {{baseUrl}}
Content-Type: application/json

{
  "produitId": null,
  "typeMvt": "ENTREE",
  "quantite": 50,
  "prixAchat": 25.00,
  "refCommande": null
}

###

### Test 2: typeMvt null
POST {{baseUrl}}
Content-Type: application/json

{
  "produitId": 1,
  "typeMvt": null,
  "quantite": 50,
  "prixAchat": 25.00,
  "refCommande": null
}

###

### Test 3: quantite null
POST {{baseUrl}}
Content-Type: application/json

{
  "produitId": 1,
  "typeMvt": "ENTREE",
  "quantite": null,
  "prixAchat": 25.00,
  "refCommande": null
}

###

### Test 4: Type de mouvement invalide
POST {{baseUrl}}
Content-Type: application/json

{
  "produitId": 1,
  "typeMvt": "INVALID_TYPE",
  "quantite": 50,
  "prixAchat": 25.00,
  "refCommande": null
}

###

### Test 5: Quantité zéro
POST {{baseUrl}}
Content-Type: application/json

{
  "produitId": 1,
  "typeMvt": "ENTREE",
  "quantite": 0,
  "prixAchat": 25.00,
  "refCommande": null
}

###############################################################################
### Tests PostgreSQL (Development)
###############################################################################

### Accéder à la console PostgreSQL (ouvrir dans le navigateur ou terminal)
### URL: http://localhost:8082/h2-console
### JDBC URL: jdbc:postgresql://localhost:5432/tricol_db
### Username: tricol_user
### Password: tricol_pass123

###############################################################################
### Notes d'utilisation
###############################################################################

# 1. Assurez-vous que le service MouvementStock est démarré sur le port 8082
# 2. Le Product Service doit être accessible sur http://localhost:8085
# 3. PostgreSQL doit être démarré et la base de données tricol_db doit exister
# 4. Utilisez IntelliJ IDEA, VSCode avec REST Client, ou Postman
# 5. Les réponses attendues:
#    - 201 CREATED pour POST
#    - 200 OK pour GET
#    - 204 NO_CONTENT pour DELETE
#    - 404 NOT_FOUND pour ressources inexistantes
#    - 400 BAD_REQUEST pour données invalides

# 6. Pour IntelliJ IDEA/VSCode: Cliquez sur "Run" à côté de chaque requête
# 7. Les variables {{baseUrl}} et {{productServiceUrl}} sont définies en haut
