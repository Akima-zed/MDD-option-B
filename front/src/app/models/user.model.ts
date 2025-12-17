export interface User {
  id: number;
  username: string;
  email: string;
  password?: string; // Ne sera jamais renvoyé par l'API
  roles?: string[];
  dateInscription?: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

export interface LoginRequest {
  emailOrUsername: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  id: number;
  username: string;
  email: string;
}
