package com.hk.admin.impl.dao.inventory;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.akube.framework.dao.Page;
import com.akube.framework.util.DateUtils;
import com.hk.admin.pact.dao.inventory.StockTransferDao;
import com.hk.domain.inventory.StockTransfer;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;

public class StockTransferDaoImpl extends BaseDaoImpl implements StockTransferDao {

    
    public Page searchStockTransfer(Date createDate, String userLogin, Warehouse fromWarehouse, Warehouse toWarehouse, int pageNo, int perPage) {
        DetachedCriteria stockTransferCriteria = DetachedCriteria.forClass(StockTransfer.class);
        if (createDate != null) {
            stockTransferCriteria.add(Restrictions.gt("createDate", createDate));
            stockTransferCriteria.add(Restrictions.lt("createDate", DateUtils.getEndOfDay(createDate)));
        }

        if (fromWarehouse != null) {
            stockTransferCriteria.add(Restrictions.eq("fromWarehouse", fromWarehouse));
        }

        if (toWarehouse != null) {
            stockTransferCriteria.add(Restrictions.eq("toWarehouse", toWarehouse));
        }

        if (!StringUtils.isBlank(userLogin)) {
            DetachedCriteria userCriteria = stockTransferCriteria.createCriteria("createdBy");
            userCriteria.add(Restrictions.like("login".toLowerCase(), "%" + userLogin.toLowerCase() + "%"));
        }
        return list(stockTransferCriteria, pageNo, perPage);
    }

}
