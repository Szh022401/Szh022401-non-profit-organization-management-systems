import { TestBed } from '@angular/core/testing';

import { FundingBasketService } from './funding-basket.service';

describe('FundingBasketService', () => {
  let service: FundingBasketService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FundingBasketService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
