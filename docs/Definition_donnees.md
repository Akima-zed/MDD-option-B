# Définition des Données

**Date** : 21 janvier 2026  
**Projet** : Monde de Dév (MDD)

---

## Schéma Entités - Diagramme UML

```
┌─────────────────────────┐
│        User             │
├─────────────────────────┤
│ -id: Long (PK)         │
│ -username: String (U)  │
│ -email: String (U)     │
│ -password: String      │
│ -dateInscription: Date │
└──────────┬──────────────┘
           │ 1:N (auteur)
           │
    ┌──────▼──────────────┐
    │  UserTheme (M:N)    │
    │  -user_id (FK)      │
    │  -theme_id (FK)     │
    └──────┬──────────────┘
           │ M:N
┌──────────▼──────────────┐
│      Theme              │
├─────────────────────────┤
│ -id: Long (PK)         │
│ -name: String (U)      │
│ -description: String   │
└──────────┬──────────────┘
           │ 1:N
┌──────────▼──────────────┐
│      Article            │
├─────────────────────────┤
│ -id: Long (PK)         │
│ -title: String         │
│ -content: Text         │
│ -author_id: Long (FK)  │
│ -theme_id: Long (FK)   │
│ -createdAt: Timestamp  │
└──────────┬──────────────┘
           │ 1:N
┌──────────▼──────────────┐
│      Comment            │
├─────────────────────────┤
│ -id: Long (PK)         │
│ -content: String       │
│ -author_id: Long (FK)  │
│ -article_id: Long(FK)  │
│ -createdAt: Timestamp  │
└─────────────────────────┘
```

---

## Tables SQL

### Table: users

```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) UNIQUE NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  date_inscription DATE DEFAULT CURRENT_DATE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

**Champs**

- `id` : Identifiant unique, clé primaire auto-incrémentée
- `username` : Nom d'utilisateur unique (2-50 caractères)
- `email` : Adresse email unique, format valide
- `password` : Hash BCrypt du mot de passe (jamais en clair)
- `date_inscription` : Date de création du compte
- `created_at` : Timestamp création
- `updated_at` : Timestamp dernière modification

**Validation**

- Username : 2-50 chars, alphanumérique + underscore, UNIQUE
- Email : Format email valide, UNIQUE
- Password : Min 8 chars, majuscule, minuscule, chiffre, caractère spécial (avant hachage BCrypt)

**Sécurité**

- Password jamais exposé en réponse API
- Hash BCrypt avec salt aléatoire
- Audit trail avec timestamps

---

### Table: themes

```sql
CREATE TABLE themes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) UNIQUE NOT NULL,
  description VARCHAR(500) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

**Champs**

- `id` : Identifiant unique
- `name` : Nom du thème (unique)
- `description` : Description du thème
- `created_at` : Date de création

**Exemples de données**

```sql
INSERT INTO themes (name, description) VALUES
('Angular', 'Framework frontend moderne avec TypeScript'),
('Spring Boot', 'Framework backend Java entreprise'),
('React', 'Bibliothèque UI JavaScript'),
('Vue.js', 'Framework progressif JavaScript');
```

---

### Table: articles

```sql
CREATE TABLE articles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  content LONGTEXT NOT NULL,
  author_id BIGINT NOT NULL,
  theme_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (theme_id) REFERENCES themes(id) ON DELETE RESTRICT
);
```

**Champs**

- `id` : Identifiant unique
- `title` : Titre de l'article (1-255 chars)
- `content` : Contenu complet de l'article (LONGTEXT)
- `author_id` : Clé étrangère vers users (auteur)
- `theme_id` : Clé étrangère vers themes (catégorie)
- `created_at` : Date de création
- `updated_at` : Date de dernière modification

**Contraintes**

- `ON DELETE CASCADE` sur author_id : Si user supprimé → articles supprimés
- `ON DELETE RESTRICT` sur theme_id : Impossible de supprimer thème avec articles

---

### Table: comments

```sql
CREATE TABLE comments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  content VARCHAR(1000) NOT NULL,
  author_id BIGINT NOT NULL,
  article_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE
);
```

**Champs**

- `id` : Identifiant unique
- `content` : Texte du commentaire (1-1000 chars)
- `author_id` : Clé étrangère vers users (auteur)
- `article_id` : Clé étrangère vers articles (article parent)
- `created_at` : Date de création
- `updated_at` : Date de modification

