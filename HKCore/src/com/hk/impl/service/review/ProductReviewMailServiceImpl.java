package com.hk.impl.service.review;

import com.akube.framework.util.BaseUtils;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.review.ProductReviewMail;
import com.hk.pact.dao.review.ProductReviewMailDao;
import com.hk.pact.service.review.ProductReviewMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProductReviewMailServiceImpl implements ProductReviewMailService {

    @Autowired
    private ProductReviewMailDao productReviewMailDao;

    public ProductReviewMail getProductReviewMailByProduct(Product product){
        return productReviewMailDao.getProductReviewMailByProduct(product);
    }
    public ProductReviewMail save(ProductReviewMail productReviewMail){
        if(productReviewMail.getCreateDt()==null){
            productReviewMail.setCreateDt(BaseUtils.getCurrentTimestamp());
        }
        productReviewMail.setUpdateDt(BaseUtils.getCurrentTimestamp());
        return productReviewMailDao.save(productReviewMail);
    }
}
