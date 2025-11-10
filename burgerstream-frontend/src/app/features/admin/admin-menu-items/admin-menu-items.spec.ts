import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminMenuItems } from './admin-menu-items';

describe('AdminMenuItems', () => {
  let component: AdminMenuItems;
  let fixture: ComponentFixture<AdminMenuItems>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminMenuItems]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminMenuItems);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
