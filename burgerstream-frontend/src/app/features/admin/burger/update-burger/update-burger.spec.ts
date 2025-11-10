import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateBurger } from './update-burger';

describe('UpdateBurger', () => {
  let component: UpdateBurger;
  let fixture: ComponentFixture<UpdateBurger>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateBurger]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateBurger);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
