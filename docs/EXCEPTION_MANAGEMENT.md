# Documentation - Système de Gestion d'Exceptions Global

## Vue d'ensemble

Un système complet de gestion d'exceptions personnalisées a été implémenté pour tous les microservices du projet Tricol. Ce système garantit une gestion cohérente des erreurs et fournit des réponses HTTP structurées et informatives.

## Architecture

### 1. **Gestion des Produits** (`Gestion des produits-spring-boot`)

#### Exceptions personnalisées créées:

- **ProduitNotFoundException** : Lancée lorsqu'un produit n'est pas trouvé
  ```java
  throw new ProduitNotFoundException(id);
  throw new ProduitNotFoundException("Message personnalisé");
  ```

- **InvalidQuantityException** : Lancée pour les quantités invalides (négatives ou nulles)
  ```java
  throw new InvalidQuantityException(quantity);
  throw new InvalidQuantityException("La quantité doit être positive");
  ```

- **ProduitSaveException** : Lancée lors d'erreurs de sauvegarde/mise à jour/suppression
  ```java
  throw new ProduitSaveException("Erreur lors de la sauvegarde", cause);
  ```

- **MouvementStockException** : Lancée lors d'erreurs de communication avec le service de mouvement de stock
  ```java
  throw new MouvementStockException("Service non accessible", cause);
  ```

#### Méthodes du service mises à jour:

- `save(Produit p)` - Validation du produit et de la quantité
- `delete(Produit p)` - Vérification de l'existence avant suppression
- `update(Produit p)` - Validation complète avant mise à jour
- `findById(Long id)` - Retourne une exception si non trouvé
- `findAllProduits()` - Gestion des erreurs de récupération
- `addQuantity(Long productId, Integer quantityToAdd, Double prixAchat)` - Validation des paramètres

### 2. **Gestion des Commandes Fournisseur** (`gestion des commmendsFournisseur spring boot`)

#### Exceptions personnalisées créées:

- **CommandeNotFoundException** : Commande non trouvée
- **FournisseurNotFoundException** : Fournisseur non trouvé
- **InvalidCommandeException** : Commande invalide
- **ApiRequestExeption** : Erreurs d'appels API (déjà existante, intégrée au handler global)

### 3. **Gestion des Fournisseurs** (`Gestion-des-Fournisseurs-spring-core`)

#### Exceptions personnalisées créées:

- **FournisseurNotFoundException** : Fournisseur non trouvé
- **InvalidFournisseurException** : Données fournisseur invalides
- **FournisseurSaveException** : Erreurs de sauvegarde

### 4. **Mouvement de Stock** (`MouvementStock`)

#### Exceptions personnalisées créées:

- **MouvementStockNotFoundException** : Mouvement non trouvé
- **InvalidMouvementException** : Mouvement invalide
- **StockInsuffisantException** : Stock insuffisant pour une sortie
- **ProduitServiceException** : Erreur de communication avec le service produit

## GlobalExceptionHandler

Chaque microservice possède un `GlobalExceptionHandler` annoté avec `@RestControllerAdvice` qui intercepte toutes les exceptions et retourne des réponses HTTP structurées.

### Structure de la réponse d'erreur (ErrorResponse):

```json
{
  "timestamp": "2025-11-04T10:30:45",
  "status": 404,
  "error": "Produit Not Found",
  "message": "Produit non trouvé avec l'ID: 123",
  "path": "/api/v1/produits/123"
}
```

### Codes HTTP utilisés:

- **404 NOT_FOUND** : Ressource non trouvée
- **400 BAD_REQUEST** : Données invalides (quantité, stock insuffisant)
- **500 INTERNAL_SERVER_ERROR** : Erreurs de sauvegarde
- **503 SERVICE_UNAVAILABLE** : Services externes non accessibles

## Exemples d'utilisation

### Exemple 1: Recherche d'un produit inexistant

**Requête:**
```http
GET /api/v1/produits/999
```

**Réponse:**
```json
{
  "timestamp": "2025-11-04T10:30:45",
  "status": 404,
  "error": "Produit Not Found",
  "message": "Produit non trouvé avec l'ID: 999",
  "path": "/api/v1/produits/999"
}
```

### Exemple 2: Création d'un produit avec quantité négative

**Requête:**
```json
POST /api/v1/produits
{
  "name": "Produit Test",
  "quantity": -5,
  "unitPrice": 100.0
}
```

**Réponse:**
```json
{
  "timestamp": "2025-11-04T10:31:00",
  "status": 400,
  "error": "Invalid Quantity",
  "message": "Quantité invalide: -5. La quantité doit être positive.",
  "path": "/api/v1/produits"
}
```

### Exemple 3: Service de mouvement de stock non accessible

**Réponse:**
```json
{
  "timestamp": "2025-11-04T10:32:00",
  "status": 503,
  "error": "Mouvement Stock Service Error",
  "message": "Le service de mouvement de stock n'est pas accessible. Le produit a été créé mais le mouvement n'a pas été enregistré.",
  "path": "/api/v1/produits"
}
```

## Avantages du système

✅ **Cohérence** : Toutes les erreurs suivent le même format dans tous les microservices

✅ **Traçabilité** : Timestamp et path permettent de tracer l'origine des erreurs

✅ **Informativité** : Messages d'erreur clairs et détaillés en français

✅ **Codes HTTP appropriés** : Utilisation correcte des codes HTTP standard

✅ **Séparation des préoccupations** : Les contrôleurs ne gèrent plus directement les exceptions

✅ **Facilité de débogage** : Stack trace préservée avec le paramètre `cause`

## Intégration dans les services

Les exceptions sont automatiquement interceptées par le `GlobalExceptionHandler`. Il suffit de les lancer dans les services:

```java
public Produit findById(Long id) {
    if (id == null) {
        throw new ProduitNotFoundException("L'ID du produit ne peut pas être null");
    }
    
    return produitRepository.findById(id)
            .orElseThrow(() -> new ProduitNotFoundException(id));
}
```

## Notes importantes

- Les exceptions héritent de `RuntimeException` (exceptions non vérifiées)
- Pas besoin de déclarer `throws` dans les signatures de méthodes
- Les transactions sont automatiquement rollback en cas d'exception RuntimeException
- Le handler global capture aussi les exceptions génériques non prévues

## Maintenance

Pour ajouter une nouvelle exception:

1. Créer la classe d'exception dans le package `exception`
2. Ajouter un `@ExceptionHandler` dans `GlobalExceptionHandler`
3. Définir le code HTTP et le message appropriés
4. Utiliser l'exception dans les services

---

**Date de création:** 2025-11-04  
**Auteur:** Système automatisé de gestion d'exceptions

