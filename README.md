# MDD - Monde de Dév

**Réseau social dédié aux développeurs** - Version MVP

MDD (Monde de Dév) est une plateforme permettant aux développeurs de s'abonner à des thèmes de programmation (JavaScript, Java, Python, etc.), consulter un fil d'actualité, publier des articles et commenter.

## Table des matières
- [Architecture](#architecture)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Configuration](#configuration)
- [Démarrage](#démarrage)
- [API Endpoints](#api-endpoints)
- [Tests](#tests)
- [Technologies utilisées](#technologies-utilisées)

## Architecture

```
MDD-option-B/
├── back/          # API REST Spring Boot
├── front/         # Application Angular
└── README.md      # Documentation technique
```

**Architecture 3-tiers :**
- **Front-end** : Angular 14 (SPA)
- **Back-end** : Spring Boot 2.7.3 (API REST)
- **Base de données** : MySQL 8.0+

## Prérequis

- **Java** : 11 ou supérieur
- **Node.js** : 14.20+ ou 16.13+
- **npm** : 6.14+
- **MySQL** : 8.0+
- **Maven** : 3.8+ (inclus via wrapper mvnw)

## Installation

### 1. Cloner le projet
```bash
git clone https://github.com/Akima-zed/MDD-option-B.git
cd MDD-option-B
```

### 2. Créer la base de données MySQL

> Note : Credentials de démonstration pour environnement local uniquement.

```sql
mysql -u root -p

CREATE DATABASE MDD_db;
CREATE USER 'mdd_user'@'localhost' IDENTIFIED BY 'mdd_password';
GRANT ALL PRIVILEGES ON MDD_db.* TO 'mdd_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### 3. Lancer le backend (Spring Boot)

Windows PowerShell :
```powershell
cd back
$env:DB_USER="mdd_user"
$env:DB_PASSWORD="mdd_password"
.\mvnw.cmd spring-boot:run
```

Linux/Mac :
```bash
cd back
DB_USER=mdd_user DB_PASSWORD=mdd_password ./mvnw spring-boot:run
```

Backend : http://localhost:8081

### 4. Lancer le frontend (Angular)

```bash
cd ../front
npm install
npm start
```

Frontend : http://localhost:4200

## Configuration

### Variables d'environnement (Backend)

Créez un fichier `.env` à la racine du dossier `back/` ou utilisez les variables système :

```properties
DB_USER=mdd_user
DB_PASSWORD=mdd_password
DB_URL=jdbc:mysql://localhost:3306/MDD_db
JWT_SECRET=votre_secret_jwt_super_securise_min_256_bits
```

### Configuration MySQL

Le fichier [application.properties](back/src/main/resources/application.properties) configure :
- JPA Hibernate (auto DDL)
- Connexion MySQL
- Données de test automatiques (DataInitializer)

**Schéma créé automatiquement** avec 4 tables :
- `users` : Utilisateurs (username, email, password hashé BCrypt)
- `theme` : Thèmes de programmation
- `article` : Articles publiés
- `comment` : Commentaires sur articles
- `user_theme` : Table de jointure (abonnements)

## Démarrage

### Démarrage rapide (développement)

**Terminal 1 - Backend :**
```bash
cd back
./mvnw spring-boot:run
# Serveur démarré sur http://localhost:8081
```

**Terminal 2 - Frontend :**
```bash
cd front
npm start
# Application disponible sur http://localhost:4200
```

### Comptes de test

Après le premier démarrage, 3 utilisateurs sont créés automatiquement :

| Email | Mot de passe | Username |
|-------|--------------|----------|
| john@test.com | password123 | johndoe |
| jane@test.com | password123 | janedoe |
| dev@test.com | password123 | devtest |

**Thèmes disponibles** : JavaScript, Java, Python, Angular, React, Node.js

## API Endpoints

**Base URL** : `http://localhost:8081/api`

### Authentification (`/auth`)

| Méthode | Endpoint | Description | Body | Réponse |
|---------|----------|-------------|------|---------|
| POST | `/auth/register` | Inscription nouvel utilisateur | `{username, email, password}` | `{token, id, username, email}` |
| POST | `/auth/login` | Connexion utilisateur | `{emailOrUsername, password}` | `{token, id, username, email}` |

**Exemple - Inscription :**
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","email":"test@example.com","password":"Test1234!"}'
```

**Exemple - Connexion :**
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"emailOrUsername":"testuser","password":"Test1234!"}'
```

### Utilisateurs (`/users`)

| Méthode | Endpoint | Description | Auth | Body |
|---------|----------|-------------|------|------|
| GET | `/users/{id}` | Récupérer un utilisateur | ✅ | - |
| GET | `/users/{id}/subscriptions` | Abonnements de l'utilisateur | ✅ | - |
| PUT | `/users/{id}` | Modifier profil (username/email) | ✅ | `{username?, email?}` |

**Exemple - Récupérer abonnements :**
```bash
curl -X GET http://localhost:8081/api/users/1/subscriptions \
  -H "Authorization: Bearer <votre_token_jwt>"
```

### Thèmes (`/themes`)

| Méthode | Endpoint | Description | Auth | Body |
|---------|----------|-------------|------|------|
| GET | `/themes` | Liste tous les thèmes | ✅ | - |
| GET | `/themes/{id}` | Récupérer un thème | ✅ | - |
| POST | `/themes/{id}/subscribe` | S'abonner à un thème | ✅ | `{}` |
| DELETE | `/themes/{id}/subscribe` | Se désabonner | ✅ | - |

**Exemple - S'abonner à un thème :**
```bash
curl -X POST http://localhost:8081/api/themes/1/subscribe \
  -H "Authorization: Bearer <votre_token_jwt>" \
  -H "Content-Type: application/json" \
  -d '{}'
```

### Articles (`/articles`)

| Méthode | Endpoint | Description | Auth | Body |
|---------|----------|-------------|------|------|
| GET | `/articles` | Fil d'actualité (triés par date DESC) | ✅ | - |
| GET | `/articles/{id}` | Récupérer un article | ✅ | - |
| POST | `/articles` | Créer un article | ✅ | `{title, content, themeId}` |
| GET | `/articles/{id}/comments` | Commentaires d'un article | ✅ | - |

**Exemple - Créer un article :**
```bash
curl -X POST http://localhost:8081/api/articles \
  -H "Authorization: Bearer <votre_token_jwt>" \
  -H "Content-Type: application/json" \
  -d '{"title":"Mon premier article","content":"Contenu de l article","themeId":1}'
```

### Commentaires (`/comments`)

| Méthode | Endpoint | Description | Auth | Body |
|---------|----------|-------------|------|------|
| POST | `/comments` | Ajouter un commentaire | ✅ | `{content, articleId}` |

**Exemple - Ajouter un commentaire :**
```bash
curl -X POST http://localhost:8081/api/comments \
  -H "Authorization: Bearer <votre_token_jwt>" \
  -H "Content-Type: application/json" \
  -d '{"content":"Super article !","articleId":1}'
```

### Sécurité

**Authentication JWT** : Toutes les routes (sauf `/auth/*`) nécessitent un token JWT dans le header :
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

- **Durée de validité** : 24 heures
- **Algorithme** : HMAC SHA-256
- **Claim** : `userId` (Long)

## Tests

### Tests Back-end (JUnit + Mockito)

```bash
cd back
./mvnw test
```

**Résultats** :
- ✅ 31 tests exécutés
- ✅ 0 échecs
- Fichiers : `AuthControllerIntegrationTest`, `UserServiceTest`, `JwtUtilTest`

### Tests Front-end (Jest)

```bash
cd front
npm test
```

**Avec rapport de couverture** :
```bash
npm test -- --coverage --watchAll=false
```

**Résultats** :
- ✅ 56 tests réussis (63 total)
- 📊 Couverture : 61.62%
  - Services : 95% ✅
  - Guards : 100% ✅
  - Interceptors : 100% ✅

## Technologies utilisées

### Back-end
| Technologie | Version | Utilisation |
|-------------|---------|-------------|
| Java | 11 | Langage principal |
| Spring Boot | 2.7.3 | Framework web |
| Spring Security | 5.7.3 | Authentification/Autorisation |
| Spring Data JPA | 2.7.3 | ORM / Persistence |
| Hibernate | 5.6.10 | Implémentation JPA |
| MySQL | 8.0+ | Base de données |
| JJWT | 0.11.5 | Génération/validation JWT |
| BCrypt | - | Hashage mots de passe |
| JUnit 5 | 5.8.2 | Tests unitaires |
| Mockito | 4.5.1 | Mocking pour tests |
| H2 | 2.1.214 | BDD en mémoire (tests) |
| Lombok | 1.18.24 | Réduction boilerplate |

### Front-end
| Technologie | Version | Utilisation |
|-------------|---------|-------------|
| Angular | 14.1.0 | Framework SPA |
| TypeScript | 4.7.4 | Langage typé |
| Angular Material | 14.1.0 | Composants UI |
| RxJS | 7.5.6 | Programmation réactive |
| Jest | 28.1.3 | Framework de tests |
| HttpClient | - | Communication HTTP |

### Principes et Patterns

- **Architecture** : 3-tiers (Présentation / Métier / Données)
- **SOLID** : Respect des principes (SRP, OCP, DIP via DI Spring)
- **REST** : API RESTful avec codes HTTP standards
- **JWT** : Authentification stateless
- **Lazy Loading** : Chargement différé des modules Angular
- **Guards** : Protection des routes Angular
- **Interceptors** : Ajout automatique token JWT

## Contributeurs

- **Développeur** : Heidi (initial) + Vous
- **Designer UX** : Juana
- **Responsable** : Orlando

## Licence

Projet interne ORION - Version MVP

### 4. Lancer le frontend (Angular)

```bash
cd front
npm install
npm start
```

Frontend : http://localhost:4200

## Technologies

- Backend : Spring Boot 3, Spring Security + JWT, MySQL
- Frontend : Angular 14, Angular Material, TypeScript

---

Documentation complète : voir "Documentation et rapport du projet MDD.md"
