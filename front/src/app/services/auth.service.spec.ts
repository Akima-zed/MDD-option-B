import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from './auth.service';
import { RegisterRequest, LoginRequest, AuthResponse } from '../models/user.model';

describe('AuthService (TDD)', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  const apiUrl = 'http://localhost:8080/api/auth';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    
    // Nettoyer le localStorage avant chaque test
    localStorage.clear();
  });

  afterEach(() => {
    httpMock.verify(); // Vérifie qu'aucune requête HTTP n'est en attente
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // TEST 1: Register
  describe('register()', () => {
    it('should register a new user and return AuthResponse', () => {
      const registerData: RegisterRequest = {
        username: 'testuser',
        email: 'test@example.com',
        password: 'Password123!'
      };

      const mockResponse: AuthResponse = {
        token: 'fake-jwt-token',
        user: {
          id: 1,
          username: 'testuser',
          email: 'test@example.com',
          roles: ['USER']
        }
      };

      service.register(registerData).subscribe((response: AuthResponse) => {
        expect(response).toEqual(mockResponse);
        expect(response.token).toBe('fake-jwt-token');
      });

      const req = httpMock.expectOne(`${apiUrl}/register`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(registerData);
      req.flush(mockResponse);
    });

    it('should handle registration error', () => {
      const registerData: RegisterRequest = {
        username: 'testuser',
        email: 'test@example.com',
        password: 'Password123!'
      };

      service.register(registerData).subscribe(
        () => fail('should have failed'),
        (error: HttpErrorResponse) => {
          expect(error.status).toBe(400);
          expect(error.error).toContain('Email already exists');
        }
      );

      const req = httpMock.expectOne(`${apiUrl}/register`);
      req.flush('Email already exists', { status: 400, statusText: 'Bad Request' });
    });
  });

  // TEST 2: Login
  describe('login()', () => {
    it('should login user and store token in localStorage', () => {
      const loginData: LoginRequest = {
        emailOrUsername: 'test@example.com',
        password: 'Password123!'
      };

      const mockResponse: AuthResponse = {
        token: 'fake-jwt-token',
        user: {
          id: 1,
          username: 'testuser',
          email: 'test@example.com'
        }
      };

      service.login(loginData).subscribe((response: AuthResponse) => {
        expect(response).toEqual(mockResponse);
        expect(localStorage.getItem('token')).toBe('fake-jwt-token');
      });

      const req = httpMock.expectOne(`${apiUrl}/login`);
      expect(req.request.method).toBe('POST');
      req.flush(mockResponse);
    });

    it('should handle login error (invalid credentials)', () => {
      const loginData: LoginRequest = {
        emailOrUsername: 'wrong@example.com',
        password: 'WrongPassword'
      };

      service.login(loginData).subscribe(
        () => fail('should have failed'),
        (error: HttpErrorResponse) => {
          expect(error.status).toBe(401);
        }
      );

      const req = httpMock.expectOne(`${apiUrl}/login`);
      req.flush('Invalid credentials', { status: 401, statusText: 'Unauthorized' });
    });
  });

  // TEST 3: Logout
  describe('logout()', () => {
    it('should clear token from localStorage', () => {
      localStorage.setItem('token', 'fake-jwt-token');
      
      service.logout();
      
      expect(localStorage.getItem('token')).toBeNull();
    });
  });

  // TEST 4: GetToken
  describe('getToken()', () => {
    it('should return token from localStorage', () => {
      localStorage.setItem('token', 'fake-jwt-token');
      
      const token = service.getToken();
      
      expect(token).toBe('fake-jwt-token');
    });

    it('should return null if no token exists', () => {
      const token = service.getToken();
      
      expect(token).toBeNull();
    });
  });

  // TEST 5: IsAuthenticated
  describe('isAuthenticated()', () => {
    it('should return true if token exists', () => {
      localStorage.setItem('token', 'fake-jwt-token');
      
      expect(service.isAuthenticated()).toBe(true);
    });

    it('should return false if no token exists', () => {
      expect(service.isAuthenticated()).toBe(false);
    });
  });
});
