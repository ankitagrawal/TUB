package com.hk.db.seed.reward;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.review.EnumReviewStatus;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.review.ReviewStatus;


@Component
public class ReviewStatusSeedData  extends BaseSeedData {


  public void insert(String name, Long id) {
    ReviewStatus reviewStatus = new ReviewStatus();
    reviewStatus.setName(name);
    reviewStatus.setId(id);
    save(reviewStatus);
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