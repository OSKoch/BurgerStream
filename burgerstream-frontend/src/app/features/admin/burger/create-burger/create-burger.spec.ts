import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateBurger } from './create-burger';

describe('CreateBurger', () => {
  let component: CreateBurger;
  let fixture: ComponentFixture<CreateBurger>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateBurger]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateBurger);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
