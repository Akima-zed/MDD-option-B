import { Theme } from './theme.model';

export interface UserProfile {
  id: number;
  username: string;
  email: string;
  dateInscription: string;
  abonnements: Theme[];
}


export interface UserUpdateRequest {
  username?: string;
  email?: string;
  password?: string;
}
