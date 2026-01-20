import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpErrorResponse } from '@angular/common/http';
import { ThemeService } from './theme.service';
import { Theme } from '../models/article.model';

describe('ThemeService (TDD)', () => {
  let service: ThemeService;
  let httpMock: HttpTestingController;
  const apiUrl = 'http://localhost:8081/api/themes';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ThemeService]
    });
    service = TestBed.inject(ThemeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // TEST 1: getThemes
  describe('getThemes()', () => {
    it('should return list of themes from API', () => {
      const mockThemes: Theme[] = [
        {
          id: 1, name: 'Java', description: 'Langage Java',
          subscribed: false
        },
        {
          id: 2, name: 'Angular', description: 'Framework Angular',
          subscribed: false
        },
        {
          id: 3, name: 'Python', description: 'Langage Python',
          subscribed: false
        }
      ];

      service.getThemes().subscribe((themes: Theme[]) => {
        expect(themes.length).toBe(3);
        expect(themes).toEqual(mockThemes);
      });

      const req = httpMock.expectOne(apiUrl);
      expect(req.request.method).toBe('GET');
      req.flush(mockThemes);
    });

    it('should handle error when getting themes fails', () => {
      service.getThemes().subscribe( // déprécié mais nécessaire pour le test
        () => fail('should have failed'),
        (error: HttpErrorResponse) => {
          expect(error.status).toBe(500);
        }
      );

      const req = httpMock.expectOne(apiUrl);
      req.flush('Server error', { status: 500, statusText: 'Internal Server Error' });
    });
  });

  // TEST 2: getThemeById
  describe('getThemeById()', () => {
    it('should return a single theme by ID', () => {
      const mockTheme: Theme = {
        id: 1,
        name: 'Java',
        description: 'Langage de programmation Java',
        subscribed: false
      };

      service.getThemeById(1).subscribe((theme: Theme) => {
        expect(theme).toEqual(mockTheme);
        expect(theme.id).toBe(1);
      });

      const req = httpMock.expectOne(`${apiUrl}/1`);
      expect(req.request.method).toBe('GET');
      req.flush(mockTheme);
    });

    it('should handle error when theme not found', () => {
      service.getThemeById(999).subscribe(
        () => fail('should have failed'),
        (error: HttpErrorResponse) => {
          expect(error.status).toBe(404);
        }
      );

      const req = httpMock.expectOne(`${apiUrl}/999`);
      req.flush('Theme not found', { status: 404, statusText: 'Not Found' });
    });
  });

  // TEST 3: subscribe
  describe('subscribe()', () => {
    it('should subscribe user to a theme', () => {
      service.subscribe(1).subscribe((response: void) => {
        expect(response).toBeNull();
      });

      const req = httpMock.expectOne(`${apiUrl}/1/subscribe`);
      expect(req.request.method).toBe('POST');
      req.flush(null);
    });

    it('should handle error when subscription fails', () => {
      service.subscribe(1).subscribe(
        () => fail('should have failed'),
        (error: HttpErrorResponse) => {
          expect(error.status).toBe(400);
        }
      );

      const req = httpMock.expectOne(`${apiUrl}/1/subscribe`);
      req.flush('Already subscribed', { status: 400, statusText: 'Bad Request' });
    });
  });

  // TEST 4: unsubscribe
  describe('unsubscribe()', () => {
    it('should unsubscribe user from a theme', () => {
      service.unsubscribe(1).subscribe((response: void) => {
        expect(response).toBeNull();
      });

      const req = httpMock.expectOne(`${apiUrl}/1/unsubscribe`);
      expect(req.request.method).toBe('POST');
      req.flush(null);
    });

    it('should handle error when unsubscription fails', () => {
      service.unsubscribe(1).subscribe(
        () => fail('should have failed'),
        (error: HttpErrorResponse) => {
          expect(error.status).toBe(400);
        }
      );

      const req = httpMock.expectOne(`${apiUrl}/1/unsubscribe`);
      req.flush('Not subscribed', { status: 400, statusText: 'Bad Request' });
    });
  });
});
