import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { FeedComponent } from './feed.component';

describe('FeedComponent', () => {
  let component: FeedComponent;
  let fixture: ComponentFixture<FeedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FeedComponent, HttpClientTestingModule],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(FeedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('doit créer le composant', () => {
    expect(component).toBeTruthy();
  });

  it('doit trier les articles par date décroissante', () => {
    const articles = [
      { id: 1, title: 'A', content: '', dateCreation: '2023-01-01' },
      { id: 2, title: 'B', content: '', dateCreation: '2023-05-01' }
    ] as any;

    const sorted = component['sortArticlesByDateDesc'](articles);

    expect(sorted[0].id).toBe(2);
    expect(sorted[1].id).toBe(1);
  });
});
