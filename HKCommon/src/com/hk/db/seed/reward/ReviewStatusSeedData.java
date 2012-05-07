package com.hk.db.seed.reward;


import java.util.ArrayList;
import java.util.List;

import com.hk.db.seed.BaseSeedData;


public class ReviewStatusSeedData  extends BaseSeedData {


  public void insert(String name, Long id) {
    ReviewStatus reviewStatus = new ReviewStatus();
    reviewStatus.setName(name);
    reviewStatus.setId(id);
    reviewStatusDao.save(reviewStatus);
  }

  public void invokeInsert() {
    List<Long> pkList = new ArrayList<Long>();

    for (EnumReviewStatus enumReviewStatus : EnumReviewStatus.values()) {

      if (pkList.contains(enumReviewStatus.getId()))
        throw new RuntimeException("Duplicate key " + enumReviewStatus.getId());
      else pkList.add(enumReviewStatus.getId());

      insert(enumReviewStatus.getName(), enumReviewStatus.getId());
    }
  }

}