# Tests E2E avec Cypress

Ce dossier contient les tests end-to-end (E2E) de l'application MDD, écrits avec Cypress.

## 📁 Structure

```
cypress/
├── e2e/                                      # Tests E2E
│   ├── 01-register-and-create-article.cy.ts  # Scénario 1 : Inscription + Création article
│   ├── 02-theme-subscription-and-feed.cy.ts  # Scénario 2 : Abonnements thèmes + Fil
│   └── 03-article-view-and-comment.cy.ts     # Scénario 3 : Consultation + Commentaires
├── support/
│   ├── commands.ts                           # Commandes custom (login, logout)
│   └── e2e.ts                               # Configuration support E2E
└── cypress.config.ts                         # Configuration Cypress
```

## 🎯 Scénarios couverts

### Scénario 1 : Inscription et création d'article
- ✅ Inscription d'un nouvel utilisateur
- ✅ Connexion automatique après inscription
- ✅ Abonnement à un thème
- ✅ Création d'un article avec titre, contenu et thème
- ✅ Vérification de l'affichage dans le fil d'actualité
- ✅ Validation des champs (erreurs si invalides)
- ✅ Gestion des erreurs (email déjà utilisé)

**Tests** : 3 specs

### Scénario 2 : Abonnement thème et fil d'actualité
- ✅ Login utilisateur existant
- ✅ Abonnement à un thème depuis la page Thèmes
- ✅ Vérification des articles du thème dans le fil
- ✅ Désabonnement depuis le profil utilisateur
- ✅ Vérification du tri chronologique (date décroissante)
- ✅ Message si aucun abonnement

**Tests** : 4 specs

### Scénario 3 : Consultation article et commentaires
- ✅ Login utilisateur existant
- ✅ Consultation d'un article depuis le fil
- ✅ Ajout d'un commentaire
- ✅ Vérification de l'affichage du commentaire
- ✅ Vérification des informations article (titre, auteur, thème, date)
- ✅ Navigation retour vers le fil
- ✅ Validation formulaire commentaire (empêcher commentaire vide)
- ✅ Message si aucun commentaire

**Tests** : 6 specs

## 🚀 Exécution des tests

### Prérequis
Avant de lancer les tests E2E, assurez-vous que :
1. **Backend** tourne sur `http://localhost:8081`
2. **Frontend** tourne sur `http://localhost:4200`
3. **Base de données** MySQL est connectée avec données de test

```bash
# Terminal 1 : Backend
cd back
./mvnw spring-boot:run

# Terminal 2 : Frontend
cd front
npm start
```

### Mode interactif (recommandé pour développement)
```bash
cd front
npm run cypress:open
# ou
npm run e2e:open
```

Une interface Cypress s'ouvre, permettant de :
- Sélectionner les tests à exécuter
- Voir l'exécution en temps réel
- Inspecter l'état de l'application à chaque étape
- Déboguer facilement

### Mode headless (CI/CD)
```bash
cd front
npm run cypress:run
# ou
npm run e2e
```

Exécute tous les tests sans interface graphique. Résultats affichés dans le terminal.

### Exécuter un seul fichier de test
```bash
npx cypress run --spec "cypress/e2e/01-register-and-create-article.cy.ts"
```

## 📊 Résultats attendus

**Total : 13 tests**
- Scénario 1 : 3 tests ✅
- Scénario 2 : 4 tests ✅
- Scénario 3 : 6 tests ✅

**Durée estimée** : ~2-3 minutes (dépend de la performance du backend)

## 🛠️ Commandes custom

### `cy.login(email, password)`
Connexion rapide via API REST (plus rapide que via UI)

```typescript
cy.login('user1@test.com', 'password123');
cy.visit('/feed');
```

### `cy.logout()`
Déconnexion en supprimant le token du localStorage

```typescript
cy.logout();
cy.visit('/login');
```

## 🐛 Debugging

### Voir les requêtes HTTP
Ouvrez la console développeur (F12) pendant l'exécution des tests dans Cypress UI.

### Voir le localStorage
```typescript
cy.window().then((window) => {
  console.log(window.localStorage.getItem('token'));
});
```

### Ajouter des pauses
```typescript
cy.pause(); // Met le test en pause (mode interactif uniquement)
cy.wait(1000); // Attendre 1 seconde
```

### Voir les éléments trouvés
```typescript
cy.get('mat-card').debug(); // Affiche les éléments dans la console
```

## 📝 Bonnes pratiques

1. **Utiliser des sélecteurs stables** :
   - ✅ `data-testid`, `formControlName`, `type="submit"`
   - ❌ Classes CSS générées dynamiquement

2. **Éviter les `cy.wait()` avec temps fixe** :
   - ✅ `cy.get('.article').should('be.visible')`
   - ❌ `cy.wait(3000)`

3. **Nettoyer l'état entre les tests** :
   ```typescript
   beforeEach(() => {
     cy.clearLocalStorage();
   });
   ```

4. **Utiliser des données uniques** :
   ```typescript
   const timestamp = Date.now();
   const email = `test${timestamp}@example.com`;
   ```

## 🔧 Configuration

Fichier `cypress.config.ts` :
- **baseUrl** : `http://localhost:4200`
- **viewportWidth** : 1280px
- **viewportHeight** : 720px
- **video** : Désactivé (pour réduire le temps d'exécution)
- **screenshotOnRunFailure** : Activé (pour déboguer les échecs)

## 📚 Documentation Cypress

- [Documentation officielle](https://docs.cypress.io)
- [Best practices](https://docs.cypress.io/guides/references/best-practices)
- [API Reference](https://docs.cypress.io/api/table-of-contents)

## ✅ Checklist validation

- [x] Installation Cypress
- [x] Configuration `cypress.config.ts`
- [x] Commandes custom (`login`, `logout`)
- [x] Scénario 1 : Inscription + Création article (3 tests)
- [x] Scénario 2 : Abonnement thème + Fil (4 tests)
- [x] Scénario 3 : Consultation + Commentaires (6 tests)
- [x] Scripts npm (`cypress:open`, `cypress:run`, `e2e`)
- [ ] Exécution complète des tests (à valider)
- [ ] Screenshots en cas d'échec
- [ ] Documentation README principale mise à jour

---

**Version** : 1.0.0  
**Date** : 6 janvier 2026  
**Auteur** : Équipe MDD - ORION
