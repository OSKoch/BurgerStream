import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateDrink } from './update-drink';

describe('UpdateDrink', () => {
  let component: UpdateDrink;
  let fixture: ComponentFixture<UpdateDrink>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateDrink]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateDrink);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
