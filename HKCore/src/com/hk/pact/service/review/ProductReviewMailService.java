package com.hk.pact.service.review;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.review.Mail;
import com.hk.domain.review.ProductReviewMail;


public interface ProductReviewMailService {

    public ProductReviewMail getProductReviewMailByProduct(Product product);

    public ProductReviewMail save(ProductReviewMail productReviewMail);

    public Page searchAllProductReviewMail(Product product,Mail mail,int pageNo, int perPage);

    public void delete(ProductReviewMail productReviewMail);

}
