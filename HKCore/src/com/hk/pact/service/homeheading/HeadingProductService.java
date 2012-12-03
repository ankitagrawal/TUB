package com.hk.pact.service.homeheading;

import com.hk.domain.content.PrimaryCategoryHeading;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.content.HeadingProduct;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Nov 28, 2012
 * Time: 1:20:07 PM
 * To change this template use File | Settings | File Templates.
 */
public interface HeadingProductService {

  public List<HeadingProduct> getHeadingProductsByHeadingId(PrimaryCategoryHeading heading);

  public HeadingProduct getHeadingProductByHeadingAndProductId(PrimaryCategoryHeading heading, String productId);

  public List<HeadingProduct>  getHeadingProductsSortedByRank(Long headingId);
  
  public void delete(HeadingProduct headingProduct);

  public HeadingProduct save(HeadingProduct headingProduct);
}
