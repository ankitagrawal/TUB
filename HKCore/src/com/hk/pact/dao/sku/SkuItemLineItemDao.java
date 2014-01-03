package com.hk.pact.dao.sku;

import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.StockTransfer;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.CartLineItemExtraOption;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.*;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.dao.BaseDao;


import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nihal
 * Date: 7/24/13
 * Time: 12:20 AM
 * To change this template use File | Settings | File Templates.
 */
public interface SkuItemLineItemDao extends BaseDao {

  public List<SkuItemLineItem> getSkuItemLineItem(LineItem lineItem, Long skuItemStatusId);

  public List<SkuItemCLI> getSkuItemCLI(CartLineItem cartLineItem, List<SkuItemStatus> skuItemStatusIds);

  public SkuItemCLI getSkuItemCLI(SkuItem skuItem);

  public SkuItemLineItem getSkuItemLineItem(SkuItem skuItem);

  public SkuItemCLI getSkuItemCLI(CartLineItem cartLineItem , Long unitNum);

  public List<SkuItemLineItem> getSkuItemLIsTemp();

  public List<SkuItemLineItem> getSkuItemLIsTemp(SkuItem skuItem);

  public boolean sicliAlreadyExists(CartLineItem cartLineItem);
  
  public Long getUnbookedCLICount(ProductVariant productVariant);
  
  public Long getUnbookedLICount(List<Sku> skuList, Double mrp);
  
  //================Methods For Foreign Table=========================

  public ForeignSkuItemCLI getForeignSkuItemCLI(Long id);


  public List<ForeignSkuItemCLI> getForeignSkuItemCli(CartLineItem cartLineItem);

  public ForeignSkuItemCLI getFSICI(Long foreignSkuItemId);

  public SkuGroup createSkuGroupWithoutBarcode(String batch, Date mfgDate, Date expiryDate, Double costPrice, Double mrp, GoodsReceivedNote goodsReceivedNote, ReconciliationVoucher reconciliationVoucher, StockTransfer stockTransfer, Sku sku);
  
  public List<ForeignSkuItemCLI> getForeignSkuItemCliForEye();
  
  public List<CartLineItemExtraOption> getCartLineItemExtraConfigForEye(Long id);

}
