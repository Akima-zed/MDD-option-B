# Analyse des Besoins Front-end

**Date** : 21 janvier 2026  
**Projet** : Monde de Dév (MDD)

---

## Pages de l'application

| Page               | Route              | Authentification | Description                                   |
| ------------------ | ------------------ | ---------------- | --------------------------------------------- |
| **Home**           | `/`                | Public           | Page d'accueil avec accès login/register      |
| **Login**          | `/login`           | Public           | Formulaire de connexion                       |
| **Register**       | `/register`        | Public           | Formulaire d'inscription                      |
| **Feed**           | `/feed`            | Protégée         | Fil d'actualité des articles (thèmes abonnés) |
| **Article**        | `/articles/:id`    | Protégée         | Détail article avec commentaires              |
| **Create Article** | `/articles/create` | Protégée         | Création d'article                            |
| **Themes**         | `/themes`          | Protégée         | Liste des thèmes et gestion abonnements       |
| **Profile**        | `/profile`         | Protégée         | Profil utilisateur et édition                 |

---

## Spécifications par page

### 1. Home (/)

**Layout**

```
┌─────────────────────────────────┐
│   LOGO MDD                      │
│   Monde de Dév                  │
│                                 │
│   [Se connecter]  [S'inscrire]  │
│                                 │
│   Bienvenue sur Monde de Dév !  │
│   Partagez vos connaissances    │
└─────────────────────────────────┘
```

**Fonctionnalités**

- Boutons Login et Register
- Redirection automatique vers `/feed` si utilisateur connecté

---

### 2. Login (/login)

**Formulaire**

- Email ou Username (champ texte)
- Password (champ password)
- Bouton "Se connecter"
- Lien vers Register

**Validation**

- Email/Username : non-vide
- Password : non-vide
- Appel API POST `/api/auth/login`
- Stockage JWT dans localStorage
- Redirection vers `/feed` si succès
- Affichage erreur si échec

---

### 3. Register (/register)

**Formulaire**

- Username (2-50 chars)
- Email (format email valide)
- Password (8+ chars, maj, min, chiffre, caractère spécial)
- Confirm Password
- Bouton "S'inscrire"
- Lien vers Login

**Validation côté client**

- Tous les champs requis
- Format email valide
- Password strength (8+ chars, pattern complexe)
- Passwords matchent

**Validation côté serveur**

- Username UNIQUE
- Email UNIQUE
- Password hashé avec BCrypt

---

### 4. Feed (/feed)

**Layout**

```
┌──────────────────────────────────┐
│ [MDD] [Thèmes] [Profil]          │
├──────────────────────────────────┤
│ FIL D'ACTUALITÉ                  │
│ [✏️ Nouvel article]              │
│ [Récent ↓] [Ancien ↑]            │
│                                  │
│ ┌───────────────────────────────┐│
│ │ Titre: Angular 14 Features    ││
│ │ Par: dev_pro • 15 jan 2026    ││
│ │ Theme: Angular [✓]            ││
│ │ [Lire →]                      ││
│ └───────────────────────────────┘│
└──────────────────────────────────┘
```

**Fonctionnalités**

- Affichage articles des thèmes abonnés
- Tri : Récent (défaut) / Ancien
- Bouton "Nouvel article" → `/articles/create`
- Click article → `/articles/:id`
- Affichage : titre, auteur, date, thème

**Données**

- GET `/api/articles` (filtre par abonnements)
- Tri par `createdAt` DESC ou ASC

---

### 5. Article (/articles/:id)

**Layout**

```
┌──────────────────────────────────┐
│ [← Retour]                       │
├──────────────────────────────────┤
│ Titre: Angular 14 Features       │
│ Theme: Angular                   │
│ Par: dev_pro • 15 jan 2026       │
│                                  │
│ [Contenu article complet...]     │
│                                  │
│ ──────────────────────────────── │
│ COMMENTAIRES (2)                 │
│ ┌───────────────────────────────┐│
│ │ Super article !               ││
│ │ john • 15 jan 10:45           ││
│ └───────────────────────────────┘│
│                                  │
│ AJOUTER COMMENTAIRE              │
│ [Texte...]                       │
│ [Envoyer]                        │
└──────────────────────────────────┘
```

**Fonctionnalités**

- Affichage article complet
- Liste commentaires (non-récursif)
- Formulaire ajout commentaire
- Bouton retour vers feed
- Suppression article (si author)

**Données**

- GET `/api/articles/:id`
- POST `/api/articles/:id/comments`
- DELETE `/api/articles/:id` (si author)

