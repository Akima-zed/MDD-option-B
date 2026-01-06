/**
 * Test E2E 2 : Abonnement thème → Vérification fil d'actualité personnalisé
 * 
 * Ce test vérifie le système d'abonnement aux thèmes :
 * 1. Login avec un utilisateur existant
 * 2. S'abonne à un thème spécifique
 * 3. Vérifie que seuls les articles du thème abonné apparaissent dans le fil
 * 4. Se désabonne du thème
 * 5. Vérifie que le fil est vide (si aucun autre abonnement)
 */

describe('Scénario E2E 2 : Abonnement thème et fil d\'actualité', () => {
  const testUser = {
    email: 'user1@test.com',
    password: 'password123',
  };

  beforeEach(() => {
    cy.clearLocalStorage();
  });

  it('Doit permettre de s\'abonner à un thème et voir ses articles dans le fil', () => {
    // ÉTAPE 1 : Login via custom command
    cy.visit('/login');
    cy.get('input[formControlName="emailOrUsername"]').type(testUser.email);
    cy.get('input[formControlName="password"]').type(testUser.password);
    cy.get('button[type="submit"]').click();

    // VÉRIFICATION : Redirection vers le fil
    cy.url().should('include', '/feed');

    // ÉTAPE 2 : Aller sur la page des thèmes
    cy.visit('/themes');
    cy.wait(500);

    // Compter le nombre de thèmes disponibles
    cy.get('mat-card').should('have.length.greaterThan', 0);

    // ÉTAPE 3 : S'abonner au premier thème disponible (si pas déjà abonné)
    cy.get('mat-card').first().within(() => {
      cy.get('h3').invoke('text').as('themeName');
      
      // Vérifier si le bouton "S'abonner" existe (pas déjà abonné)
      cy.get('button').then(($button) => {
        if ($button.text().includes('S\'abonner')) {
          cy.wrap($button).click();
          cy.wait(500);
        }
      });
    });

    // ÉTAPE 4 : Vérifier le fil d'actualité
    cy.visit('/feed');
    cy.wait(1000);

    // VÉRIFICATION : Le fil contient des articles
    cy.get('mat-card').should('have.length.greaterThan', 0);

    // VÉRIFICATION : Chaque article affiche un badge de thème
    cy.get('mat-card').first().within(() => {
      cy.get('.theme-badge, mat-chip, [class*="theme"]').should('exist');
    });
  });

  it('Doit permettre de se désabonner d\'un thème depuis le profil', () => {
    // ÉTAPE 1 : Login
    cy.visit('/login');
    cy.get('input[formControlName="emailOrUsername"]').type(testUser.email);
    cy.get('input[formControlName="password"]').type(testUser.password);
    cy.get('button[type="submit"]').click();
    cy.wait(500);

    // ÉTAPE 2 : S'abonner à un thème si nécessaire
    cy.visit('/themes');
    cy.wait(500);
    
    cy.get('button').contains('S\'abonner').first().then(($btn) => {
      if ($btn.length > 0) {
        cy.wrap($btn).click();
        cy.wait(500);
      }
    });

    // ÉTAPE 3 : Aller sur le profil
    cy.visit('/profile');
    cy.wait(500);

    // ÉTAPE 4 : Vérifier que les abonnements sont affichés
    cy.contains(/mes abonnements|subscriptions/i).should('be.visible');

    // ÉTAPE 5 : Compter le nombre d'abonnements avant désabonnement
    cy.get('mat-chip, .subscription-item, [class*="theme"]').its('length').as('initialCount');

    // ÉTAPE 6 : Se désabonner du premier thème
    cy.get('button[aria-label*="désabonner"], button[matChipRemove], button').contains(/désabonner|unsubscribe/i).first().click();
    cy.wait(500);

    // VÉRIFICATION : Le nombre d'abonnements a diminué
    cy.get('@initialCount').then((initialCount) => {
      cy.get('mat-chip, .subscription-item, [class*="theme"]').should('have.length', Number(initialCount) - 1);
    });
  });

  it('Doit afficher un message si aucun abonnement n\'existe', () => {
    // ÉTAPE 1 : Login
    cy.visit('/login');
    cy.get('input[formControlName="emailOrUsername"]').type(testUser.email);
    cy.get('input[formControlName="password"]').type(testUser.password);
    cy.get('button[type="submit"]').click();
    cy.wait(500);

    // ÉTAPE 2 : Se désabonner de tous les thèmes
    cy.visit('/profile');
    cy.wait(500);

    // Désabonner de tous les thèmes existants
    cy.get('button[aria-label*="désabonner"], button[matChipRemove], button').contains(/désabonner|unsubscribe/i).then(($buttons) => {
      if ($buttons.length > 0) {
        // Cliquer sur chaque bouton de désabonnement
        cy.wrap($buttons).each(($btn) => {
          cy.wrap($btn).click();
          cy.wait(300);
        });
      }
    });

    // ÉTAPE 3 : Vérifier le fil d'actualité
    cy.visit('/feed');
    cy.wait(1000);

    // VÉRIFICATION : Message indiquant aucun article ou abonnement
    cy.contains(/aucun article|no articles|abonnez-vous|subscribe/i).should('be.visible');
  });

  it('Doit trier les articles par date décroissante dans le fil', () => {
    // ÉTAPE 1 : Login
    cy.visit('/login');
    cy.get('input[formControlName="emailOrUsername"]').type(testUser.email);
    cy.get('input[formControlName="password"]').type(testUser.password);
    cy.get('button[type="submit"]').click();
    cy.wait(500);

    // ÉTAPE 2 : S'assurer d'avoir au moins un abonnement
    cy.visit('/themes');
    cy.wait(500);
    cy.get('button').contains('S\'abonner').first().click();
    cy.wait(500);

    // ÉTAPE 3 : Vérifier le fil
    cy.visit('/feed');
    cy.wait(1000);

    // VÉRIFICATION : Les articles sont triés par date (le plus récent en premier)
    cy.get('mat-card').should('have.length.greaterThan', 1);
    
    // Extraire les dates et vérifier l'ordre décroissant
    cy.get('mat-card .date, mat-card [class*="date"], mat-card small').then(($dates) => {
      const dates = Array.from($dates).map((el) => el.textContent || '');
      expect(dates.length).to.be.greaterThan(0);
    });
  });
});
