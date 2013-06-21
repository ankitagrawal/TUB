package com.hk.pact.dao.email;

import java.util.Date;
import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.marketing.NotifyMe;
import com.hk.impl.dao.email.NotifyMeDto;
import com.hk.pact.dao.BaseDao;

public interface NotifyMeDao extends BaseDao {

    public NotifyMe save(NotifyMe notifyMe);

    public Page searchNotifyMe(Date startDate, Date endDate, int pageNo, int perPage, Product product, ProductVariant productVariant, Category primaryCategory, Boolean productInStock, Boolean productDeleted);

    public List<NotifyMe> searchNotifyMe(Date startDate, Date endDate, Product product, ProductVariant productVariant, Category primaryCategory, Boolean productInStock, Boolean productDeleted);

    public List<String> getPendingNotifyMeProductVariant();

    public List<NotifyMe> getNotifyMeListForProductVariantInStock();

    public Page getNotifyMeListForProductVariantInStock(int pageNo, int perPage);

    public List<NotifyMe> getAllNotifyMeForSameUser(String notifyMeEmail);

    public List<NotifyMe> getPendingNotifyMeList(String notifyMeEmail, ProductVariant productVariant);

    public List<NotifyMe> getPendingNotifyMeListByVariant(String notifyMeEmail, List<ProductVariant> productVariantList);

    public List<NotifyMe> notifyMeForSimilarProductsMails(Date endDate, Boolean productVariantOutOfStock, Boolean productDeleted);

    public Page getAllNotifyMeList(int pageNo, int perPage, Product product, Category primaryCategory);

    public Page getNotifyMeListForSimilarProducts(int pageNo, int perPage, Product product, Category primaryCategory, Boolean productVariantOutOfStock, Boolean similarProduct);

    public Page notifyMeListForInStockProduct(int pageNo, int perPage ,Product product, Category primaryCategory);


}
