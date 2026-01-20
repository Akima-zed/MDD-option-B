import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { of, throwError } from 'rxjs';
import { ThemesComponent } from './themes.component';
import { ThemeService } from '../../services/theme.service';

describe('ThemesComponent', () => {
  let component: ThemesComponent;
  let fixture: ComponentFixture<ThemesComponent>;
  let themeServiceSpy: Partial<ThemeService>;

  beforeEach(async () => {
    // Spy avec retour d'Observable directement
    themeServiceSpy = {
      getThemes: jest.fn().mockReturnValue(of([])),
      subscribe: jest.fn().mockReturnValue(of(null))
    };

    await TestBed.configureTestingModule({
      imports: [ThemesComponent, HttpClientTestingModule],
      providers: [{ provide: ThemeService, useValue: themeServiceSpy }],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(ThemesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges(); // ngOnInit est appelé ici
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load themes successfully', () => {
    const themesMock = [{ id: 1, name: 'Test', description: '', subscribed: false }];
    (themeServiceSpy.getThemes as jest.Mock).mockReturnValue(of(themesMock));

    component.loadThemes();

    expect(component.themes).toEqual(themesMock);
    expect(component.isLoading).toBe(false);
    expect(component.errorMessage).toBe('');
  });

  it('should handle error when loading themes', () => {
    (themeServiceSpy.getThemes as jest.Mock).mockReturnValue(throwError(() => new Error('Error')));

    component.loadThemes();

    expect(component.themes).toEqual([]);
    expect(component.isLoading).toBe(false);
    expect(component.errorMessage).toBe('Erreur lors du chargement des thèmes');
  });

  it('should mark theme as subscribed', () => {
    const theme = { id: 1, name: 'Test', description: '', subscribed: false };

    component.subscribe(theme);

    expect(theme.subscribed).toBe(true);
  });

  it('should complete destroy$ on ngOnDestroy', () => {
    const nextSpy = jest.spyOn(component['destroy$'], 'next');
    const completeSpy = jest.spyOn(component['destroy$'], 'complete');

    component.ngOnDestroy();

    expect(nextSpy).toHaveBeenCalled();
    expect(completeSpy).toHaveBeenCalled();
  });
});
