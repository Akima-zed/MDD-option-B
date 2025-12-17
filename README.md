# MDD - Monde de Dév

Application de réseau social pour développeurs.

## Prérequis

- Java 17+
- Node.js 18+ (ou 22+ pour ng serve)
- MySQL 8.0+
- Maven 3.8+

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
