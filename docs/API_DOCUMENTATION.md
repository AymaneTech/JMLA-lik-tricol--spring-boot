# Documentation de l'API de Gestion des Commandes Fournisseurs

## Nouveautés et Corrections

### ✅ Problèmes Résolus

1. **Erreur 405 (Method Not Allowed)** : Correction de la route PATCH pour la mise à jour du statut
2. **Support Multi-Produits** : Vous pouvez maintenant créer des commandes avec plusieurs produits
3. **Informations Complètes des Produits** : Les réponses retournent maintenant toutes les informations des produits, pas seulement les IDs
4. **Réduction automatique du stock** : Lors du passage au statut LIVREE, le stock des produits est réduit automatiquement

### 📋 Endpoints Disponibles

#### 1. Créer une Commande avec Plusieurs Produits
```
POST /api/v1/commandes
Content-Type: application/json

{
  "idFournisseur": 1,
  "produits": [
    {
      "produitId": 1,
      "quantite": 10
    },
    {
      "produitId": 2,
      "quantite": 5
    }
  ]
}
```

**Réponse** :
```json
{
  "id": 1,
  "status": "EN_ATTENTE",
  "idFournisseur": 1,
  "produits": [
    {
      "id": 1,
      "nom": "Produit A",
      "prix": 100.0,
      "quantite": 10,
      "unitPrice": 100.0
    },
    {
      "id": 2,
      "nom": "Produit B",
      "prix": 50.0,
      "quantite": 5,
      "unitPrice": 50.0
    }
  ]
}
```

#### 2. Récupérer Toutes les Commandes
```
GET /api/v1/commandes
```

**Réponse** : Liste de commandes avec les détails complets des produits

#### 3. Récupérer une Commande par ID
```
GET /api/v1/commandes/{id}
```

**Réponse** : Commande avec les détails complets des produits

#### 4. Mettre à Jour le Statut d'une Commande
```
PATCH /api/v1/commandes/{id}/status
Content-Type: application/json

{
  "status": "LIVREE"
}
```

**Statuts Disponibles** :
- `EN_ATTENTE`
- `EN_COURS`
- `LIVREE`
- `ANNULEE`

**Comportement Spécial** : Quand une commande passe au statut `LIVREE`, le système :
1. **Réduit automatiquement la quantité en stock** de chaque produit (via `PATCH /api/v1/products/reduce-quantity/{id}`)
2. **Crée des mouvements de stock** de type `SORTIE` pour chaque produit (optionnel, si le service est disponible)
3. Enregistre la référence de la commande dans le mouvement

⚠️ **Important** : Si la réduction de stock échoue (produit non disponible, quantité insuffisante, etc.), l'opération complète est annulée pour maintenir la cohérence des données.

#### 5. Supprimer une Commande
```
DELETE /api/v1/commandes/{id}
```

Supprime la commande et toutes les lignes de produits associées.

### 🔧 Structure des Données

#### CommandeRequestDTO
- `idFournisseur` (Long) : ID du fournisseur
- `produits` (List<ProduitCommandeDTO>) : Liste des produits à commander

#### ProduitCommandeDTO
- `produitId` (Long) : ID du produit
- `quantite` (Integer) : Quantité commandée

#### CommandeResponseDTO
- `id` (Long) : ID de la commande
- `status` (CommendeStatus) : Statut de la commande
- `idFournisseur` (Long) : ID du fournisseur
- `produits` (List<ProduitDetailDTO>) : Liste des produits avec détails complets

#### ProduitDetailDTO
- `id` (Long) : ID du produit
- `nom` (String) : Nom du produit
- `prix` (Double) : Prix unitaire du produit (depuis le service produits)
- `quantite` (Integer) : Quantité commandée
- `unitPrice` (Double) : Prix au moment de la commande

### 🔗 Intégration avec les Microservices

Le service de gestion des commandes interagit avec :

#### Service Produits (`${produits.service.url}`)
- **GET** `/api/v1/products/{id}` : Récupère les informations d'un produit
- **PATCH** `/api/v1/products/reduce-quantity/{id}` : Réduit la quantité en stock
  ```json
  {
    "quantityToReduce": 10
  }
  ```

#### Service Mouvements (`${mouvements.service.url}`) - Optionnel
- **POST** `/api/v1/mouvements` : Crée un mouvement de stock
  ```json
  {
    "produitId": 1,
    "typeMvt": "SORTIE",
    "quantite": 10,
    "prixAchat": 100.0,
    "refCommande": 1
  }
  ```
  
⚠️ **Note** : Le service de mouvements est optionnel. Si l'endpoint n'est pas disponible, la réduction de stock se fera quand même, mais sans enregistrement du mouvement.

### 📝 Notes Importantes

1. La table `commande_produit` gère la relation many-to-many entre commandes et produits
2. Le prix unitaire (`unitPrice`) est capturé au moment de la création de la commande pour historique
3. Les commandes créées ont automatiquement le statut `EN_ATTENTE`
4. Lors de la suppression d'une commande, toutes les lignes de produits associées sont supprimées
5. **La réduction de stock est irréversible** : assurez-vous que le statut LIVREE est correct avant de l'appliquer
