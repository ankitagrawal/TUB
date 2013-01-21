package com.hk.impl.dao.review;

import com.akube.framework.util.BaseUtils;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.review.ProductReviewMail;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.review.ProductReviewMailDao;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class ProductReviewMailDaoImpl extends BaseDaoImpl implements ProductReviewMailDao {

    public ProductReviewMail getProductReviewMailByProduct(Product product){
         return (ProductReviewMail) findUniqueByNamedParams("from ProductReviewMail prm where prm.product = :product", new String[]{"product"}, new Object[]{product});
    }

    public ProductReviewMail save(ProductReviewMail productReviewMail){
        return (ProductReviewMail) super.save(productReviewMail);
    }
}
