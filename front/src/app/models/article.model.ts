import { User } from './user.model';

export interface Theme {
  id: number;
  name: string;
  description?: string;
}

export interface Article {
  id: number;
  title: string;
  content: string;
  dateCreation: string;
  author: User;
  theme: Theme;
}

export interface CreateArticleRequest {
  title: string;
  content: string;
  themeId: number;
}