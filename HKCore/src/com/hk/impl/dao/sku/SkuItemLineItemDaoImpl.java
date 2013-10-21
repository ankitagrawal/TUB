package com.hk.impl.dao.sku;

import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.StockTransfer;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.*;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.sku.*;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nihal
 * Date: 7/24/13
 * Time: 12:21 AM
 * To change this template use File | Settings | File Templates.
 */

@Repository
public class SkuItemLineItemDaoImpl extends BaseDaoImpl implements SkuItemLineItemDao {

  @Autowired
  BaseDao baseDao;

  @Override
  public List<SkuItemLineItem> getSkuItemLineItem(LineItem lineItem, Long skuItemStatusId) {
    String sql = "from SkuItemLineItem s where s.lineItem = :lineItem and s.skuItem.skuItemStatus.id= :skuItemStatusId";
    Query query = getSession().createQuery(sql).setParameter("lineItem", lineItem).setParameter("skuItemStatusId", skuItemStatusId);
    return (List<SkuItemLineItem>) query.list();
  }

  @Override
  public List<SkuItemCLI> getSkuItemCLI(CartLineItem cartLineItem, List<SkuItemStatus> skuItemStatusIds) {
    String sql = "from SkuItemCLI s where s.cartLineItem = :cartLineItem and s.skuItem.skuItemStatus in (:skuItemStatusIds)";
    Query query = getSession().createQuery(sql).setParameter("cartLineItem", cartLineItem).setParameterList("skuItemStatusIds", skuItemStatusIds);
    return (List<SkuItemCLI>) query.list();
  }

  @Override
  public SkuItemCLI getSkuItemCLI(SkuItem skuItem) {
    String sql = "from SkuItemCLI s where s.skuItem = :skuItem order by s.id desc";
    List<SkuItemCLI> siliList = (List<SkuItemCLI>) getSession().createQuery(sql).setParameter("skuItem", skuItem).list();
    return siliList != null && !siliList.isEmpty() ? siliList.get(0) : null;
  }

  public SkuItemLineItem getSkuItemLineItem(SkuItem skuItem) {
    String sql = "from SkuItemLineItem s where s.skuItem = :skuItem order by s.id desc";
    List<SkuItemLineItem> siliList = (List<SkuItemLineItem>) getSession().createQuery(sql).setParameter("skuItem", skuItem).list();
    return siliList != null && !siliList.isEmpty() ? siliList.get(0) : null;
  }

  public SkuItemCLI getSkuItemCLI(CartLineItem cartLineItem , Long unitNum) {
    String sql = "from SkuItemCLI s where s.cartLineItem = :cartLineItem and s.unitNum = :unitNum";
    return (SkuItemCLI) getSession().createQuery(sql).setParameter("cartLineItem", cartLineItem).setParameter("unitNum", unitNum).uniqueResult();
  }

  public List<SkuItemLineItem> getSkuItemLIsTemp() {
    String sql = "from SkuItemLineItem s group by s.skuItem having count(s.skuItem) > 1";
    return (List<SkuItemLineItem>) getSession().createQuery(sql).list();
  }

  public List<SkuItemLineItem> getSkuItemLIsTemp(SkuItem skuItem) {
    String sql = "from SkuItemLineItem s where s.skuItem = :skuItem";
    return (List<SkuItemLineItem>) getSession().createQuery(sql).setParameter("skuItem", skuItem).list();
  }

  public boolean sicliAlreadyExists(CartLineItem cartLineItem) {
    String sql = " from SkuItemCLI sicli where sicli.cartLineItem = :cartlineItem ";
    List<SkuItemCLI> siclis = (List<SkuItemCLI>) getSession().createQuery(sql).setParameter("cartlineItem", cartLineItem).list();
    if (siclis != null && siclis.size() > 0) {
      if (siclis.size() == cartLineItem.getQty()) {
        return true;
      }
    }
    return false;
  }

  public Long getUnbookedCLICount(ProductVariant productVariant) {
    String query = "select sum(c.qty) from CartLineItem c " +
        "where  c.productVariant = :productVariant and c.order.orderStatus.id in (:orderStatusIds) and c.skuItemCLIs.size <= 0 ";
    Long cliQty = (Long) getSession().createQuery(query)
        .setParameterList("orderStatusIds", Arrays.asList(EnumOrderStatus.Placed.getId()))
        .setParameter("productVariant", productVariant).uniqueResult();

    return cliQty != null ? cliQty : 0L;
  }

  public Long getUnbookedLICount(List<Sku> skuList, Double mrp) {
    Long liQty = 0L;
    if(skuList != null && !skuList.isEmpty()){
    List<Long> statuses = new ArrayList<Long>();
    statuses.addAll(EnumShippingOrderStatus.getShippingOrderStatusIDs(EnumShippingOrderStatus.getStatusForBookedInventory()));

    String query2 = " select sum(li.qty) from LineItem li inner join li.shippingOrder as so " +
        "where li.sku in (:skuList) and so.shippingOrderStatus.id in (:shippingOrderStatusIds) and li.skuItemLineItems.size <= 0 ";
      if(mrp != null){
      query2 += "and li.markedPrice = :mrp ";
      }

      Query query = getSession().createQuery(query2)
          .setParameterList("shippingOrderStatusIds", statuses)
          .setParameterList("skuList", skuList);
      if (mrp != null) {
        query = query.setParameter("mrp", mrp);
      }

      liQty = (Long) query.uniqueResult();

    }
    return liQty != null ? liQty : 0L;
  }
  
  public ForeignSkuItemCLI getForeignSkuItemCLI(Long id){
    return get(ForeignSkuItemCLI.class, id);
  }

  public List<ForeignSkuItemCLI> getForeignSkuItemCli(CartLineItem cartLineItem){
    String sql = "from ForeignSkuItemCLI f where f.cartLineItem = :cartLineItem";
    return (List<ForeignSkuItemCLI>)getSession().createQuery(sql).setParameter("cartLineItem", cartLineItem).list();
  }


  public ForeignSkuItemCLI getFSICI(Long foreignSkuItemId){
    String sql = "from ForeignSkuItemCLI f where f.skuItemId = :foreignSkuItemId";
    return (ForeignSkuItemCLI) getSession().createQuery(sql).setParameter("foreignSkuItemId", foreignSkuItemId).uniqueResult();
  }
  public SkuGroup createSkuGroupWithoutBarcode(String batch, Date mfgDate, Date expiryDate, Double costPrice, Double mrp, GoodsReceivedNote goodsReceivedNote, ReconciliationVoucher reconciliationVoucher, StockTransfer stockTransfer, Sku sku) {
    SkuGroup skuGroup = new SkuGroup();
    skuGroup.setBatchNumber(batch);
    skuGroup.setMfgDate(mfgDate);
    skuGroup.setExpiryDate(expiryDate);
    skuGroup.setCostPrice(costPrice);
    skuGroup.setMrp(mrp);
    skuGroup.setGoodsReceivedNote(goodsReceivedNote);
    skuGroup.setReconciliationVoucher(reconciliationVoucher);
    skuGroup.setStockTransfer(stockTransfer);
    skuGroup.setSku(sku);
    skuGroup.setCreateDate(new Date());
    skuGroup = (SkuGroup)baseDao.save(skuGroup);
    return skuGroup;
  }


}
