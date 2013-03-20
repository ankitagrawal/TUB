package com.hk.pact.dao.review;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.review.Mail;
import com.hk.domain.review.ProductReviewMail;
import com.hk.pact.dao.BaseDao;
import com.akube.framework.dao.Page;

import java.util.List;


public interface ProductReviewMailDao extends BaseDao{

    public ProductReviewMail getProductReviewMailByProduct(Product product);

    public Page searchAllProductReviewMail(Product product,Mail mail,int pageNo, int perPage);

    public ProductReviewMail save(ProductReviewMail productReviewMail);

    public void delete(ProductReviewMail productReviewMail);
}
