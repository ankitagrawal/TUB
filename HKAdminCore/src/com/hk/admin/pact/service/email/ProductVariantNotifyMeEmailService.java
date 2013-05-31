package com.hk.admin.pact.service.email;

import com.hk.admin.impl.service.email.ProductInventoryDomain;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.marketing.NotifyMe;
import com.hk.impl.dao.email.NotifyMeDto;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 4/18/13
 * Time: 1:07 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductVariantNotifyMeEmailService {

    public void sendNotifyMeEmailForInStockProducts(final float notifyConversionRate, final int bufferRate);

    public int sendNotifyMeEmailForDeletedOOSHidden(final float notifyConversionRate, final int bufferRate,List<NotifyMe> notifyMeList);

    public List<ProductInventoryDomain> getProductVariantOfSimilarProductWithAvailableInventory(ProductVariant productVariant);

    public List<Product> getInStockSimilarProductsWithMaxInvn(ProductVariant productVariant, int noOfSimilarProduct);
}
