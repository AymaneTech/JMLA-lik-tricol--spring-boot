# Module de Gestion des Mouvements de Stock (Microservice)

Microservice Spring Boot pour la gestion des mouvements de stock dans un système de gestion de stock et de commandes pour une entreprise de vêtements.

## 📋 Vue d'ensemble

Ce microservice fournit une structure complète (skeleton) pour gérer les mouvements de stock avec les opérations suivantes :
- **ENTREE** : Ajout de stock (achats, retours clients)
- **SORTIE** : Retrait de stock (ventes, pertes)
- **AJUSTEMENT** : Correction de stock (inventaire)

⚠️ **Note importante** : Ce code est un squelette (skeleton) - **aucune logique métier n'est implémentée**. Tous les emplacements d'implémentation sont marqués avec des commentaires `// TODO:` détaillés.

🏗️ **Architecture Microservices** : Ce service gère uniquement les mouvements de stock. Les informations sur les produits sont gérées dans un microservice séparé et communiquent via API REST.

## 🏗️ Architecture

Le projet suit une architecture en couches Spring Boot :

```
src/main/java/org/ismail/mouvementstock/
├── model/                    # Couche Entités (JPA)
│   ├── TypeMouvement.java    # Enum: ENTREE, SORTIE, AJUSTEMENT
│   └── MouvementStock.java   # Entité MouvementStock (stocke produitId)
│
├── dto/                      # Couche DTO (Data Transfer Objects)
│   └── MouvementStockDTO.java
│
├── repository/               # Couche Repository (Data Access)
│   └── MouvementStockRepository.java
│
├── service/                  # Couche Service (Logique métier)
│   ├── MouvementStockService.java      # Interface
│   └── MouvementStockServiceImpl.java  # Implémentation (skeleton)
│
├── controller/               # Couche Controller (API REST)
│   └── MouvementStockController.java
│
└── exception/                # Gestion des exceptions
    ├── ResourceNotFoundException.java
    └── GlobalExceptionHandler.java
```

## 📊 Modèle de données

### TypeMouvement (Enum)
- `ENTREE` : Mouvement d'entrée de stock
- `SORTIE` : Mouvement de sortie de stock
- `AJUSTEMENT` : Ajustement de stock

### MouvementStock
| Champ | Type | Description |
|-------|------|-------------|
| id | Long | Identifiant unique |
| typeMvt | TypeMouvement | Type de mouvement |
| quantite | Integer | Quantité du mouvement |
| prixAchat | BigDecimal | Prix d'achat (optionnel) |
| dateMvt | LocalDateTime | Date du mouvement |
| refCommande | Long | Référence commande (nullable) |
| produitId | Long | ID du produit (référence au microservice Produit) |

## 🔌 API REST Endpoints

Base URL: `/api/v1/mouvements`

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/v1/mouvements` | Créer un nouveau mouvement |
| GET | `/api/v1/mouvements` | Lister tous les mouvements |
| GET | `/api/v1/mouvements/produit/{id}` | Lister les mouvements d'un produit |
| GET | `/api/v1/mouvements/type/{type}` | Lister les mouvements par type |
| DELETE | `/api/v1/mouvements/{id}` | Supprimer un mouvement |

### Exemple de requête POST

```json
{
  "produitId": 1,
  "typeMvt": "ENTREE",
  "quantite": 100,
  "prixAchat": 25.50,
  "refCommande": 12345
}
```

## 🔧 Méthodes du Repository

### MouvementStockRepository
- `findByProduitId(Long produitId)` : Recherche par produit
- `findByTypeMvt(TypeMouvement type)` : Recherche par type
- `findByRefCommande(Long refCommande)` : Recherche par commande

## 🌐 Communication Inter-Microservices

Le service doit communiquer avec le microservice Produit pour :
- ✅ Vérifier l'existence d'un produit
- ✅ Mettre à jour le stock après un mouvement
- ✅ Annuler l'impact sur le stock lors de la suppression d'un mouvement

### Options de communication recommandées :

#### 1. **RestTemplate** (synchrone)
```java
@Autowired
private RestTemplate restTemplate;

// Vérifier l'existence d'un produit
ResponseEntity<Produit> response = restTemplate.getForEntity(
    "http://produit-service/api/v1/produits/{id}", 
    Produit.class, 
    produitId
);
```

#### 2. **WebClient** (réactif - recommandé)
```java
@Autowired
private WebClient webClient;

// Mettre à jour le stock
webClient.patch()
    .uri("http://produit-service/api/v1/produits/{id}/stock/add", produitId)
    .bodyValue(Map.of("quantite", quantite))
    .retrieve()
    .bodyToMono(Void.class)
    .block();
```

#### 3. **OpenFeign** (déclaratif - le plus simple)
```java
@FeignClient(name = "produit-service")
public interface ProduitClient {
    @GetMapping("/api/v1/produits/{id}")
    Produit getProduitById(@PathVariable Long id);
    
