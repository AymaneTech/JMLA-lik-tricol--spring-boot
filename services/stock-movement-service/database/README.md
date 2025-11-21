# Configuration PostgreSQL pour MouvementStock

## 📋 Prérequis

- PostgreSQL 12+ installé
- Accès au superuser `postgres`

## 🚀 Installation rapide

### Option 1: Script automatique

```bash
# Se connecter à PostgreSQL en tant que postgres
sudo -u postgres psql -f database/init-db.sql
```

### Option 2: Commandes manuelles

```bash
# Se connecter à PostgreSQL
sudo -u postgres psql

# Exécuter les commandes suivantes:
```

```sql
-- Créer l'utilisateur
CREATE USER tricol_user WITH PASSWORD 'tricol_pass123';

-- Créer la base de données
CREATE DATABASE tricol_db OWNER tricol_user;

-- Donner les privilèges
GRANT ALL PRIVILEGES ON DATABASE tricol_db TO tricol_user;

-- Se connecter à la base
\c tricol_db

-- Donner les droits sur le schéma
GRANT ALL ON SCHEMA public TO tricol_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO tricol_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO tricol_user;

-- Droits par défaut
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO tricol_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO tricol_user;
```

## ✅ Vérification

```bash
# Tester la connexion
psql -U tricol_user -d tricol_db -h localhost -p 5432

# Si la connexion fonctionne, vous verrez:
# tricol_db=>
```

## 📊 Structure de la table

La table `mouvement_stock` sera créée automatiquement par Hibernate au premier démarrage:

```sql
CREATE TABLE mouvement_stock (
    id BIGSERIAL PRIMARY KEY,
    produit_id BIGINT NOT NULL,
    type_mvt VARCHAR(20) NOT NULL,
    quantite INTEGER NOT NULL,
    prix_achat DOUBLE PRECISION,
    date_mvt TIMESTAMP NOT NULL,
    ref_commande BIGINT
);

-- Index pour améliorer les performances
CREATE INDEX idx_mouvement_produit ON mouvement_stock(produit_id);
CREATE INDEX idx_mouvement_type ON mouvement_stock(type_mvt);
CREATE INDEX idx_mouvement_commande ON mouvement_stock(ref_commande);
CREATE INDEX idx_mouvement_date ON mouvement_stock(date_mvt);
```

## 🔧 Configuration PostgreSQL (pg_hba.conf)

Si vous avez des problèmes d'authentification, vérifier `/etc/postgresql/*/main/pg_hba.conf`:

```
# TYPE  DATABASE        USER            ADDRESS                 METHOD
local   all             all                                     peer
host    all             all             127.0.0.1/32            md5
host    all             all             ::1/128                 md5
```

Redémarrer PostgreSQL après modification:
```bash
sudo systemctl restart postgresql
```

## 🗑️ Réinitialiser (si nécessaire)

```sql
-- Supprimer la base de données
DROP DATABASE IF EXISTS tricol_db;

-- Supprimer l'utilisateur
DROP USER IF EXISTS tricol_user;

-- Puis recréer avec les commandes ci-dessus
```

## 📦 Backup et Restore

### Backup
```bash
pg_dump -U tricol_user -d tricol_db -F c -f mouvement_stock_backup.dump
```

### Restore
```bash
pg_restore -U tricol_user -d tricol_db -F c mouvement_stock_backup.dump
```

## 🔍 Monitoring

### Voir les connexions actives
```sql
SELECT pid, usename, datname, state, query 
FROM pg_stat_activity 
WHERE datname = 'tricol_db';
```

### Taille de la base
```sql
SELECT pg_size_pretty(pg_database_size('tricol_db'));
```

### Performance des requêtes
```sql
SELECT * FROM pg_stat_statements 
WHERE query LIKE '%mouvement_stock%' 
ORDER BY total_time DESC;
```
-- Script SQL pour créer la base de données PostgreSQL pour MouvementStock
-- Exécuter ce script en tant que superuser PostgreSQL

-- 1. Créer l'utilisateur (si n'existe pas déjà)
DO
$$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'tricol_user') THEN
      CREATE USER tricol_user WITH PASSWORD 'tricol_pass123';
   END IF;
END
$$;

-- 2. Créer la base de données (si n'existe pas déjà)
SELECT 'CREATE DATABASE tricol_db OWNER tricol_user'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'tricol_db')\gexec

-- 3. Donner tous les privilèges
GRANT ALL PRIVILEGES ON DATABASE tricol_db TO tricol_user;

-- 4. Se connecter à la base tricol_db pour configurer les permissions
\c tricol_db

-- 5. Donner les droits sur le schéma public
GRANT ALL ON SCHEMA public TO tricol_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO tricol_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO tricol_user;

-- 6. Configurer les droits par défaut pour les futures tables
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO tricol_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO tricol_user;

-- Note: Spring Boot créera automatiquement la table mouvement_stock 
-- au démarrage grâce à spring.jpa.hibernate.ddl-auto=update