---

### 6. Create Article (/articles/create)

**Formulaire**

- Titre (1-255 chars)
- Thème (dropdown, requis)
- Contenu (textarea, 1-5000 chars)
- Bouton "Publier"

**Validation**

- Tous champs requis
- Titre non-vide
- Thème sélectionné
- Contenu non-vide

**Données**

- GET `/api/themes` (pour dropdown)
- POST `/api/articles`
- Redirection vers `/articles/:newId` si succès

---

### 7. Themes (/themes)

**Layout**

```
┌──────────────────────────────────┐
│ TOUS LES THÈMES                  │
│                                  │
│ ┌──────────────┐ ┌────────────┐ │
│ │ Angular      │ │ [✓ Abonné] │ │
│ │ Framework... │ │ [Se désa.] │ │
│ └──────────────┘ └────────────┘ │
│                                  │
│ ┌──────────────┐ ┌────────────┐ │
│ │ Spring Boot  │ │ [S'abonner]│ │
│ │ Java back... │ │            │ │
│ └──────────────┘ └────────────┘ │
└──────────────────────────────────┘
```

**Fonctionnalités**

- Liste tous les thèmes
- Affichage status abonnement
- Bouton Subscribe/Unsubscribe
- Toggle dynamique sans reload

**Données**

- GET `/api/themes`
- POST `/api/themes/:id/subscribe`
- DELETE `/api/themes/:id/unsubscribe`

---

### 8. Profile (/profile)

**Layout**

```
┌──────────────────────────────────┐
│ MON PROFIL                       │
├──────────────────────────────────┤
│ USERNAME: julie                  │
│ EMAIL: julie@example.com         │
│                                  │
│ [Modifier profil]                │
│                                  │
│ MES ABONNEMENTS (3)              │
│ ☑ Angular                        │
│ ☑ Spring Boot                    │
│ ☑ React                          │
│                                  │
│ [Se déconnecter]                 │
└──────────────────────────────────┘
```

**Fonctionnalités**

- Affichage profil (username, email)
- Mode édition : formulaire modifiable
- Validation : email, username unicity
- Affichage abonnements
- Gestion abonnements (check/uncheck)
- Logout : clear token, redirect home

**Données**

- GET `/api/users/me`
- PUT `/api/users/me`
- Logout : localStorage.removeItem('token')

---

## Composants réutilisables

| Composant       | Usage           | Props                        |
| --------------- | --------------- | ---------------------------- |
| **ArticleCard** | Feed            | article, onClick             |
| **CommentList** | Article detail  | comments                     |
| **CommentForm** | Article detail  | articleId, onSubmit          |
| **ThemeList**   | Themes, Profile | themes, subscribed, onToggle |

---

## Services HTTP

### AuthService

```typescript
register(data): Observable<AuthResponse>
login(data): Observable<AuthResponse>
logout(): void
isAuthenticated(): boolean
```

### UserService

```typescript
getProfile(): Observable<UserResponse>
updateProfile(data): Observable<UserResponse>
```

### ArticleService

```typescript
getFeed(): Observable<Article[]>
getArticle(id): Observable<ArticleDetail>
createArticle(data): Observable<Article>
deleteArticle(id): Observable<void>
```

### ThemeService

```typescript
getThemes(): Observable<Theme[]>
subscribe(id): Observable<void>
unsubscribe(id): Observable<void>
```

### CommentService

```typescript
getComments(articleId): Observable<Comment[]>
addComment(articleId, data): Observable<Comment>
```

---

## Routes et Guards

**Routes protégées (AuthGuard)**

- `/feed`
- `/articles/:id`
- `/articles/create`
- `/themes`
- `/profile`

**Routes publiques**

- `/`
- `/login`
- `/register`

**AuthGuard logic**

```typescript
canActivate(): boolean {
  if (localStorage.getItem('token')) {
    return true;
  }
  router.navigate(['/login']);
  return false;
}
```

---

## Responsive Design

**Breakpoints**

- Mobile : < 576px
- Tablet : 576px - 991px
- Desktop : ≥ 992px

**Adaptations**

- Mobile : Stack vertical, full width
- Tablet : 2 colonnes
- Desktop : 3+ colonnes, sidebar

---

## Technologies

- **Framework** : Angular 14
- **Langage** : TypeScript 4.7.4
- **UI Library** : Angular Material 14
- **HTTP** : HttpClient (RxJS)
- **Forms** : Reactive Forms
- **Routing** : Angular Router
- **State** : Services + RxJS
