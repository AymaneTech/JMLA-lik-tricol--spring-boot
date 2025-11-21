# MouvementStock Microservice - Structure Template

## 📋 Vue d'ensemble

Microservice Spring Boot pour la gestion des mouvements de stock (ENTREE, SORTIE, AJUSTEMENT).
Ce service communique avec le **Product Service** externe via REST API.

**⚠️ IMPORTANT**: Ce projet est un **template/skeleton** - aucune logique n'est implémentée.
Tous les emplacements d'implémentation sont marqués avec des commentaires `// TODO:` détaillés.

---

## 🏗️ Architecture Complète

```
src/main/java/org/ismail/mouvementstock/
│
├── controller/
│   └── MouvementStockController.java          ✅ Tous les endpoints REST
│
├── service/
│   ├── MouvementStockService.java             ✅ Interface du service
│   └── MouvementStockServiceImpl.java         ✅ Implémentation (skeleton)
│
├── repository/
│   └── MouvementStockRepository.java          ✅ JPA Repository
│
├── model/
│   ├── MouvementStock.java                    ✅ Entité JPA avec Lombok
│   └── TypeMouvement.java                     ✅ Enum (ENTREE, SORTIE, AJUSTEMENT)
│
├── dto/
│   ├── MouvementStockRequestDTO.java          ✅ DTO pour les requêtes
│   ├── MouvementStockResponseDTO.java         ✅ DTO pour les réponses
│   └── ProductDTO.java                        ✅ DTO pour les produits du service externe
│
├── client/
│   └── ProductClient.java                     ✅ Client REST pour Product Service
│
├── exception/
│   ├── ResourceNotFoundException.java         ✅ Exception personnalisée
│   └── GlobalExceptionHandler.java            ✅ Gestion globale des erreurs
│
└── config/
    └── RestTemplateConfig.java                ✅ Configuration RestTemplate
```

---

## 🔌 API REST Endpoints

**Base URL**: `/api/v1/mouvements`

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `POST` | `/` | Créer un nouveau mouvement |
| `GET` | `/` | Lister tous les mouvements (avec pagination) |
| `GET` | `/produit/{produitId}` | Mouvements pour un produit spécifique |
| `GET` | `/type/{type}` | Mouvements par type (ENTREE/SORTIE/AJUSTEMENT) |
| `GET` | `/commande/{refCommande}` | Mouvements liés à une commande |
| `DELETE` | `/{id}` | Supprimer un mouvement |

---

## 📊 Modèles de Données

### MouvementStock (Entité JPA)
```java
@Entity
@Table(name = "mouvement_stock")
@Data @NoArgsConstructor @AllArgsConstructor
public class MouvementStock {
    Long id
    Long produitId              // Référence au Product Service
    TypeMouvement typeMvt       // ENTREE, SORTIE, AJUSTEMENT
    Integer quantite
    Double prixAchat
    LocalDateTime dateMvt
    Long refCommande
}
```

### DTOs
- **MouvementStockRequestDTO**: Pour créer un mouvement
- **MouvementStockResponseDTO**: Pour retourner un mouvement
- **ProductDTO**: Pour mapper les réponses du Product Service

---

## 🌐 Communication avec Product Service

### ProductClient.java
Responsable des appels REST vers: `http://localhost:8085/api/v1/products`

**Méthodes (à implémenter)**:
- `ProductDTO getProductById(Long id)` → `GET /products/{id}`
- `void updateProductQuantity(Long id, Integer newQuantity)` → `PUT /products/{id}`
- `boolean productExists(Long id)` → Vérification d'existence

---

## 📝 Logique Métier (à implémenter)

### createMouvement(MouvementStockRequestDTO dto)
1. Valider `produitId` non null
2. Appeler `ProductClient.getProductById()` pour vérifier existence
3. Valider quantité selon le type:
   - **ENTREE**: `quantite > 0`
   - **SORTIE**: `quantite > 0` ET `quantite <= stock actuel`
   - **AJUSTEMENT**: flexible
4. Créer l'entité avec `dateMvt = LocalDateTime.now()`
5. Sauvegarder dans la BD
6. Calculer nouveau stock:
   - **ENTREE**: `stock + quantite`
   - **SORTIE**: `stock - quantite`
   - **AJUSTEMENT**: `quantite` (valeur absolue)
7. Appeler `ProductClient.updateProductQuantity()`
8. Retourner le DTO de réponse

### deleteMouvement(Long id)
1. Récupérer le mouvement
2. Vérifier existence (sinon `ResourceNotFoundException`)
3. Récupérer le produit actuel
4. Calculer stock à restaurer:
   - **ENTREE**: `stock actuel - quantite`
   - **SORTIE**: `stock actuel + quantite`
   - **AJUSTEMENT**: logique complexe (peut-être interdire)
5. Mettre à jour le stock via `ProductClient`
6. Supprimer le mouvement

---

## ⚙️ Configuration

### application.properties
```properties
spring.application.name=mouvement-stock-service
server.port=8082

# H2 Database (développement)
spring.datasource.url=jdbc:h2:mem:mouvementstock
spring.jpa.hibernate.ddl-auto=update

# Product Service URL
product.service.url=http://localhost:8085/api/v1/products
```

---

## 🚀 Démarrage

```bash
# Compiler
./mvnw clean compile

# Lancer l'application
./mvnw spring-boot:run
```

Le service démarrera sur **http://localhost:8082**

---

## 📦 Dépendances

- Spring Boot 3.5.7
- Spring Data JPA
- Spring Web
- Lombok
- H2 Database (développement)

---

## ✅ État de Compilation

**BUILD SUCCESS** ✅

Tous les fichiers compilent sans erreur. Le projet est prêt pour l'implémentation de la logique métier.

---

## 📚 Prochaines Étapes

1. Implémenter la logique dans `MouvementStockServiceImpl`
2. Implémenter les appels REST dans `ProductClient`
3. Ajouter la validation avec `@Valid` sur les DTOs
4. Implémenter les handlers d'exception complets
5. Ajouter des tests unitaires
6. Configurer une base de données MySQL/PostgreSQL pour production
7. Ajouter la documentation Swagger/OpenAPI

---

**Version**: 0.0.1-SNAPSHOT  
**Architecture**: Microservices  
**Communication**: REST (RestTemplate)

