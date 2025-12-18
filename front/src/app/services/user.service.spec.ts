import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { UserService } from './user.service';
import { Theme } from '../models/article.model';

describe('UserService (TDD)', () => {
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

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getUserSubscriptions', () => {
    it('should return user subscriptions', () => {
      const mockThemes: Theme[] = [
        { id: 1, nom: 'Angular', description: 'Framework' },
        { id: 2, nom: 'React', description: 'Library' }
      ];

      service.getUserSubscriptions(1).subscribe((themes) => {
        expect(themes).toEqual(mockThemes);
        expect(themes.length).toBe(2);
      });

      const req = httpMock.expectOne(`${apiUrl}/1/subscriptions`);
      expect(req.request.method).toBe('GET');
      req.flush(mockThemes);
    });

    it('should handle error when fetching subscriptions fails', () => {
      service.getUserSubscriptions(999).subscribe({
        next: () => fail('should have failed'),
        error: (error) => {
          expect(error.status).toBe(404);
        }
      });

      const req = httpMock.expectOne(`${apiUrl}/999/subscriptions`);
      req.flush('User not found', { status: 404, statusText: 'Not Found' });
    });
  });

  describe('unsubscribeFromTheme', () => {
    it('should unsubscribe from a theme', () => {
      service.unsubscribeFromTheme(1, 2).subscribe((response) => {
        expect(response).toBeUndefined();
      });

      const req = httpMock.expectOne('http://localhost:8081/api/themes/2/subscribe');
      expect(req.request.method).toBe('DELETE');
      req.flush(null);
    });

    it('should handle error when unsubscribe fails', () => {
      service.unsubscribeFromTheme(1, 999).subscribe({
        next: () => fail('should have failed'),
        error: (error) => {
          expect(error.status).toBe(404);
        }
      });

      const req = httpMock.expectOne('http://localhost:8081/api/themes/999/subscribe');
      req.flush('Theme not found', { status: 404, statusText: 'Not Found' });
    });
  });

  describe('updateUser', () => {
    it('should update user successfully', () => {
      const mockUpdatedUser = {
        id: 1,
        username: 'john_updated',
        email: 'john.updated@example.com'
      };

      const updateData = {
        username: 'john_updated',
        email: 'john.updated@example.com'
      };

      service.updateUser(1, updateData).subscribe((user) => {
        expect(user).toEqual(mockUpdatedUser);
        expect(user.username).toBe('john_updated');
        expect(user.email).toBe('john.updated@example.com');
      });

      const req = httpMock.expectOne(`${apiUrl}/1`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual(updateData);
      req.flush(mockUpdatedUser);
    });

    it('should update only username', () => {
      const mockUpdatedUser = {
        id: 1,
        username: 'new_username',
        email: 'john@example.com'
      };

      service.updateUser(1, { username: 'new_username' }).subscribe((user) => {
        expect(user.username).toBe('new_username');
      });

      const req = httpMock.expectOne(`${apiUrl}/1`);
      expect(req.request.method).toBe('PUT');
      req.flush(mockUpdatedUser);
    });

    it('should update only email', () => {
      const mockUpdatedUser = {
        id: 1,
        username: 'john_doe',
        email: 'new.email@example.com'
      };

      service.updateUser(1, { email: 'new.email@example.com' }).subscribe((user) => {
        expect(user.email).toBe('new.email@example.com');
      });

      const req = httpMock.expectOne(`${apiUrl}/1`);
      expect(req.request.method).toBe('PUT');
      req.flush(mockUpdatedUser);
    });

    it('should handle error when email already exists', () => {
      const errorResponse = { message: 'Cet email est déjà utilisé' };

      service.updateUser(1, { email: 'taken@example.com' }).subscribe({
        next: () => fail('should have failed'),
        error: (error) => {
          expect(error.status).toBe(400);
          expect(error.error.message).toBe('Cet email est déjà utilisé');
        }
      });

      const req = httpMock.expectOne(`${apiUrl}/1`);
      req.flush(errorResponse, { status: 400, statusText: 'Bad Request' });
    });

    it('should handle error when username already exists', () => {
      const errorResponse = { message: 'Ce nom d\'utilisateur est déjà utilisé' };

      service.updateUser(1, { username: 'taken_username' }).subscribe({
        next: () => fail('should have failed'),
        error: (error) => {
          expect(error.status).toBe(400);
          expect(error.error.message).toBe('Ce nom d\'utilisateur est déjà utilisé');
        }
      });

      const req = httpMock.expectOne(`${apiUrl}/1`);
      req.flush(errorResponse, { status: 400, statusText: 'Bad Request' });
    });

    it('should handle error when user not found', () => {
      const errorResponse = { message: 'Utilisateur non trouvé avec l\'ID: 999' };

      service.updateUser(999, { username: 'test' }).subscribe({
        next: () => fail('should have failed'),
        error: (error) => {
          expect(error.status).toBe(400);
        }
      });

      const req = httpMock.expectOne(`${apiUrl}/999`);
      req.flush(errorResponse, { status: 400, statusText: 'Bad Request' });
    });
  });
});
