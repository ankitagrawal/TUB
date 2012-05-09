package com.hk.constants.review;

import java.util.Arrays;
import java.util.List;

import com.hk.domain.review.ReviewStatus;

public enum EnumReviewStatus {

    Pending(10L, "Pending"), Published(20L, "Published"), Deleted(999L, "Deleted");

    private String name;
    private Long   id;

    EnumReviewStatus(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public ReviewStatus asReviewStatus() {
        ReviewStatus reviewStatus = new ReviewStatus();
        reviewStatus.setId(this.getId());
        reviewStatus.setName(this.getName());
        return reviewStatus;
    }

    public static List<Long> getAllStatus() {
        return Arrays.asList(EnumReviewStatus.Pending.getId(), EnumReviewStatus.Published.getId(), EnumReviewStatus.Deleted.getId());
    }
}
