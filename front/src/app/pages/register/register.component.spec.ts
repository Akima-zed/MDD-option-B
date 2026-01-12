import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { NO_ERRORS_SCHEMA, DebugElement } from '@angular/core';
import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authServiceSpy: Partial<AuthService>;

  beforeEach(async () => {
    authServiceSpy = { register: jest.fn() };

    await TestBed.configureTestingModule({
      imports: [ 
        RegisterComponent, 
        HttpClientTestingModule,
        RouterTestingModule,
        NoopAnimationsModule
      ],
      providers: [ { provide: AuthService, useValue: authServiceSpy } ],
      schemas: [ NO_ERRORS_SCHEMA ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should mark form invalid when password does not meet policy', () => {
    component.registerForm.controls['username'].setValue('user');
    component.registerForm.controls['email'].setValue('user@test.com');
    component.registerForm.controls['password'].setValue('password123'); // no uppercase or special

    expect(component.registerForm.invalid).toBe(true);
    expect(component.registerForm.get('password')?.hasError('pattern')).toBe(true);
  });

  it('should call authService.register when form is valid and submitted', () => {
    const response = { token: 'fake', id: 2, username: 'newuser', email: 'newuser@test.com' } as any;
    (authServiceSpy.register as jest.Mock).mockReturnValue(of(response));

    component.registerForm.controls['username'].setValue('newuser');
    component.registerForm.controls['email'].setValue('newuser@test.com');
    component.registerForm.controls['password'].setValue('Password123!');

    expect(component.registerForm.valid).toBe(true);

    fixture.detectChanges();
    // directly call submit handler to avoid DOM click timing issues
    component.onSubmit();

    expect(authServiceSpy.register).toHaveBeenCalledWith({ username: 'newuser', email: 'newuser@test.com', password: 'Password123!' });
  });
});
