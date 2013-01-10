package com.hk.pact.dao.review;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.review.ProductReviewMail;
import com.hk.pact.dao.BaseDao;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/7/13
 * Time: 1:25 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductReviewMailDao extends BaseDao{
    //public List<ProductReviewMail> getMailSetting(String productId);
    //public Boolean isMailingEnabled(String productId);
    public ProductReviewMail getProductReviewMailByProduct(Product product);

    public ProductReviewMail save(ProductReviewMail productReviewMail);
}
