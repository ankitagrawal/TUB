package com.hk.impl.dao.review;

import com.akube.framework.util.BaseUtils;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.review.ProductReviewMail;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.review.ProductReviewMailDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/7/13
 * Time: 4:18 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ProductReviewMailDaoImpl extends BaseDaoImpl implements ProductReviewMailDao {

    public ProductReviewMail getProductReviewMailByProduct(Product product){
         return (ProductReviewMail) findUniqueByNamedParams("from ProductReviewMail prm where prm.product = :product", new String[]{"product"}, new Object[]{product});
    }

    public ProductReviewMail save(ProductReviewMail productReviewMail){
        return (ProductReviewMail) super.save(productReviewMail);
    }
    /*public Boolean isMailingEnabled(String productId){
        return getSession().createQuery("select p.isEnabled from ProductReviewMail p where p.product.id = :productId").setString("productId", productId).equals("true");
    }
    public List<ProductReviewMail> getMailSetting(String productId){
        return getSession().createQuery("select p from ProductReviewMail p where p.product.id = :productId").setString("productId", productId).list();
    } */


}
