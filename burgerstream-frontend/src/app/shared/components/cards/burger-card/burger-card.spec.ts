import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BurgerCard } from './burger-card';

describe('BurgerCard', () => {
  let component: BurgerCard;
  let fixture: ComponentFixture<BurgerCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BurgerCard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BurgerCard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
