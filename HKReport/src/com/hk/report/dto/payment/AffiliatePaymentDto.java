package com.hk.report.dto.payment;

import com.hk.domain.affiliate.Affiliate;

public class AffiliatePaymentDto {
  private Affiliate affiliate;
  private Double amount;

  public Affiliate getAffiliate() {
    return affiliate;
  }

  public void setAffiliate(Affiliate affiliate) {
    this.affiliate = affiliate;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }
}
