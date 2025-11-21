-- Script d'initialisation de la base de données PostgreSQL pour Tricol
-- Ce script crée la base de données et l'utilisateur nécessaires

-- Créer l'utilisateur s'il n'existe pas
DO
$$
BEGIN
   IF NOT EXISTS (
      SELECT FROM pg_catalog.pg_user WHERE usename = 'tricol_user'
   ) THEN
      CREATE USER tricol_user WITH PASSWORD 'tricol_pass123';
   END IF;
END
$$;

-- Créer la base de données principale
CREATE DATABASE tricol_db;

-- Donner tous les privilèges à l'utilisateur
GRANT ALL PRIVILEGES ON DATABASE tricol_db TO tricol_user;

-- Se connecter à la base de données tricol_db
\c tricol_db

-- Donner les privilèges sur le schéma public
GRANT ALL ON SCHEMA public TO tricol_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO tricol_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO tricol_user;

-- Permissions par défaut pour les futurs objets
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO tricol_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO tricol_user;

