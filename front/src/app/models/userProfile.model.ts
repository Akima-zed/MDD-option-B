import { Theme } from './theme.model';

export interface UserProfile {
  id: number;
  username: string;
  email: string;
  dateInscription: string;
  abonnements: Theme[];
}
