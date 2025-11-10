import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateSide } from './update-side';

describe('UpdateSide', () => {
  let component: UpdateSide;
  let fixture: ComponentFixture<UpdateSide>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateSide]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateSide);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
