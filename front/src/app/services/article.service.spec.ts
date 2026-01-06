import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpErrorResponse } from '@angular/common/http';
import { ArticleService } from './article.service';
import { Article, CreateArticleRequest } from '../models/article.model';

describe('ArticleService (TDD)', () => {
  let service: ArticleService;
  let httpMock: HttpTestingController;
  const apiUrl = 'http://localhost:8081/api/articles';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ArticleService]
    });
    service = TestBed.inject(ArticleService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // TEST 1: getArticles
  describe('getArticles()', () => {
    it('should return list of articles from API', () => {
      const mockArticles: Article[] = [
        {
          id: 1,
          titre: 'Article 1',
          contenu: 'Contenu 1',
          dateCreation: '2025-12-17',
          auteur: { id: 1, username: 'user1', email: 'user1@test.com' },
          theme: { id: 1, nom: 'Java' }
        },
        {
          id: 2,
          titre: 'Article 2',
          contenu: 'Contenu 2',
          dateCreation: '2025-12-16',
          auteur: { id: 2, username: 'user2', email: 'user2@test.com' },
          theme: { id: 2, nom: 'Angular' }
        }
      ];

      service.getArticles().subscribe((articles: Article[]) => {
        expect(articles.length).toBe(2);
        expect(articles).toEqual(mockArticles);
      });

      const req = httpMock.expectOne(apiUrl);
      expect(req.request.method).toBe('GET');
      req.flush(mockArticles);
    });

    it('should handle error when getting articles fails', () => {
      service.getArticles().subscribe(
        () => fail('should have failed'),
        (error: HttpErrorResponse) => {
          expect(error.status).toBe(500);
        }
      );

      const req = httpMock.expectOne(apiUrl);
      req.flush('Server error', { status: 500, statusText: 'Internal Server Error' });
    });
  });

  // TEST 2: getArticleById
  describe('getArticleById()', () => {
    it('should return a single article by ID', () => {
      const mockArticle: Article = {
        id: 1,
        titre: 'Article Test',
        contenu: 'Contenu test',
        dateCreation: '2025-12-17',
        auteur: { id: 1, username: 'testuser', email: 'test@test.com' },
        theme: { id: 1, nom: 'Java' }
      };

      service.getArticleById(1).subscribe((article: Article) => {
        expect(article).toEqual(mockArticle);
        expect(article.id).toBe(1);
      });

      const req = httpMock.expectOne(`${apiUrl}/1`);
      expect(req.request.method).toBe('GET');
      req.flush(mockArticle);
    });

    it('should handle error when article not found', () => {
      service.getArticleById(999).subscribe(
        () => fail('should have failed'),
        (error: HttpErrorResponse) => {
          expect(error.status).toBe(404);
        }
      );

      const req = httpMock.expectOne(`${apiUrl}/999`);
      req.flush('Article not found', { status: 404, statusText: 'Not Found' });
    });
  });

  // TEST 3: createArticle
  describe('createArticle()', () => {
    it('should create a new article and return it', () => {
      const createData: CreateArticleRequest = {
        titre: 'Nouvel article',
        contenu: 'Contenu du nouvel article',
        themeId: 1
      };

      const mockResponse: Article = {
        id: 10,
        titre: 'Nouvel article',
        contenu: 'Contenu du nouvel article',
        dateCreation: '2025-12-17',
        auteur: { id: 1, username: 'currentuser', email: 'current@test.com' },
        theme: { id: 1, nom: 'Java' }
      };

      service.createArticle(createData).subscribe((article: Article) => {
        expect(article).toEqual(mockResponse);
        expect(article.id).toBe(10);
      });

      const req = httpMock.expectOne(apiUrl);
      expect(req.request.method).toBe('POST');
      // Le service transforme titre -> title et contenu -> content
      expect(req.request.body).toEqual({
        title: createData.titre,
        content: createData.contenu,
        themeId: createData.themeId
      });
      req.flush(mockResponse);
    });

    it('should handle error when creation fails', () => {
      const createData: CreateArticleRequest = {
        titre: '',
        contenu: 'Contenu',
        themeId: 1
      };

      service.createArticle(createData).subscribe(
        () => fail('should have failed'),
        (error: HttpErrorResponse) => {
          expect(error.status).toBe(400);
        }
      );

      const req = httpMock.expectOne(apiUrl);
      req.flush('Validation error', { status: 400, statusText: 'Bad Request' });
    });
  });
});
