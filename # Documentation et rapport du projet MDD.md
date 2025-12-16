# Documentation et rapport du projet MDD

**Auteur** : [Nom et prénom de l’étudiant]  
**Version** : [ex. 1.0.0]  
**Date** : [JJ/MM/AAAA]  

---

## 1. Présentation générale du projet

### 1.1 Objectifs du projet
Brièvement, présentez le but du projet, les besoins métiers et les principales fonctionnalités développées.  
Expliquez le contexte de l’entreprise et la valeur ajoutée attendue du produit.

#### Analyse initiale (Étape 1)

**Flux utilisateurs principaux du MVP :**
1. Inscription d’un utilisateur
2. Connexion (authentification JWT)
3. Consultation et modification du profil utilisateur
4. Consultation de la liste des thèmes, abonnement/désabonnement à un thème
5. Création d’un article (auteur et date générés automatiquement)
6. Consultation d’un article
7. Consultation du fil d’actualité (liste d’articles triés)
8. Ajout d’un commentaire sous un article

**Entités principales du domaine :**
- **Utilisateur** : informations personnelles, identifiants, rôles, abonnements, articles publiés, commentaires
- **Thème** : nom, description, liste d’abonnés
- **Article** : titre, contenu, auteur, date de création, thème associé, liste de commentaires
- **Commentaire** : contenu, auteur, date, article associé

Ces éléments serviront de base à la conception technique et à la modélisation des données lors de l’étape suivante.

#### Exemple de diagramme de séquence — Inscription utilisateur

![alt text][imageCapture]

### 1.2 Périmètre fonctionnel
Présentez les fonctionnalités livrées (liste synthétique), en précisant leur état (terminée / en cours / à venir).

| Fonctionnalités                | Description                              | Statut        |
|---------------------------------|------------------------------------------|---------------|
| Création d’un compte utilisateur | Formulaire et validation d’inscription   |               |
| Publication d’un article        | Gestion CRUD via API                     |               |
| Commentaires                    | Association article/commentaires         |               |
| Authentification                | Sécurisation JWT                         |               |

---

## 2. Architecture et conception technique

