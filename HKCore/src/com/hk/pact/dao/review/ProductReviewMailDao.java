package com.hk.pact.dao.review;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.review.ProductReviewMail;
import com.hk.pact.dao.BaseDao;

import java.util.List;


public interface ProductReviewMailDao extends BaseDao{

    public ProductReviewMail getProductReviewMailByProduct(Product product);

    public ProductReviewMail save(ProductReviewMail productReviewMail);
}
