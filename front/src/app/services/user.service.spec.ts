import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { UserService } from './user.service';
import { UserProfile } from '../models/userProfile.model';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;
  const apiUrl = 'http://localhost:8081/api/users';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UserService]
    });

    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('devrait être créé', () => {
    const assertion = expect(service);
    assertion.toBeTruthy();
  });

  describe('unsubscribeFromTheme', () => {
    it('devrait désabonner un utilisateur d’un thème', () => {
      service.unsubscribeFromTheme(2).subscribe((response) => {
        const assertion = expect(response);
        assertion.toBeUndefined();
      });

      const req = httpMock.expectOne('http://localhost:8081/api/themes/2/unsubscribe');

      const assertionMethod = expect(req.request.method);
      assertionMethod.toBe('POST');

      const assertionBody = expect(req.request.body);
      assertionBody.toEqual({});

      req.flush(null);
    });

    it('devrait gérer une erreur lors du désabonnement', () => {
      service.unsubscribeFromTheme(999).subscribe({
        next: () => fail('la requête aurait dû échouer'),
        error: (error) => {
          const assertion = expect(error.status);
          assertion.toBe(404);
        }
      });

      const req = httpMock.expectOne('http://localhost:8081/api/themes/999/unsubscribe');
      req.flush('Theme not found', { status: 404, statusText: 'Not Found' });
    });
  });

  describe('updateUser', () => {
    it('devrait mettre à jour un utilisateur', () => {
      const mockUpdatedUser: UserProfile = {
        id: 1,
        username: 'john_updated',
        email: 'john.updated@example.com',
        dateInscription: '',
        abonnements: []
      };

      const updateData = {
        username: 'john_updated',
        email: 'john.updated@example.com'
      };

      service.updateUser(updateData).subscribe((user) => {
        const assertion = expect(user);
        assertion.toEqual(mockUpdatedUser);
      });

      const req = httpMock.expectOne(`${apiUrl}/me`);

      const assertionMethod = expect(req.request.method);
      assertionMethod.toBe('PUT');

      const assertionBody = expect(req.request.body);
      assertionBody.toEqual(updateData);

      req.flush(mockUpdatedUser);
    });

    it('devrait gérer une erreur si l’email existe déjà', () => {
      const errorResponse = { message: 'Cet email est déjà utilisé' };

      service.updateUser({ email: 'taken@example.com' }).subscribe({
        next: () => fail('la requête aurait dû échouer'),
        error: (error) => {
          const assertionStatus = expect(error.status);
          assertionStatus.toBe(400);

          const assertionMessage = expect(error.error.message);
          assertionMessage.toBe('Cet email est déjà utilisé');
        }
      });

      const req = httpMock.expectOne(`${apiUrl}/me`);
      req.flush(errorResponse, { status: 400, statusText: 'Bad Request' });
    });

    it('devrait gérer une erreur si le nom d’utilisateur existe déjà', () => {
      const errorResponse = { message: 'Ce nom d\'utilisateur est déjà utilisé' };

      service.updateUser({ username: 'taken_username' }).subscribe({
        next: () => fail('la requête aurait dû échouer'),
        error: (error) => {
          const assertionStatus = expect(error.status);
          assertionStatus.toBe(400);

          const assertionMessage = expect(error.error.message);
          assertionMessage.toBe('Ce nom d\'utilisateur est déjà utilisé');
        }
      });

      const req = httpMock.expectOne(`${apiUrl}/me`);
      req.flush(errorResponse, { status: 400, statusText: 'Bad Request' });
    });
  });

  describe('getCurrentUser', () => {
    it('devrait récupérer le profil utilisateur', () => {
      const mockUser: UserProfile = {
        id: 1,
        username: 'john_doe',
        email: 'john@example.com',
        dateInscription: '',
        abonnements: []
      };

      service.getCurrentUser().subscribe((user) => {
        const assertion = expect(user);
        assertion.toEqual(mockUser);
      });

      const req = httpMock.expectOne(`${apiUrl}/me`);

      const assertionMethod = expect(req.request.method);
      assertionMethod.toBe('GET');

      req.flush(mockUser);
    });
  });
});