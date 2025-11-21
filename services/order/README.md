# Gestion des Commandes Fournisseurs - Spring Boot

## 🎉 Changements et Améliorations Récents

### ✅ Problèmes Résolus

1. **Erreur 405 (Method Not Allowed)** ✓
   - **Cause** : L'endpoint utilisait `@RequestParam` au lieu de `@PathVariable`
   - **Solution** : Modification de la route en `PATCH /api/v1/commandes/{id}/status`

2. **Support Multi-Produits** ✓
   - Vous pouvez maintenant créer une commande avec plusieurs produits
   - Utilisation de la table `commande_produit` pour la relation many-to-many

3. **Informations Complètes des Produits** ✓
   - Les réponses retournent maintenant toutes les informations des produits
   - Incluant: id, nom, prix, quantité, et prix unitaire au moment de la commande

4. **Création Automatique des Mouvements de Stock** ✓
   - Lorsqu'une commande passe au statut `LIVREE`
   - Création automatique de mouvements de type `ENTREE` pour chaque produit
   - Mise à jour automatique des quantités en stock

### 🆕 Nouveaux DTOs Créés

1. **CommandeRequestDTO** : Pour créer une commande avec plusieurs produits
2. **CommandeResponseDTO** : Pour retourner une commande avec détails complets
3. **ProduitCommandeDTO** : Pour spécifier un produit et sa quantité dans une commande
4. **ProduitDetailDTO** : Pour retourner les détails complets d'un produit

### 📝 Modifications des Services

#### CommendeService & CommendeServiceImpl
- `creerCommende()` : Supporte maintenant plusieurs produits
- `findAll()` : Retourne les commandes avec détails complets des produits
- `findById()` : Retourne une commande avec détails complets des produits
- `updateCommendeStatus()` : Crée automatiquement les mouvements de stock pour statut LIVREE
- `deleteById()` : Supprime la commande et ses lignes de produits associées

#### CommendeController
- Routes mises à jour pour utiliser les nouveaux DTOs
- Endpoint de mise à jour de statut corrigé : `PATCH /api/v1/commandes/{id}/status`

### 🗂️ Structure de la Base de Données

#### Table `commande`
- `id` : Identifiant unique
- `status` : Statut de la commande (EN_ATTENTE, EN_COURS, LIVREE, ANNULEE)
- `id_fournisseur` : Référence au fournisseur

#### Table `commande_produit`
- `id` : Identifiant unique
- `commande_id` : Référence à la commande
- `produit_id` : Référence au produit
- `quantite` : Quantité commandée
- `unit_price` : Prix unitaire au moment de la commande

### 🔧 Configuration Maven

Le fichier `pom.xml` a été mis à jour pour :
- Configurer correctement le processeur d'annotations Lombok
- Assurer la génération des getters/setters lors de la compilation

### 📚 Documentation

Consultez `API_DOCUMENTATION.md` pour :
- Exemples de requêtes HTTP
- Structure détaillée des DTOs
- Comportements spéciaux des endpoints

### 🧪 Fichier de Test

Le fichier `testCommendsEndpointes.http` contient des exemples de requêtes pour tester tous les endpoints.

## 🚀 Démarrage

1. **Compilation** :
   ```bash
   ./mvnw clean compile
   ```

2. **Empaquetage** :
   ```bash
   ./mvnw clean package
   ```

3. **Exécution** :
   ```bash
   ./mvnw spring-boot:run
   ```

## 📋 Exemple d'Utilisation

### Créer une commande avec plusieurs produits
```bash
POST http://localhost:8080/api/v1/commandes
Content-Type: application/json

{
  "idFournisseur": 1,
  "produits": [
    {"produitId": 1, "quantite": 10},
    {"produitId": 2, "quantite": 5}
  ]
}
```

### Mettre à jour le statut
```bash
PATCH http://localhost:8080/api/v1/commandes/1/status
Content-Type: application/json

{
  "status": "LIVREE"
}
```

## 🔗 Services Dépendants

Ce microservice communique avec :
- **Service Produits** (`${produits.service.url}`) : Récupération des infos produits et mise à jour des stocks
- **Service Mouvements** (`${mouvements.service.url}`) : Création des mouvements de stock

Assurez-vous que ces services sont configurés dans `application.properties`.

## ✨ Nettoyage Effectué

- Suppression des dossiers vides inutilisés (`static/`, `templates/`, `db/changelog/`)
- Code optimisé et refactorisé
- Projet reconnu comme projet Java avec toutes les dépendances Maven correctement configurées

## 📞 Support

Pour toute question ou problème, référez-vous à la documentation API ou consultez les fichiers de test HTTP fournis.

