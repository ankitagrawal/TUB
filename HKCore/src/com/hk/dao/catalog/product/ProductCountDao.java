package com.hk.dao.catalog.product;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hk.constants.core.RoleConstants;
import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductCount;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.web.filter.WebContext;


@Repository
public class ProductCountDao extends BaseDaoImpl {

 
  @Transactional
  public void getOrCreateProductCount(Product product, User user) {
      Object productTracker = WebContext.getRequest().getSession().getAttribute("productTracker" + product.getId());
    if (productTracker == null || !productTracker.equals(product.getId())) {
        
      if (user == null || !user.getRoles().contains(get(Role.class, RoleConstants.HK_EMPLOYEE))) {
        ProductCount productCount = findByProduct(product);
        if (productCount != null) {
          productCount.setCounter(productCount.getCounter() + 1);
          save(productCount);
        } else {
          ProductCount newProduct = new ProductCount();
          newProduct.setProduct(product);
          newProduct.setCounter(1L);
          save(newProduct);
        }
        WebContext.getRequest().getSession().setAttribute("productTracker" + product.getId(), product.getId());
      }
    }
  }

  @Transactional
  public ProductCount findByProduct(Product product) {
    Criteria criteria = getSession().createCriteria(ProductCount.class);
    criteria.add(Restrictions.eq("product", product));
    return (ProductCount) criteria.uniqueResult();
  }

}