**Particularité**

- ⚠️ Commentaires **NON-RÉCURSIFS** (pas de parent_comment_id)
- Un seul niveau de commentaires

---

### Table: user_theme (Association M:N)

```sql
CREATE TABLE user_theme (
  user_id BIGINT NOT NULL,
  theme_id BIGINT NOT NULL,
  subscribed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id, theme_id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (theme_id) REFERENCES themes(id) ON DELETE CASCADE
);
```

**Champs**

- `user_id` : Clé étrangère vers users
- `theme_id` : Clé étrangère vers themes
- `subscribed_at` : Date d'abonnement

**Particularité**

- Clé primaire composite (user_id, theme_id)
- Évite les doublons d'abonnement
- CASCADE sur les deux côtés

---

## Relations

| Source                 | Target    | Type         | Contrainte |
| ---------------------- | --------- | ------------ | ---------- |
| **Users**              | Articles  | 1:N (auteur) | CASCADE    |
| **Users**              | Comments  | 1:N (auteur) | CASCADE    |
| **Users** ↔ **Themes** | UserTheme | M:N          | CASCADE    |
| **Themes**             | Articles  | 1:N          | RESTRICT   |
| **Articles**           | Comments  | 1:N          | CASCADE    |

---

## Index et Optimisation

```sql
-- Primary Keys (automatiques)
ALTER TABLE users ADD PRIMARY KEY (id);
ALTER TABLE themes ADD PRIMARY KEY (id);
ALTER TABLE articles ADD PRIMARY KEY (id);
ALTER TABLE comments ADD PRIMARY KEY (id);

-- Unique Constraints
ALTER TABLE users ADD UNIQUE KEY unique_username (username);
ALTER TABLE users ADD UNIQUE KEY unique_email (email);
ALTER TABLE themes ADD UNIQUE KEY unique_name (name);

-- Foreign Key Indexes
ALTER TABLE articles ADD INDEX idx_author (author_id);
ALTER TABLE articles ADD INDEX idx_theme (theme_id);
ALTER TABLE comments ADD INDEX idx_author (author_id);
ALTER TABLE comments ADD INDEX idx_article (article_id);
ALTER TABLE user_theme ADD INDEX idx_user (user_id);
ALTER TABLE user_theme ADD INDEX idx_theme (theme_id);

-- Indexes pour tri/recherche
ALTER TABLE articles ADD INDEX idx_created (created_at);
ALTER TABLE comments ADD INDEX idx_created (created_at);
```

---

## Validation et Sécurité

### Validation côté application (Spring Boot)

```java
@Entity
public class User {
    @NotBlank(message = "Username required")
    @Size(min = 2, max = 50)
    private String username;

    @Email(message = "Valid email required")
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%]).{8,}$")
    private String password;
}
```

### Hachage password (BCrypt)

```java
// Avant insertion en base
String encrypted = passwordEncoder.encode(plainPassword);
// Format: $2a$10$SlcTTvL5aBXYYWYLJ6dLYO...
// Non-reversible → protection contre fuites
```

### Protection SQL Injection

- JPA avec requêtes paramétrées
- Aucune concaténation SQL manuelle
- `@Query` avec `:param` seulement

### Protection XSS

- Angular sanitization automatique (DomSanitizer)
- Pas d'insertion innerHTML direct
- Validation stricte côté serveur

---

## Diagramme des flux de données

```
User Register/Login
    ↓
JWT Token généré
    ↓
Stockage localStorage (frontend)
    ↓
Requêtes API avec header Authorization: Bearer <token>
    ↓
Validation JWT (backend)
    ↓
Accès données selon user_id
    ↓
Retour JSON (DTO, jamais Entity direct)
```

---

## Statistiques base de données

**5 tables**

- users : ~3-5 colonnes métier
- themes : ~2-3 colonnes
- articles : ~5-6 colonnes
- comments : ~4-5 colonnes
- user_theme : ~3 colonnes

**7 relations**

- 2 relations 1:N (users → articles, users → comments)
- 1 relation M:N (users ↔ themes)
- 2 relations 1:N (themes → articles, articles → comments)

**9 contraintes d'intégrité**

- 5 PRIMARY KEY
- 7 FOREIGN KEY
- 3 UNIQUE
