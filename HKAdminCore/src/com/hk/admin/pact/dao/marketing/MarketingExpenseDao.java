package com.hk.admin.pact.dao.marketing;

import java.util.Date;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.marketing.AdNetworks;
import com.hk.domain.marketing.MarketingExpense;
import com.hk.pact.dao.BaseDao;

public interface MarketingExpenseDao extends BaseDao {

    public Page searchMarketingExpense(Category category,  Long adNetworksId, Date startDate, Date endDate, int pageNo, int perPage);

    public MarketingExpense checkUniqueMarketingExpense(Category category, Date date, AdNetworks adNetworks);

}
