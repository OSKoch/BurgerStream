import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateSide } from './create-side';

describe('CreateSide', () => {
  let component: CreateSide;
  let fixture: ComponentFixture<CreateSide>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateSide]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateSide);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
