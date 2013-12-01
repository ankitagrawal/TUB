package com.hk.admin.pact.dao.marketing;

import com.hk.domain.amazon.AmazonFeed;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.dao.BaseDao;

public interface AmazonFeedDao extends BaseDao {

    public AmazonFeed findByPV(ProductVariant productVariant);

}
