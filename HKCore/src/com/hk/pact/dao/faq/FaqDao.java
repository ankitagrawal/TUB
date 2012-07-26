package com.hk.pact.dao.faq;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.LowInventory;
import com.hk.domain.faq.Faq;
import com.hk.pact.dao.BaseDao;

import java.util.List;

public interface FaqDao extends BaseDao {

    public Page searchFaq(String keywords, String primaryCategory, String secondaryCategory, int pageNo, int perPage);

    public Page getFaqByCategory(String primaryCategory, String secondaryCategory, int pageNo, int perPage);
    
}