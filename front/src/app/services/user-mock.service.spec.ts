import { TestBed } from '@angular/core/testing';
import { UserMockService } from './user-mock.service';

describe('UserMockService', () => {
  let service: UserMockService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserMockService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return all users', () => {
    const users = service.getUsers();
    expect(users.length).toBe(2);
    expect(users[0].username).toBe('johndoe');
  });

  it('should return user by id', () => {
    const user = service.getUserById(2);
    expect(user).toBeTruthy();
    expect(user?.username).toBe('janedoe');
  });

  it('should return undefined for unknown id', () => {
    const user = service.getUserById(999);
    expect(user).toBeUndefined();
  });
});
