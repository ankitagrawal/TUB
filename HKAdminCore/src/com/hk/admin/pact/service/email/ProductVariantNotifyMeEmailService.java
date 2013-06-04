package com.hk.admin.pact.service.email;

import com.hk.admin.impl.service.email.ProductInventoryDto;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.marketing.NotifyMe;

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

    public List<ProductInventoryDto> getProductVariantOfSimilarProductWithAvailableInventory(ProductVariant productVariant);

    public List<Product> getInStockSimilarProductsWithMaxInvn(ProductVariant productVariant, int noOfSimilarProduct);
}
