import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { AuthGuard } from './auth.guard';
import { AuthService } from '../services/auth.service';

describe('AuthGuard (TDD)', () => {
  let guard: AuthGuard;
  let authService: jest.Mocked<AuthService>;
  let router: jest.Mocked<Router>;

  beforeEach(() => {
    const authServiceSpy = {
      isAuthenticated: jest.fn()
    } as any;
    const routerSpy = {
      navigate: jest.fn()
    } as any;

    TestBed.configureTestingModule({
      providers: [
        AuthGuard,
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    });

    guard = TestBed.inject(AuthGuard);
    authService = TestBed.inject(AuthService) as jest.Mocked<AuthService>;
    router = TestBed.inject(Router) as jest.Mocked<Router>;
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });

  describe('canActivate()', () => {
    it('should allow access when user is authenticated', () => {
      authService.isAuthenticated.mockReturnValue(true);

      const result = guard.canActivate();

      expect(result).toBe(true);
      expect(authService.isAuthenticated).toHaveBeenCalled();
      expect(router.navigate).not.toHaveBeenCalled();
    });

    it('should redirect to login when user is not authenticated', () => {
      authService.isAuthenticated.mockReturnValue(false);

      const result = guard.canActivate();

      expect(result).toBe(false);
      expect(authService.isAuthenticated).toHaveBeenCalled();
      expect(router.navigate).toHaveBeenCalledWith(['/login']);
    });
  });
});