### 2.1 Schéma global de l’architecture
![Diagramme de l'architecture](lien_vers_diagramme)

#### Description de l’architecture globale (Étape 2)

L’application Monde de Dév (MDD) est structurée selon une architecture en trois couches :

- **Front-end** : Application Angular (TypeScript), responsable de l’interface utilisateur, de la navigation et de la gestion de l’état. Elle communique exclusivement avec l’API via HTTP(S) et gère l’authentification via JWT (stocké en mémoire ou localStorage).

- **Back-end** : Application Spring Boot (Java 17+), exposant une API REST sécurisée. Elle gère la logique métier, la validation, la sécurité (Spring Security + JWT), et l’accès aux données via Spring Data JPA.

- **Base de données** : MySQL, stockant les utilisateurs, articles, thèmes, abonnements et commentaires. L’accès se fait uniquement via le back-end.

**Sécurité** :
- Authentification et autorisation via JWT (JSON Web Token)
- Endpoints sécurisés (sauf /register et /login)
- Rôles utilisateurs gérés côté back-end
- Données sensibles jamais exposées côté front

**Flux principal** :
1. L’utilisateur interagit avec le front (Angular)
2. Le front appelle l’API REST (Spring Boot)
3. Le back traite la requête, accède à la BDD si besoin, et renvoie la réponse
4. Le front affiche le résultat à l’utilisateur

Un schéma visuel (Draw.io) sera ajouté pour illustrer ces interactions.

Intégrez un diagramme d’architecture (UML, C4 ou équivalent) illustrant les liens entre :

- Le front-end,
- L’API,
- Le back-end et la base de données,
- Les outils externes ou services tiers.

Ajoutez une légende explicative et précisez les choix d’organisation technique (modules, dossiers, conventions internes).

### 2.2 Choix techniques
Présentez ici chaque choix structurant du projet.

| Éléments choisis   | Type                | Objectif du choix                           | Justification                                   |
|--------------------|---------------------|---------------------------------------------|-------------------------------------------------|
| Angular 19         | Framework front-end | Structuration de l’application et gestion de la réactivité | Respect des standards du parcours et cohérence avec les maquettes Figma |

### 2.3 API et schémas de données
Présentez ici la conception et la structuration de votre API :

#### Endpoints REST — Tableau récapitulatif

| Endpoint | Méthode | Description |
|----------|---------|-------------|
| /api/auth/register | POST | Inscription d’un nouvel utilisateur |
| /api/auth/login | POST | Connexion, retourne un JWT |
| /api/users/me | GET | Récupérer le profil de l’utilisateur connecté |
| /api/users/me | PUT | Modifier le profil de l’utilisateur connecté |
| /api/users/{id} | GET | Détail d’un utilisateur (admin ou public limité) |
| /api/articles | GET | Liste des articles (fil d’actualité) |
| /api/articles | POST | Créer un article |
| /api/articles/{id} | GET | Détail d’un article |
| /api/articles/{id} | PUT | Modifier un article (auteur ou admin) |
| /api/articles/{id} | DELETE | Supprimer un article (auteur ou admin) |
| /api/themes | GET | Liste des thèmes |
| /api/themes/{id} | GET | Détail d’un thème |
| /api/themes/{id}/subscribe | POST | S’abonner à un thème |
| /api/themes/{id}/unsubscribe | POST | Se désabonner d’un thème |
| /api/articles/{id}/comments | POST | Ajouter un commentaire à un article |
| /api/articles/{id}/comments | GET | Liste des commentaires d’un article |
| /api/users/me/subscriptions | GET | Liste des thèmes suivis par l’utilisateur |
| /api/themes/{id}/subscribers | GET | Liste des abonnés d’un thème |

#### Exemples de requêtes et réponses JSON

**POST /api/auth/register**
Requête :
```json
{
	"username": "johndoe",
	"email": "john@example.com",
	"password": "MotDePasse123"
}
```
Réponse :
```json
{
	"id": 1,
	"username": "johndoe",
	"email": "john@example.com",
	"roles": ["USER"],
	"dateInscription": "2025-12-16"
}
```

**POST /api/auth/login**
Requête :
```json
{
	"email": "john@example.com",
	"password": "MotDePasse123"
}
```
Réponse :
```json
{
	"token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**GET /api/users/me**
Réponse :
```json
{
	"id": 1,
	"username": "johndoe",
	"email": "john@example.com",
	"roles": ["USER"],
	"dateInscription": "2025-12-16"
}
```

**POST /api/articles**
Requête :
```json
{
	"titre": "Mon premier article",
	"contenu": "Contenu de l’article...",
	"themeId": 2
}
```
Réponse :
```json
{
	"id": 10,
	"titre": "Mon premier article",
	"contenu": "Contenu de l’article...",
	"dateCreation": "2025-12-16",
	"auteur": { "id": 1, "username": "johndoe" },
	"theme": { "id": 2, "nom": "Java" }
}
```

**GET /api/articles**
Réponse :
```json
[
	{
		"id": 10,
		"titre": "Mon premier article",
		"dateCreation": "2025-12-16",
		"auteur": { "id": 1, "username": "johndoe" },
		"theme": { "id": 2, "nom": "Java" }
	}
]
```

**GET /api/themes**
Réponse :
```json
[
	{ "id": 1, "nom": "Java", "description": "Tout sur Java" },
	{ "id": 2, "nom": "Angular", "description": "Front-end moderne" }
]
```

**POST /api/themes/2/subscribe**
Réponse :
```json
{ "message": "Abonnement au thème réussi." }
```

**POST /api/articles/10/comments**
Requête :
```json
{
	"contenu": "Bravo pour cet article !"
}
```
Réponse :
```json
{
	"id": 5,
	"contenu": "Bravo pour cet article !",
	"dateCreation": "2025-12-16",
	"auteur": { "id": 1, "username": "johndoe" }
}
```

#### Modèle de données — Entités principales et relations

**Utilisateur**
- id (PK)
- username
- email
- motDePasse (hashé)
- roles
- dateInscription
- abonnements (liste de Thèmes)
- articles (liste d’Articles)
- commentaires (liste de Commentaires)

**Thème**
- id (PK)
- nom
- description
- abonnés (liste d’Utilisateurs)
- articles (liste d’Articles)

**Article**
- id (PK)
- titre
- contenu
- dateCreation
- auteur (Utilisateur)
- theme (Thème)
- commentaires (liste de Commentaires)

**Commentaire**
- id (PK)
- contenu
- dateCreation
- auteur (Utilisateur)
- article (Article)

**Relations principales**
- Un Utilisateur peut s’abonner à plusieurs Thèmes (relation N-N)
- Un Thème peut avoir plusieurs abonnés (N-N)
- Un Utilisateur peut écrire plusieurs Articles (1-N)
- Un Article appartient à un Thème (N-1)
- Un Article a plusieurs Commentaires (1-N)
- Un Commentaire appartient à un Article (N-1)
- Un Commentaire a un auteur (N-1)

Un schéma UML (Draw.io) sera ajouté pour illustrer ces entités et relations.

| Endpoint         | Méthode | Description                     | Corps / Réponse           |
|------------------|---------|---------------------------------|---------------------------|
| /api/articles    | GET     | Récupère la liste des articles  | JSON – liste d’articles   |
| /api/users/{id}  | GET     | Détail d’un utilisateur         | JSON – profil utilisateur |
| /api/login       | POST    | Authentifie un utilisateur      | Token JWT                 |

Ajoutez une représentation visuelle des relations (UML / diagramme de classes ou entités).

---

## 3. Tests, performance et qualité

### 3.1 Stratégie de test
Décrivez les tests mis en place :  
- unitaires, d’intégration, end-to-end,  
- frameworks utilisés,  
- taux de couverture.

| Type de test   | Outil / framework | Portée                  | Résultats               |
|----------------|-------------------|-------------------------|-------------------------|
| Test unitaire  | JUnit             | Services du back-end     |                         |
| Test d’intégration | [Framework]     | [Détails]               |                         |
| Test e2e       | [Framework]        | [Détails]               |                         |

### 3.2 Rapport de performance et optimisation
Décrivez les actions menées pour améliorer la performance du code et du rendu.

Exemple :  
"Après audit Lighthouse, la performance du front est passée de 65 à 91/100 grâce à l’optimisation des images et du lazy-loading des modules Angular."

### 3.3 Revue technique
Présentez une synthèse critique du code :

**Points forts**  
- Structure, modularité, lisibilité

**Points à améliorer**  
- Complexité, dette technique, sécurité

**Actions correctives appliquées**  
- Refactorisation via héritage de service parent.

---

## 4. Documentation utilisateur et supervision

### 4.1 FAQ utilisateur
Rédigez une courte section d’aide destinée aux utilisateurs internes ou finaux.

**Q : Comment créer un compte ?**  
**R :** Cliquez sur “S’inscrire”, remplissez le formulaire et validez. Vous recevrez un email de confirmation.

**Q : Que faire si l’application ne charge pas ?**  
**R :** Rafraîchissez la page. Si le problème persiste, contactez le support technique.

### 4.2 Supervision et tâches déléguées à l’IA
Décrivez les tâches confiées à l’IA, et comment vous avez vérifié, validé ou corrigé son travail.

| Tâche déléguée         | Outil / collaborateur  | Objectif                            | Vérification effectuée  |
|------------------------|------------------------|-------------------------------------|-------------------------|
| Génération de tests unitaires | GitHub Copilot        | Gain de temps sur les tests basiques | Revue et correction des assertions |

---

## 5. Annexes

- Captures d’écran de l’UI et vues principales.  
- Analyse des besoins front-end (liens avec les spécifications ou maquettes).  
- Définition des données (schémas, formats, règles de validation et sécurisation).  
- Rapports de couverture et de tests (exports ou impressions d’écran).  
- Rapport de revue technique (version complète, datée et signée si applicable).



[imageCapture]: <Capture d'écran 2025-12-12 130614.png>