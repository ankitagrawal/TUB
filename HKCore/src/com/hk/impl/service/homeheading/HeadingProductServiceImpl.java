package com.hk.impl.service.homeheading;

import com.hk.domain.content.PrimaryCategoryHeading;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.homeheading.HeadingProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.content.HeadingProduct;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Nov 28, 2012
 * Time: 1:21:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class HeadingProductServiceImpl implements HeadingProductService {

  @Autowired
  BaseDao baseDao;

  @SuppressWarnings("unchecked")
  public List<HeadingProduct> getHeadingProductsByHeadingId(PrimaryCategoryHeading heading){
   return (List<HeadingProduct>)getBaseDao().findByNamedQueryAndNamedParam("getHeadingProductsByHeadingId", new String[]{"heading"}, new Object[]{heading}); 
  }

  public BaseDao getBaseDao() {
    return baseDao;
  }
}
