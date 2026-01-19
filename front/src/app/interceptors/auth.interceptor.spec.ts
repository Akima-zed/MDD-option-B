import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { AuthInterceptor } from './auth.interceptor';
import { AuthService } from '../services/auth.service';

describe('AuthInterceptor (TDD)', () => {
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;
  let authService: jest.Mocked<AuthService>;

  beforeEach(() => {
    const authServiceSpy = {
      getToken: jest.fn()
    } as any;

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        {
          provide: HTTP_INTERCEPTORS,
          useClass: AuthInterceptor,
          multi: true
        }
      ]
    });

    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
    authService = TestBed.inject(AuthService) as jest.Mocked<AuthService>;
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('devrait ajouter l’en-tête Authorization lorsque le token existe', () => {
    const mockToken = 'fake-jwt-token-12345';
    authService.getToken.mockReturnValue(mockToken);

    httpClient.get('/api/test').subscribe();

    const req = httpMock.expectOne('/api/test');
    expect(req.request.headers.has('Authorization')).toBe(true);
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${mockToken}`);
    req.flush({});
  });

  it('ne devrait PAS ajouter l’en-tête Authorization lorsque le token est null', () => {
    authService.getToken.mockReturnValue(null);

    httpClient.get('/api/test').subscribe();

    const req = httpMock.expectOne('/api/test');
    expect(req.request.headers.has('Authorization')).toBe(false);
    req.flush({});
  });

  it('ne devrait PAS ajouter l’en-tête Authorization lorsque le token est une chaîne vide', () => {
    authService.getToken.mockReturnValue('');

    httpClient.get('/api/test').subscribe();

    const req = httpMock.expectOne('/api/test');
    expect(req.request.headers.has('Authorization')).toBe(false);
    req.flush({});
  });

  it('devrait fonctionner avec les requêtes POST', () => {
    const mockToken = 'another-jwt-token';
    authService.getToken.mockReturnValue(mockToken);

    httpClient.post('/api/articles', { title: 'Test' }).subscribe();

    const req = httpMock.expectOne('/api/articles');
    expect(req.request.method).toBe('POST');
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${mockToken}`);
    req.flush({});
  });

  it('devrait fonctionner avec les requêtes PUT', () => {
    const mockToken = 'jwt-for-put';
    authService.getToken.mockReturnValue(mockToken);

    httpClient.put('/api/users/1', { username: 'Updated' }).subscribe();

    const req = httpMock.expectOne('/api/users/1');
    expect(req.request.method).toBe('PUT');
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${mockToken}`);
    req.flush({});
  });

  it('devrait fonctionner avec les requêtes DELETE', () => {
    const mockToken = 'jwt-for-delete';
    authService.getToken.mockReturnValue(mockToken);

    httpClient.delete('/api/themes/1').subscribe();

    const req = httpMock.expectOne('/api/themes/1');
    expect(req.request.method).toBe('DELETE');
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${mockToken}`);
    req.flush({});
  });
});
