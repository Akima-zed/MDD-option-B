# MDD - Monde de Dév

Réseau social pour développeurs permettant de s'abonner à des thèmes de programmation, consulter un fil d'actualité, publier des articles et commenter.

## Table des matières

- [À propos](#à-propos)
- [Architecture](#architecture)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Démarrage](#démarrage)
- [API](#api)
- [Tests](#tests)
- [Technologies](#technologies)

## À propos

MDD est une application permettant aux développeurs de :

- S'abonner à des thèmes (JavaScript, Java, Python, Angular, React, Node.js)
- Consulter un fil d'actualité
- Créer et partager des articles
- Commenter les articles

## Architecture

- Frontend : Angular 14 (Single Page Application)
- Backend : Spring Boot 2.7.3 (API REST)
- Base de données : MySQL 8.0+

## Prérequis

- Java 11 ou supérieur
- Node.js 14.20+ ou 16.13+
- npm 6.14+
- MySQL 8.0+
- Maven 3.8+ (inclus via wrapper mvnw)

### 1. Cloner le projet

```bash
git clone https://github.com/Akima-zed/MDD-option-B.git
cd MDD-option-B
```

### 2. Créer la base de données MySQL

```sql
mysql -u root -p

CREATE DATABASE MDD_db;
CREATE USER 'mdd_user'@'localhost' IDENTIFIED BY 'mdd_password';
GRANT ALL PRIVILEGES ON MDD_db.* TO 'mdd_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### 3. Configurer les variables d'environnement

Les variables d'environnement peuvent être définies via le système ou via un fichier `.env` :

```properties
DB_USER=mdd_user
DB_PASSWORD=mdd_password
DB_URL=jdbc:mysql://localhost:3306/MDD_db
JWT_SECRET=votre_base64_secret_jwt_256bits
```

La JWT_SECRET doit être une clé HMAC 256 bits encodée en Base64.

## Démarrage

### Backend (Terminal 1)

```powershell
cd back
$env:DB_USER="mdd_user"
$env:DB_PASSWORD="mdd_password"
.\mvnw.cmd spring-boot:run
```

Le serveur démarre sur http://localhost:8081

### Frontend (Terminal 2)

```bash
cd front
npm install
npm start
```

L'application est disponible sur http://localhost:4200

### Comptes de test

Trois utilisateurs sont créés automatiquement au démarrage :

| Email         | Mot de passe | Username |
| ------------- | ------------ | -------- |
| john@test.com | password123  | johndoe  |
| jane@test.com | password123  | janedoe  |
| dev@test.com  | password123  | devtest  |

## API

Base URL : `http://localhost:8081/api`

Toutes les routes (sauf `/auth/*`) nécessitent un token JWT dans le header Authorization.

### Authentification

| Méthode | Endpoint         | Description | Body                          |
| ------- | ---------------- | ----------- | ----------------------------- |
| POST    | `/auth/register` | Inscription | `{username, email, password}` |
| POST    | `/auth/login`    | Connexion   | `{emailOrUsername, password}` |

### Utilisateurs

| Méthode | Endpoint                    | Description               |
| ------- | --------------------------- | ------------------------- |
| GET     | `/users/{id}`               | Récupérer un utilisateur  |
| GET     | `/users/{id}/subscriptions` | Récupérer les abonnements |
| PUT     | `/users/{id}`               | Modifier le profil        |

### Thèmes

| Méthode | Endpoint                 | Description            |
| ------- | ------------------------ | ---------------------- |
| GET     | `/themes`                | Lister tous les thèmes |
| GET     | `/themes/{id}`           | Récupérer un thème     |
| POST    | `/themes/{id}/subscribe` | S'abonner              |
| DELETE  | `/themes/{id}/subscribe` | Se désabonner          |

### Articles

| Méthode | Endpoint                  | Description               | Body                        |
| ------- | ------------------------- | ------------------------- | --------------------------- |
| GET     | `/articles`               | Fil d'actualité           | -                           |
| GET     | `/articles/{id}`          | Récupérer un article      | -                           |
| POST    | `/articles`               | Créer un article          | `{title, content, themeId}` |
| GET     | `/articles/{id}/comments` | Commentaires d'un article | -                           |

### Commentaires

| Méthode | Endpoint    | Description            | Body                   |
| ------- | ----------- | ---------------------- | ---------------------- |
| POST    | `/comments` | Ajouter un commentaire | `{content, articleId}` |

## Tests

### Backend (JUnit 5 + Mockito + JaCoCo)

Lancer les tests :

```bash
cd back
./mvnw clean test jacoco:report
```

Résultats :

- 43 tests exécutés - 100% de réussite
- Couverture globale backend : 64%
- Rapport HTML : `back/target/site/jacoco/index.html`

### Frontend (Jest)

Lancer les tests :

```bash
cd front
npm test
```

Avec couverture :

```bash
npm run test:coverage
```

Résultats :
82 tests exécutés - 100% de réussite
Couverture globale frontend : 82.8%
Rapport HTML : `front/coverage/index.html`

## Technologies

### Backend

| Technologie     | Version | Utilisation                   |
| --------------- | ------- | ----------------------------- |
| Java            | 11      | Langage principal             |
| Spring Boot     | 2.7.3   | Framework web                 |
| Spring Security | 5.7.3   | Authentification/Autorisation |
| Spring Data JPA | 2.7.3   | ORM / Persistence             |
| Hibernate       | 5.6.10  | Implémentation JPA            |
| MySQL           | 8.0+    | Base de données               |
| JJWT            | 0.11.5  | Génération/validation JWT     |
| JUnit 5         | 5.8.2   | Tests unitaires               |
| Mockito         | 4.5.1   | Mocking                       |

### Frontend

| Technologie      | Version | Utilisation            |
| ---------------- | ------- | ---------------------- |
| Angular          | 14.1.0  | Framework SPA          |
| TypeScript       | 4.7.4   | Langage typé           |
| Angular Material | 14.1.0  | Composants UI          |
| RxJS             | 7.5.6   | Programmation réactive |
| Jest             | 28.1.3  | Tests                  |

### Principes

- Architecture 3-tiers
- API RESTful
- Authentification JWT
- Principes SOLID
