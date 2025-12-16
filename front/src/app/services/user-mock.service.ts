import { Injectable } from '@angular/core';
import { User } from '../models/user.model';

@Injectable({ providedIn: 'root' })
export class UserMockService {
  private users: User[] = [
    {
      id: 1,
      username: 'johndoe',
      email: 'john@example.com',
      roles: ['USER'],
      dateInscription: '2025-12-16',
      abonnements: [],
      articles: [],
      commentaires: []
    },
    {
      id: 2,
      username: 'janedoe',
      email: 'jane@example.com',
      roles: ['USER'],
      dateInscription: '2025-12-15',
      abonnements: [],
      articles: [],
      commentaires: []
    }
  ];

  getUsers(): User[] {
    return this.users;
  }

  getUserById(id: number): User | undefined {
    return this.users.find(u => u.id === id);
  }
}
