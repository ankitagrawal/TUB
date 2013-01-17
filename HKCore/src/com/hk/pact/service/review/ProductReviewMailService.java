package com.hk.pact.service.review;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.review.ProductReviewMail;


public interface ProductReviewMailService {

    public ProductReviewMail getProductReviewMailByProduct(Product product);

    public ProductReviewMail save(ProductReviewMail productReviewMail);

}
