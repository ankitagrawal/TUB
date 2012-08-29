package com.hk.impl.dao.catalog.product;

import com.hk.domain.catalog.product.SimilarProduct;
import com.hk.domain.catalog.product.Product;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.catalog.product.SimilarProductsDao;

import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Aug 28, 2012
 * Time: 1:32:53 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class SimilarProductsDaoImpl extends BaseDaoImpl implements SimilarProductsDao{

    public List<SimilarProduct> getSimProdsFromDB (Product inputProduct){
      return (List<SimilarProduct>) findByNamedParams("from SimilarProduct sp where sp.product =  :inputProduct", new String[] { "inputProduct" },
                   new Object[] { inputProduct });
    }

}