    @PatchMapping("/api/v1/produits/{id}/stock/add")
    void addStock(@PathVariable Long id, @RequestBody StockUpdateDTO dto);
}
```

## 💼 Logique métier attendue

### createMouvement(MouvementStockDTO dto)
1. Valider que produitId n'est pas null
2. **(Optionnel)** Appeler le microservice Produit pour vérifier l'existence
3. Créer le mouvement avec dateMvt = LocalDateTime.now()
4. **(Optionnel)** Appeler le microservice Produit pour mettre à jour le stock :
   - **ENTREE** : `POST /api/v1/produits/{id}/stock/add`
   - **SORTIE** : `POST /api/v1/produits/{id}/stock/subtract`
   - **AJUSTEMENT** : `POST /api/v1/produits/{id}/stock/adjust`
5. Sauvegarder le mouvement

### deleteMouvement(Long id)
1. Récupérer le mouvement
2. **(Optionnel)** Appeler le microservice Produit pour annuler l'impact :
   - **ENTREE** : `POST /api/v1/produits/{id}/stock/subtract`
   - **SORTIE** : `POST /api/v1/produits/{id}/stock/add`
3. Supprimer le mouvement

## 🚨 Gestion des erreurs

### ResourceNotFoundException (404)
Levée quand une ressource n'existe pas (mouvement, ou produit dans le microservice).

### IllegalArgumentException (400)
Levée pour les arguments invalides :
- Quantité négative
- produitId null
- Données invalides

### Exception générique (500)
Gère toutes les autres exceptions, y compris les erreurs de communication inter-services.

## 🛡️ Résilience Microservices (à implémenter)

Pour une architecture robuste, considérez :

1. **Circuit Breaker** (Resilience4j)
   - Évite les appels répétés à un service défaillant
   
2. **Retry Pattern**
   - Réessaye les appels échoués avec backoff exponentiel
   
3. **Timeout Configuration**
   - Configure des timeouts pour éviter les blocages

4. **Fallback Strategy**
   - Définir un comportement de secours si le microservice Produit est indisponible

## 📝 TODO - Implémentation requise

### 1. Configurer le client REST

**Option A - RestTemplate** (ajouter au pom.xml si nécessaire)
```java
@Configuration
public class RestConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

**Option B - WebClient** (déjà inclus dans Spring Boot)
```java
@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .baseUrl("http://produit-service")
            .build();
    }
}
```

**Option C - OpenFeign** (ajouter la dépendance)
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

### 2. Implémenter la logique métier

- **MouvementStockServiceImpl.java** - Toute la logique métier
- **MouvementStockController.java** - Tous les endpoints
- **GlobalExceptionHandler.java** - Tous les handlers d'exception

## 🛠️ Technologies utilisées

- **Spring Boot 3.5.7**
- **Spring Data JPA** (pour la persistance)
- **Spring Web** (pour l'API REST)
- **Java 17**
- **(À ajouter) Spring Cloud** - pour OpenFeign, Circuit Breaker, etc.

## 📦 Configuration

### application.properties

```properties
# Nom du service
spring.application.name=mouvement-stock-service

# Configuration de la base de données
spring.datasource.url=jdbc:mysql://localhost:3306/mouvement_stock
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# URL du microservice Produit
produit.service.url=http://localhost:8081

# Configuration du serveur
server.port=8082
```

### application.yml (alternative)

```yaml
spring:
  application:
    name: mouvement-stock-service
  datasource:
    url: jdbc:mysql://localhost:3306/mouvement_stock
    username: root
    password: password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

produit:
  service:
    url: http://localhost:8081

server:
  port: 8082
```

## 🚀 Démarrage

```bash
# Compiler le projet
./mvnw clean install

# Lancer l'application
./mvnw spring-boot:run
```

## 📚 Prochaines étapes

1. ✅ Choisir et configurer le client REST (RestTemplate, WebClient ou Feign)
2. ✅ Implémenter la logique métier dans `MouvementStockServiceImpl`
3. ✅ Implémenter les endpoints dans `MouvementStockController`
4. ✅ Implémenter les handlers dans `GlobalExceptionHandler`
5. ✅ Ajouter la validation des DTO avec `@Valid`
6. ✅ Configurer la résilience (Circuit Breaker, Retry)
7. ✅ Ajouter des tests unitaires et d'intégration
8. ✅ Configurer la base de données
9. ✅ Ajouter la documentation Swagger/OpenAPI
10. ✅ Configurer un API Gateway si nécessaire
11. ✅ Implémenter la découverte de services (Eureka) si nécessaire

## 🔍 Exemple de DTO pour la communication

```java
// DTO pour mettre à jour le stock dans le microservice Produit
public class StockUpdateDTO {
    private Integer quantite;
    
    // Getters et Setters
}
```

## 👥 Conventions de nommage

- **Entités** : Nom au singulier, PascalCase
- **Tables** : Nom au pluriel, snake_case
- **Méthodes** : camelCase, verbes d'action
- **Variables** : camelCase, noms descriptifs
- **Constantes** : UPPER_SNAKE_CASE

---

**Version** : 0.0.1-SNAPSHOT  
**Auteur** : Ismail  
**Architecture** : Microservices  
**Date** : 2025-11-02
