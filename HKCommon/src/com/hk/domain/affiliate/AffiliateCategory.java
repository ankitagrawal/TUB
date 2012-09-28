package com.hk.domain.affiliate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: USER
 * Date: Jul 21, 2011
 * Time: 5:25:57 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "affiliate_category")
/*@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)*/
public class AffiliateCategory {

  @Id
  @Column(name = "affiliate_category_name", nullable = false, length = 80)
  private String affiliateCategoryName;

  @Column(name = "overview", length = 100)
  private String overview;

  public void setAffiliateCategoryName(String affiliateCategoryName) {
    this.affiliateCategoryName = affiliateCategoryName;
  }

  public String getAffiliateCategoryName() {
    return affiliateCategoryName;
  }

  public String getOverview() {
    return overview;
  }

  public void setOverview(String overview) {
    this.overview = overview;
  }

  @Override
  public String toString() {
    return affiliateCategoryName != null ? affiliateCategoryName : "";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof AffiliateCategory)) return false;

    AffiliateCategory category = (AffiliateCategory) o;

    if (affiliateCategoryName != null ? !affiliateCategoryName.equals(category.affiliateCategoryName) : category.affiliateCategoryName != null)
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    return affiliateCategoryName != null ? affiliateCategoryName.hashCode() : 0;
  }

}