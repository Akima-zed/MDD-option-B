import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpErrorResponse } from '@angular/common/http';
import { CommentService } from './comment.service';
import { Comment, CreateCommentRequest } from '../models/comment.model';

describe('CommentService (TDD)', () => {
  let service: CommentService;
  let httpMock: HttpTestingController;
  const apiUrl = 'http://localhost:8081/api/articles';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CommentService]
    });
    service = TestBed.inject(CommentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // TEST 1: getCommentsByArticleId
  describe('getCommentsByArticleId()', () => {
    it('should return list of comments for an article', () => {
      const mockComments: Comment[] = [
        {
          id: 1,
          content: 'Premier commentaire',
          dateCreation: '2025-12-17',
          author: { id: 1, username: 'user1', email: 'user1@test.com' },
          articleId: 1
        },
        {
          id: 2,
          content: 'Deuxième commentaire',
          dateCreation: '2025-12-17',
          author: { id: 2, username: 'user2', email: 'user2@test.com' },
          articleId: 1
        }
      ];

      service.getCommentsByArticleId(1).subscribe((comments: Comment[]) => {
        expect(comments.length).toBe(2);
        expect(comments).toEqual(mockComments);
      });

      const req = httpMock.expectOne(`${apiUrl}/1/comments`);
      expect(req.request.method).toBe('GET');
      req.flush(mockComments);
    });

    it('should handle error when getting comments fails', () => {
      service.getCommentsByArticleId(1).subscribe(
        () => fail('should have failed'),
        (error: HttpErrorResponse) => {
          expect(error.status).toBe(404);
        }
      );

      const req = httpMock.expectOne(`${apiUrl}/1/comments`);
      req.flush('Article not found', { status: 404, statusText: 'Not Found' });
    });
  });

  // TEST 2: createComment
  describe('createComment()', () => {
    it('should create a new comment and return it', () => {
      const createData: CreateCommentRequest = {
        content: 'Nouveau commentaire',
        articleId: 1
      };

      const mockResponse: Comment = {
        id: 10,
        content: 'Nouveau commentaire',
        dateCreation: '2025-12-17',
        author: { id: 1, username: 'currentuser', email: 'current@test.com' },
        articleId: 1
      };

      service.createComment(createData).subscribe((comment: Comment) => {
        expect(comment).toEqual(mockResponse);
        expect(comment.id).toBe(10);
        expect(comment.content).toBe('Nouveau commentaire');
      });

      const req = httpMock.expectOne(`${apiUrl}/1/comments`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual({ content: createData.content });
      req.flush(mockResponse);
    });

    it('should handle error when comment creation fails', () => {
      const createData: CreateCommentRequest = {
        content: '',
        articleId: 1
      };

      service.createComment(createData).subscribe(
        () => fail('should have failed'),
        (error: HttpErrorResponse) => {
          expect(error.status).toBe(400);
        }
      );

      const req = httpMock.expectOne(`${apiUrl}/1/comments`);
      req.flush('Validation error', { status: 400, statusText: 'Bad Request' });
    });
  });
});
