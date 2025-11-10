import { TestBed } from '@angular/core/testing';

import { SizeOptionService } from './size-option-service';

describe('SizeOptionService', () => {
  let service: SizeOptionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SizeOptionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
