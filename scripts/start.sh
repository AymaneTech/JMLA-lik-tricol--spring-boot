#!/bin/bash
set -e
echo "🚀 Démarrage de l'application Tricol..."
# Vérifier si nous sommes dans un environnement Railway
if [ -n "$RAILWAY_ENVIRONMENT" ]; then
    echo "📦 Environnement Railway détecté"
    # Utiliser les variables d'environnement Railway pour PostgreSQL
    export SPRING_DATASOURCE_URL=${DATABASE_URL:-jdbc:postgresql://localhost:5432/tricol_db}
    export SPRING_DATASOURCE_USERNAME=${PGUSER:-tricol_user}
    export SPRING_DATASOURCE_PASSWORD=${PGPASSWORD:-tricol_pass123}
fi
# Démarrer le service en fonction de la variable SERVICE_NAME
case "$SERVICE_NAME" in
    "auth")
        echo "🔐 Démarrage du service Authentification..."
        cd Authontification
        java -jar target/Authontification-0.0.1-SNAPSHOT.war
        ;;
    "produits")
        echo "📦 Démarrage du service Produits..."
        cd "Gestion des produits-spring-boot"
        java -jar target/gestion-des-produits-0.0.1-SNAPSHOT.war
        ;;
    "fournisseurs")
        echo "🏢 Démarrage du service Fournisseurs..."
        cd Gestion-des-Fournisseurs-spring-core
        java -jar target/gestion-fournisseurs.war
        ;;
    "commandes")
        echo "🛒 Démarrage du service Commandes..."
        cd "gestion des commmendsFournisseur spring boot"
        java -jar target/gestion-des-commmends-fournisseur-spring-boot-0.0.1-SNAPSHOT.war
        ;;
    "mouvementstock")
        echo "📊 Démarrage du service Mouvement Stock..."
        cd MouvementStock
        java -jar target/mouvementstock-0.0.1-SNAPSHOT.war
        ;;
    *)
        echo "❌ SERVICE_NAME non défini ou invalide: $SERVICE_NAME"
        echo "Services disponibles: auth, produits, fournisseurs, commandes, mouvementstock"
        exit 1
        ;;
esac
