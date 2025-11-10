import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SideCard } from './side-card';

describe('SideCard', () => {
  let component: SideCard;
  let fixture: ComponentFixture<SideCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SideCard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SideCard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
