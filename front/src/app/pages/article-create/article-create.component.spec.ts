import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ArticleCreateComponent } from './article-create.component';
import { ArticleService } from '../../services/article.service';
import { ThemeService } from '../../services/theme.service';
import { of, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

// Mocks simples
const routerMock = { navigate: jest.fn() };
const snackBarMock = { open: jest.fn() };

describe('ArticleCreateComponent', () => {
  let component: ArticleCreateComponent;
  let fixture: ComponentFixture<ArticleCreateComponent>;
  let articleService: ArticleService;
  let themeService: ThemeService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        ArticleCreateComponent,
        HttpClientTestingModule,
        RouterTestingModule,
        NoopAnimationsModule
      ],
      providers: [
        { provide: ArticleService, useValue: { createArticle: jest.fn() } },
        { provide: ThemeService, useValue: { getThemes: jest.fn().mockReturnValue(of([])) } },
        { provide: Router, useValue: routerMock },
        { provide: MatSnackBar, useValue: snackBarMock }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(ArticleCreateComponent);
    component = fixture.componentInstance;
    articleService = TestBed.inject(ArticleService);
    themeService = TestBed.inject(ThemeService);
    fixture.detectChanges();
  });

  it('devrait créer le composant', () => {
    expect(component).toBeTruthy();
  });

  it('devrait charger les thèmes', () => {
    const themesMock = [{ id: 1, name: 'Tech', subscribed: false }];
    jest.spyOn(themeService, 'getThemes').mockReturnValue(of(themesMock));

    component.loadThemes();

    expect(component.themes).toEqual(themesMock);
  });

  it('ne doit pas soumettre si formulaire invalide', () => {
    component.articleForm.setValue({
      title: '',
      content: '',
      themeId: ''
    });

    component.onSubmit();

    expect(component.isLoading).toBe(false);
  });

  
  it('devrait appeler createArticle quand le formulaire est valide', fakeAsync(() => {
    const articleMock = {
      id: 1,
      title: 'Titre valide',
      content: 'Contenu suffisamment long',
      dateCreation: new Date().toISOString(),
      author: { id: 1, username: 'user', email: 'user@test.com' },
      theme: { id: 1, name: 'Tech', subscribed: false }
    };

    jest.spyOn(articleService, 'createArticle').mockReturnValue(of(articleMock));

    component.articleForm.setValue({
      title: 'Titre valide',
      content: 'Contenu suffisamment long',
      themeId: 1
    });

    component.onSubmit();
    tick();

    expect(articleService.createArticle).toHaveBeenCalled();
    expect(component.isLoading).toBe(false);
  }));

  it('devrait gérer une erreur lors de la création', fakeAsync(() => {
    jest.spyOn(articleService, 'createArticle').mockReturnValue(
      throwError(() => ({ error: { message: 'Fail' } }))
    );

    component.articleForm.setValue({
      title: 'Titre valide',
      content: 'Contenu suffisamment long',
      themeId: 1
    });

    component.onSubmit();
    tick();

    expect(component.isLoading).toBe(false);
    expect(component.errorMessage).toBe('Fail');
  }));
});