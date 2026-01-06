/**
 * Test E2E 1 : Inscription → Login automatique → Création article → Vérification fil d'actualité
 * 
 * Ce test vérifie le flux complet d'un nouvel utilisateur :
 * 1. S'inscrit sur la plateforme
 * 2. Est automatiquement connecté après inscription
 * 3. Crée un nouvel article
 * 4. Vérifie que l'article apparaît dans le fil d'actualité
 */

describe('Scénario E2E 1 : Inscription et création article', () => {
  const timestamp = Date.now();
  const newUser = {
    username: `testuser${timestamp}`,
    email: `test${timestamp}@example.com`,
    password: 'TestPassword123!',
  };

  const newArticle = {
    title: `Article de test ${timestamp}`,
    content: 'Ceci est le contenu de mon article de test créé via Cypress E2E.',
  };

  beforeEach(() => {
    // Nettoyer le localStorage avant chaque test
    cy.clearLocalStorage();
  });

  it('Doit permettre à un utilisateur de s\'inscrire, se connecter et créer un article', () => {
    // ÉTAPE 1 : Inscription
    cy.visit('/register');
    
    cy.get('input[formControlName="username"]').type(newUser.username);
    cy.get('input[formControlName="email"]').type(newUser.email);
    cy.get('input[formControlName="password"]').type(newUser.password);
    
    cy.get('button[type="submit"]').click();

    // VÉRIFICATION : Redirection vers le fil d'actualité après inscription
    cy.url().should('include', '/feed');
    
    // VÉRIFICATION : Token JWT stocké dans localStorage
    cy.window().then((window) => {
      const token = window.localStorage.getItem('token');
      expect(token).to.exist;
      expect(token).to.not.be.empty;
    });

    // ÉTAPE 2 : S'abonner à un thème (nécessaire pour créer un article)
    cy.visit('/themes');
    cy.wait(500); // Attendre le chargement des thèmes
    
    // S'abonner au premier thème disponible
    cy.get('button').contains('S\'abonner').first().click();
    cy.wait(500); // Attendre la confirmation de l'abonnement

    // ÉTAPE 3 : Créer un nouvel article
    cy.visit('/article/create');
    
    // Sélectionner un thème dans la liste déroulante
    cy.get('mat-select[formControlName="themeId"]').click();
    cy.get('mat-option').first().click();
    
    // Remplir le titre
    cy.get('input[formControlName="title"]').type(newArticle.title);
    
    // Remplir le contenu
    cy.get('textarea[formControlName="content"]').type(newArticle.content);
    
    // Soumettre le formulaire
    cy.get('button[type="submit"]').click();

    // VÉRIFICATION : Redirection vers le fil d'actualité
    cy.url().should('include', '/feed');
    
    // ÉTAPE 4 : Vérifier que l'article apparaît dans le fil
    cy.wait(1000); // Attendre le chargement du fil
    
    // Vérifier que le titre de l'article est visible
    cy.contains(newArticle.title).should('be.visible');
    
    // Vérifier que l'auteur est le bon utilisateur
    cy.contains(newUser.username).should('be.visible');
  });

  it('Doit afficher des erreurs de validation si les champs sont invalides lors de l\'inscription', () => {
    cy.visit('/register');
    
    // Tenter de soumettre le formulaire vide
    cy.get('button[type="submit"]').click();
    
    // VÉRIFICATION : Les champs requis doivent être signalés
    cy.get('input[formControlName="username"]').should('have.class', 'ng-invalid');
    cy.get('input[formControlName="email"]').should('have.class', 'ng-invalid');
    cy.get('input[formControlName="password"]').should('have.class', 'ng-invalid');
  });

  it('Doit afficher une erreur si l\'email est déjà utilisé', () => {
    // Utiliser un email qui existe déjà (depuis DataInitializer)
    cy.visit('/register');
    
    cy.get('input[formControlName="username"]').type('newtestuser');
    cy.get('input[formControlName="email"]').type('user1@test.com'); // Email existant
    cy.get('input[formControlName="password"]').type('TestPassword123!');
    
    cy.get('button[type="submit"]').click();
    
    // VÉRIFICATION : Message d'erreur affiché
    cy.contains(/email.*déjà|email.*exists|already/i).should('be.visible');
  });
});
