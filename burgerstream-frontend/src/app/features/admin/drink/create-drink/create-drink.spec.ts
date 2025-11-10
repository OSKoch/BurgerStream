import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateDrink } from './create-drink';

describe('CreateDrink', () => {
  let component: CreateDrink;
  let fixture: ComponentFixture<CreateDrink>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateDrink]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateDrink);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
