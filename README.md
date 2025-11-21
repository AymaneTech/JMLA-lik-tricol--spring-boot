# 🏭 JMLA-LIK - Tricol Supply Management System

A **microservices-based system** for complete supply chain management — including products, suppliers, orders, and stock movements — built with **Spring Boot**, **PostgreSQL**, and **Docker**.

---

## 🚀 Overview

Tricol is a B2B system designed for **primary suppliers (wholesalers)** who manage product catalogs, supplier orders, and automatic stock updates using the **CUMP (Weighted Average Cost)** method.

Each service is independent and communicates via REST APIs.

---

## 🧩 Microservices Architecture

| Service | Port | Description |
|----------|------|-------------|
| **PostgreSQL** | 5442 | Shared database |
| **Products Service** | 8080 | Manages product catalog and stock |
| **Suppliers Service** | 8082 | Manages supplier information |
| **Orders Service** | 8083 | Manages supplier orders and CUMP calculation |
| **Stock Movement Service** | 8084 | Tracks stock entries, exits, and adjustments |

> ⚠️ *Auth service temporarily excluded from this version.*

---

## 🏗️ Architecture Diagram

<img width="1452" height="619" alt="Capture d’écran du 2025-11-09 16-15-48" src="https://github.com/user-attachments/assets/7c3554d7-689f-4e27-a25e-43ef6852cb4e" />

  
---

## 🧱 Technical Stack

- **Backend Framework:** Spring Boot 3.x (Java 17)
- **Database:** PostgreSQL 15
- **ORM:** Spring Data JPA + Hibernate
- **API Communication:** REST (WebClient)
- **Containerization:** Docker & Docker Compose
- **Architecture Style:** Microservices (fully decoupled)

---

## ⚙️ How to Run

### Prerequisites
- Docker 20.10+
- Docker Compose 2.0+

### Start the system
```bash
# Start all services
docker-compose up -d

# Check logs
docker-compose logs -f

# List running containers
docker-compose ps
```

<img width="1493" height="203" alt="image" src="https://github.com/user-attachments/assets/60032e21-bcfb-4038-8a4a-7f4882278145" />

  
---

## 🌐 API Endpoints

### Products Service (8080)
```http
GET    /produits                 # List all products
POST   /produits                 # Create a product
GET    /produits/{id}            # Get product details
PUT    /produits/{id}            # Update a product
DELETE /produits/{id}            # Delete a product
PATCH  /produits/{id}/reduce-stock # Reduce stock
```

---

### Suppliers Service (8082)
```http
GET    /fournisseurs             # List all suppliers
POST   /fournisseurs             # Create a supplier
GET    /fournisseurs/{id}        # Get supplier details
PUT    /fournisseurs/{id}        # Update a supplier
DELETE /fournisseurs/{id}        # Delete a supplier
```

---

### Orders Service (8083)
```http
GET    /api/v1/commandes               # List all orders
POST   /api/v1/commandes               # Create an order (multi-product)
GET    /api/v1/commandes/{id}          # Get order details
PUT    /api/v1/commandes/{id}          # Update order
DELETE /api/v1/commandes/{id}          # Delete order
PATCH  /api/v1/commandes/{id}/status   # Update order status
```

**Order Statuses:** `PENDING`, `CONFIRMED`, `DELIVERED`, `CANCELLED`

**Pricing Method:** Automatically computed using **CUMP (Weighted Average Cost)**

---

### Stock Movement Service (8084)
```http
GET  /api/mouvements                  # List all movements
POST /api/mouvements                  # Register a movement
GET  /api/mouvements/{id}             # Get movement details
GET  /api/mouvements/produit/{id}     # Get movements by product
```

**Movement Types:** `ENTREE`, `SORTIE`, `AJUSTEMENT`

---

## 💡 Key Features

- 📦 Multi-product order management  
- 🧮 Automatic **CUMP price calculation**  
- 🔄 Real-time stock update when an order is delivered  
- 🧾 Full stock movement traceability  
- 🧰 Error handling and validation across services  

---

## 🧠 Example Request – Create Order

```json
POST http://localhost:8083/api/v1/commandes
Content-Type: application/json

{
  "fournisseurId": 1,
  "produits": [
    {
      "produitId": 1,
      "quantite": 100,
      "prixUnitaire": 15.50
    },
    {
      "produitId": 2,
      "quantite": 50,
      "prixUnitaire": 25.00
    }
  ],
  "status": "PENDING"
}
```

<img width="1904" height="971" alt="Capture d’écran du 2025-11-09 15-54-35" src="https://github.com/user-attachments/assets/c11c4b5a-416c-4846-b8d8-f169a6d40a71" />

<img width="1905" height="650" alt="Capture d’écran du 2025-11-09 15-55-14" src="https://github.com/user-attachments/assets/1a87c01f-45bf-474e-ab21-2a1076274538" />

---

## 🧰 Useful Commands

```bash
# Restart a service
docker-compose restart commandes

# Rebuild a service
docker-compose up -d --build commandes

# View logs of a service
docker-compose logs -f commandes

# Stop all containers
docker-compose down

# Stop & remove volumes
docker-compose down -v
```

---

## 🧩 Future Improvements

- Add Authentication microservice (JWT)  
- Implement Role-based Access Control  
- Add Reporting/Analytics for suppliers and stock trends  
- Build a minimal frontend dashboard  

---

## 📚 Documentation Links

- [API Orders Documentation](docs/API_DOCUMENTATION.md)
- [Docker Setup Guide](docs/README-DOCKER.md)

---

## 🧑‍💻 Author

**Tricol Systems © 2025**  
Developed by ismail baoud  
> Focused on building scalable, modular supply management systems using Spring Boot and Docker.

