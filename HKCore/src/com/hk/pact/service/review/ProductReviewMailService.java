package com.hk.pact.service.review;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.review.ProductReviewMail;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/9/13
 * Time: 12:28 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductReviewMailService {

    public ProductReviewMail getProductReviewMailByProduct(Product product);

    public ProductReviewMail save(ProductReviewMail productReviewMail);

}
