// ***********************************************
// Custom Cypress commands
// ***********************************************

/**
 * Custom command to login via API and store token
 * @example cy.login('user1@test.com', 'password123')
 */
Cypress.Commands.add('login', (email: string, password: string) => {
  cy.request({
    method: 'POST',
    url: 'http://localhost:8081/api/auth/login',
    body: {
      emailOrUsername: email,
      password: password,
    },
  }).then((response) => {
    expect(response.status).to.eq(200);
    expect(response.body.token).to.exist;
    
    // Store token in localStorage (comme le fait l'app Angular)
    window.localStorage.setItem('token', response.body.token);
    window.localStorage.setItem('userId', response.body.id.toString());
  });
});

/**
 * Custom command to logout
 * @example cy.logout()
 */
Cypress.Commands.add('logout', () => {
  window.localStorage.removeItem('token');
  window.localStorage.removeItem('userId');
});
