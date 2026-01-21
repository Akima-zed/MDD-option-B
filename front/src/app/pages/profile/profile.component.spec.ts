import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { of } from 'rxjs';

import { ProfileComponent } from './profile.component';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';
import { UserProfile } from '../../models/userProfile.model';
import { Router } from '@angular/router';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

// Mocks
const snackBarMock = { open: jest.fn() };

describe('ProfileComponent', () => {
  let component: ProfileComponent;
  let fixture: ComponentFixture<ProfileComponent>;
  let userService: jest.Mocked<UserService>;
  let authService: jest.Mocked<AuthService>;
  let routerMock: { navigate: jest.Mock };

  const mockUser: UserProfile = {
    id: 1,
    username: 'testuser',
    email: 'test@example.com',
    abonnements: [],
    dateInscription: '2024-01-01'
  };

  beforeEach(async () => {
    const userServiceMock = {
      getCurrentUser: jest.fn().mockReturnValue(of(mockUser)),
      updateUser: jest.fn().mockReturnValue(of(mockUser)),
      unsubscribeFromTheme: jest.fn().mockReturnValue(of({}))
    } as unknown as jest.Mocked<UserService>;

    const authServiceMock = {
      logout: jest.fn()
    } as unknown as jest.Mocked<AuthService>;

    routerMock = {
      navigate: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [
        ProfileComponent,
        HttpClientTestingModule,
        NoopAnimationsModule
      ],
      providers: [
        { provide: UserService, useValue: userServiceMock },
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(ProfileComponent);
    component = fixture.componentInstance;

    userService = TestBed.inject(UserService) as jest.Mocked<UserService>;
    authService = TestBed.inject(AuthService) as jest.Mocked<AuthService>;

    fixture.detectChanges();
  });

  it('devrait créer le composant', () => {
    expect(component).toBeTruthy();
  });

  it('devrait initialiser le formulaire et charger l’utilisateur', () => {
    expect(component.profileForm).toBeDefined();
    expect(component.profileForm.get('username')?.value).toBe('testuser');
    expect(component.profileForm.get('email')?.value).toBe('test@example.com');
    expect(userService.getCurrentUser).toHaveBeenCalled();
  });

  it('devrait rendre le formulaire invalide si les champs sont vides', () => {
    component.profileForm.setValue({ username: '', email: '', password: '' });
    expect(component.profileForm.invalid).toBe(true);
  });

  it('ne doit pas appeler updateUser si le formulaire est invalide', () => {
    component.profileForm.setValue({ username: '', email: '', password: '' });
    component.saveProfile();
    expect(userService.updateUser).not.toHaveBeenCalled();
  });

  it('devrait appeler updateUser si le formulaire est valide', () => {
    component.profileForm.setValue({
      username: 'Julie',
      email: 'julie@example.com',
      password: 'Test123!'
    });

    component.saveProfile();

    expect(userService.updateUser).toHaveBeenCalledWith({
      username: 'Julie',
      email: 'julie@example.com',
      password: 'Test123!'
    });
  });

  it('devrait appeler logout et rediriger vers la home', () => {
    component.logout();

    expect(authService.logout).toHaveBeenCalled();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/']);
  });
});