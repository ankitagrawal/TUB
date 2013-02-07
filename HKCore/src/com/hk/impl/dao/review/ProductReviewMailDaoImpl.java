package com.hk.impl.dao.review;

import com.akube.framework.dao.Page;
import com.akube.framework.util.BaseUtils;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.review.Mail;
import com.hk.domain.review.ProductReviewMail;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.review.ProductReviewMailDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class ProductReviewMailDaoImpl extends BaseDaoImpl implements ProductReviewMailDao {

    public ProductReviewMail getProductReviewMailByProduct(Product product){
         return (ProductReviewMail) findUniqueByNamedParams("from ProductReviewMail prm where prm.product = :product", new String[]{"product"}, new Object[]{product});
    }

    public Page searchAllProductReviewMail(Mail mail,int pageNo, int perPage){
        return list(getProductReviewMailCriteria(mail),pageNo,perPage);
    }

    private DetachedCriteria getProductReviewMailCriteria(Mail mail) {
        DetachedCriteria productReviewMailCriteria = DetachedCriteria.forClass(ProductReviewMail.class);

        if (mail != null) {
            productReviewMailCriteria.add(Restrictions.eq("mail", mail));
        }
        productReviewMailCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));

        return productReviewMailCriteria;
    }

    public void delete(ProductReviewMail productReviewMail){
        super.delete(productReviewMail);
    }

    public ProductReviewMail save(ProductReviewMail productReviewMail){
        return (ProductReviewMail) super.save(productReviewMail);
    }
}
