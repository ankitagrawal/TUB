package com.hk.pact.dao.email;

import java.util.Date;
import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.marketing.NotifyMe;
import com.hk.pact.dao.BaseDao;

public interface NotifyMeDao extends BaseDao {

    public NotifyMe save(NotifyMe notifyMe);

    public Page searchNotifyMe(Date startDate, Date endDate, int pageNo, int perPage, Product product, ProductVariant productVariant, Category primaryCategory);

    public List<String> getPendingNotifyMeProductVariant();

    public List<NotifyMe> getNotifyMeListBetweenDate(Date startDate, Date endDate);

    public List<NotifyMe> getNotifyMeListForProductVariantInStock();

    public Page getNotifyMeListForProductVariantInStock(int pageNo, int perPage);

    public List<NotifyMe> getAllNotifyMeForSameUser(String notifyMeEmail);

    public List<NotifyMe> getPendingNotifyMeList(String notifyMeEmail, ProductVariant productVariant);

}
