/**
 * Test E2E 3 : Consultation article → Ajout commentaire → Vérification affichage
 * 
 * Ce test vérifie le système de commentaires :
 * 1. Login avec un utilisateur existant
 * 2. Consultation d'un article depuis le fil
 * 3. Ajout d'un commentaire sur l'article
 * 4. Vérification que le commentaire s'affiche correctement
 * 5. Vérification que l'auteur du commentaire est correct
 */

describe('Scénario E2E 3 : Consultation article et ajout commentaire', () => {
  const testUser = {
    email: 'user1@test.com',
    password: 'password123',
  };

  const commentText = `Ceci est un commentaire de test créé le ${new Date().toLocaleString('fr-FR')}`;

  beforeEach(() => {
    cy.clearLocalStorage();
  });

  it('Doit permettre de consulter un article et d\'ajouter un commentaire', () => {
    // ÉTAPE 1 : Login
    cy.visit('/login');
    cy.get('input[formControlName="emailOrUsername"]').type(testUser.email);
    cy.get('input[formControlName="password"]').type(testUser.password);
    cy.get('button[type="submit"]').click();
    cy.wait(500);

    // ÉTAPE 2 : S'assurer d'avoir au moins un abonnement
    cy.visit('/themes');
    cy.wait(500);
    cy.get('button').contains('S\'abonner').first().then(($btn) => {
      if ($btn.length > 0) {
        cy.wrap($btn).click();
        cy.wait(500);
      }
    });

    // ÉTAPE 3 : Aller sur le fil et cliquer sur le premier article
    cy.visit('/feed');
    cy.wait(1000);

    // Vérifier qu'il y a au moins un article
    cy.get('mat-card').should('have.length.greaterThan', 0);

    // Cliquer sur le premier article
    cy.get('mat-card').first().click();
    cy.wait(500);

    // VÉRIFICATION : URL contient /article/:id
    cy.url().should('match', /\/article\/\d+/);

    // VÉRIFICATION : Le contenu de l'article est affiché
    cy.get('h1, h2, [class*="title"]').should('exist');
    cy.get('p, [class*="content"]').should('exist');

    // ÉTAPE 4 : Compter le nombre de commentaires existants
    cy.get('mat-card, .comment-item, [class*="comment"]').its('length').as('initialCommentCount');

    // ÉTAPE 5 : Ajouter un commentaire
    cy.get('textarea[formControlName="content"]').should('be.visible').type(commentText);
    cy.get('button[type="submit"]').contains(/envoyer|publier|send|post/i).click();
    cy.wait(1000);

    // VÉRIFICATION : Le nouveau commentaire est affiché
    cy.contains(commentText).should('be.visible');

    // VÉRIFICATION : Le nombre de commentaires a augmenté
    cy.get('@initialCommentCount').then((initialCount) => {
      cy.get('mat-card, .comment-item, [class*="comment"]').should('have.length', Number(initialCount) + 1);
    });

    // VÉRIFICATION : L'auteur du commentaire est correct
    cy.contains(commentText).parent().within(() => {
      cy.contains(/user1|testuser/i).should('exist');
    });
  });

  it('Doit afficher tous les commentaires d\'un article', () => {
    // ÉTAPE 1 : Login
    cy.visit('/login');
    cy.get('input[formControlName="emailOrUsername"]').type(testUser.email);
    cy.get('input[formControlName="password"]').type(testUser.password);
    cy.get('button[type="submit"]').click();
    cy.wait(500);

    // ÉTAPE 2 : Consulter un article
    cy.visit('/feed');
    cy.wait(1000);
    cy.get('mat-card').first().click();
    cy.wait(500);

    // VÉRIFICATION : Section commentaires existe
    cy.contains(/commentaires|comments/i).should('be.visible');

    // VÉRIFICATION : Chaque commentaire affiche un auteur et une date
    cy.get('mat-card, .comment-item, [class*="comment"]').each(($comment) => {
      cy.wrap($comment).within(() => {
        // Vérifier qu'il y a un auteur
        cy.get('[class*="author"], strong, .username').should('exist');
        
        // Vérifier qu'il y a une date
        cy.get('[class*="date"], small, time').should('exist');
      });
    });
  });

  it('Doit afficher un message si l\'article n\'a aucun commentaire', () => {
    // ÉTAPE 1 : Login
    cy.visit('/login');
    cy.get('input[formControlName="emailOrUsername"]').type(testUser.email);
    cy.get('input[formControlName="password"]').type(testUser.password);
    cy.get('button[type="submit"]').click();
    cy.wait(500);

    // ÉTAPE 2 : Créer un nouvel article (qui n'aura aucun commentaire)
    cy.visit('/themes');
    cy.wait(500);
    cy.get('button').contains('S\'abonner').first().click();
    cy.wait(500);

    cy.visit('/article/create');
    cy.get('mat-select[formControlName="themeId"]').click();
    cy.get('mat-option').first().click();
    cy.get('input[formControlName="title"]').type('Article sans commentaires');
    cy.get('textarea[formControlName="content"]').type('Cet article est créé pour tester l\'affichage sans commentaires.');
    cy.get('button[type="submit"]').click();
    cy.wait(1000);

    // ÉTAPE 3 : Consulter l'article nouvellement créé (premier du fil)
    cy.get('mat-card').first().click();
    cy.wait(500);

    // VÉRIFICATION : Message "Aucun commentaire" ou équivalent
    cy.contains(/aucun commentaire|no comments|soyez le premier|be the first/i).should('be.visible');
  });

  it('Doit empêcher l\'envoi d\'un commentaire vide', () => {
    // ÉTAPE 1 : Login
    cy.visit('/login');
    cy.get('input[formControlName="emailOrUsername"]').type(testUser.email);
    cy.get('input[formControlName="password"]').type(testUser.password);
    cy.get('button[type="submit"]').click();
    cy.wait(500);

    // ÉTAPE 2 : Consulter un article
    cy.visit('/feed');
    cy.wait(1000);
    cy.get('mat-card').first().click();
    cy.wait(500);

    // ÉTAPE 3 : Tenter d'envoyer un commentaire vide
    cy.get('button[type="submit"]').contains(/envoyer|publier|send|post/i).should('be.disabled');

    // Ou si le bouton n'est pas désactivé, vérifier la validation du formulaire
    cy.get('textarea[formControlName="content"]').should('have.class', 'ng-pristine');
  });

  it('Doit afficher les informations de l\'article (titre, auteur, thème, date)', () => {
    // ÉTAPE 1 : Login
    cy.visit('/login');
    cy.get('input[formControlName="emailOrUsername"]').type(testUser.email);
    cy.get('input[formControlName="password"]').type(testUser.password);
    cy.get('button[type="submit"]').click();
    cy.wait(500);

    // ÉTAPE 2 : S'assurer d'avoir un abonnement
    cy.visit('/themes');
    cy.wait(500);
    cy.get('button').contains('S\'abonner').first().click();
    cy.wait(500);

    // ÉTAPE 3 : Consulter le premier article du fil
    cy.visit('/feed');
    cy.wait(1000);
    cy.get('mat-card').first().click();
    cy.wait(500);

    // VÉRIFICATION : Titre de l'article
    cy.get('h1, h2, [class*="title"]').should('exist').and('not.be.empty');

    // VÉRIFICATION : Auteur de l'article
    cy.contains(/auteur|author|par|by/i).should('exist');

    // VÉRIFICATION : Thème de l'article
    cy.get('mat-chip, .theme-badge, [class*="theme"]').should('exist');

    // VÉRIFICATION : Date de création
    cy.get('[class*="date"], small, time').should('exist');

    // VÉRIFICATION : Contenu de l'article
    cy.get('p, [class*="content"]').should('exist').and('not.be.empty');
  });

  it('Doit permettre de revenir au fil depuis un article', () => {
    // ÉTAPE 1 : Login
    cy.visit('/login');
    cy.get('input[formControlName="emailOrUsername"]').type(testUser.email);
    cy.get('input[formControlName="password"]').type(testUser.password);
    cy.get('button[type="submit"]').click();
    cy.wait(500);

    // ÉTAPE 2 : Consulter un article
    cy.visit('/feed');
    cy.wait(1000);
    cy.get('mat-card').first().click();
    cy.wait(500);

    // ÉTAPE 3 : Cliquer sur le bouton retour ou lien "Retour au fil"
    cy.get('button, a').contains(/retour|back|fil|feed/i).click();
    cy.wait(500);

    // VÉRIFICATION : Retour au fil d'actualité
    cy.url().should('include', '/feed');
  });
});
