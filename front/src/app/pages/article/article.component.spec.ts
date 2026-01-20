import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { ArticleComponent } from './article.component';
import { ArticleService } from '../../services/article.service';
import { CommentService } from '../../services/comment.service';
import { Comment } from '../../models/comment.model';
import { Article } from '../../models/article.model';

describe('ArticleComponent', () => {
  let component: ArticleComponent;
  let fixture: ComponentFixture<ArticleComponent>;
  let articleService: ArticleService;
  let commentService: CommentService;
  let router: Router;


  // préparation de l'environnement de test
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        ArticleComponent,
        HttpClientTestingModule,
        RouterTestingModule,
        NoopAnimationsModule
      ],
      providers: [
        {
          provide: ActivatedRoute, 
          useValue: { snapshot: { paramMap: { get: () => '1' } } }
        }
      ],
      schemas: [NO_ERRORS_SCHEMA] // Ignore les composants inconnus
    }).compileComponents();

    fixture = TestBed.createComponent(ArticleComponent);
    component = fixture.componentInstance;
    articleService = TestBed.inject(ArticleService);
    commentService = TestBed.inject(CommentService);
    router = TestBed.inject(Router);

    fixture.detectChanges();  
  });

  it('doit créer le composant', () => {
    expect(component).toBeTruthy();
  });

  it('doit initialiser le formulaire de commentaire', () => {
    component.ngOnInit();
    expect(component.commentForm).toBeDefined();
    expect(component.commentForm.controls['content']).toBeDefined();
  });

  it('doit avoir isLoading à true lors du loadArticle', async () => {
    const mockArticle: Article = {
      id: 1,
      title: 'Titre test',
      content: 'Contenu test',
      theme: { id: 1, name: 'Thème test', subscribed: false },
      author: { id: 1, username: 'UserTest', email: 'test@test.com' },
      dateCreation: new Date().toISOString()
    };

    jest.spyOn(articleService, 'getArticleById').mockReturnValue(of(mockArticle));

    component.loadArticle(1);
    await fixture.whenStable();//attend que l'observable soit terminé

  expect(component.isLoading).toBe(false);
  expect(component.article).toEqual(mockArticle);
  });

  it('doit ajouter un commentaire quand formulaire valide', () => {
    component.article = {
      id: 1,
      title: 'Titre test',
      content: 'Contenu test',
      theme: { id: 1, name: 'Thème test', subscribed: false },
      author: { id: 1, username: 'UserTest', email: 'test@test.com' },
      dateCreation: new Date().toISOString()
    };

    component.commentForm.setValue({ content: 'Super article' });

    const mockComment: Comment = {
      id: 1,
      content: 'Super article',
      articleId: 1,
      author: { id: 1, username: 'UserTest', email: 'test@test.com' },
      dateCreation: new Date().toISOString()
    };

    jest.spyOn(commentService, 'createComment').mockReturnValue(of(mockComment));

    component.onSubmitComment();

    expect(component.isSubmitting).toBe(false);
    expect(component.comments.length).toBe(1);
    expect(component.comments[0]).toEqual(mockComment);
  });

  it('ne doit pas soumettre si formulaire invalide', () => {
    component.article = {
      id: 1,
      title: 'Titre test',
      content: 'Contenu test',
      theme: { id: 1, name: 'Thème test', subscribed: false },
      author: { id: 1, username: 'UserTest', email: 'test@test.com' },
      dateCreation: new Date().toISOString()
    };

    component.commentForm.setValue({ content: '' }); // invalide
    component.onSubmitComment();

    expect(component.comments.length).toBe(0);
  });

  it('doit gérer erreur lors de la création du commentaire', () => {
    component.article = {
      id: 1,
      title: 'Titre test',
      content: 'Contenu test',
      theme: { id: 1, name: 'Thème test', subscribed: false },
      author: { id: 1, username: 'UserTest', email: 'test@test.com' },
      dateCreation: new Date().toISOString()
    };

    component.commentForm.setValue({ content: 'Erreur' });

    jest.spyOn(commentService, 'createComment').mockReturnValue(throwError(() => new Error('fail')));

    component.onSubmitComment();

    expect(component.isSubmitting).toBe(false);
    expect(component.comments.length).toBe(0);
  });

  it('doit naviguer en arrière', () => {
    const spy = jest.spyOn(router, 'navigate');
    component.goBack();
    expect(spy).toHaveBeenCalledWith(['/feed']);
  });

  it('doit gérer erreur loadComments', () => {
    jest.spyOn(commentService, 'getCommentsByArticleId').mockReturnValue(throwError(() => new Error('fail')));

    component.loadComments(1);

    expect(component.comments.length).toBe(0);
  });
});
