package com.hk.web.action.admin.queue;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.pact.service.inventory.SkuGroupService;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Oct 1, 2013
 * Time: 4:18:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class PutListAction extends BaseAction {

  @Autowired
  SkuGroupService skuGroupService;

  private static Logger logger = LoggerFactory.getLogger(PutListAction.class);

  private GoodsReceivedNote grn;
  private Map<Sku, List<SkuGroup>> skuSkuGroupMap = new HashMap<Sku, List<SkuGroup>>();


  public Resolution putList() {
    List<GrnLineItem> grnLineItemList = grn.getGrnLineItems();
    if (grnLineItemList != null) {
      try {
        for (GrnLineItem grnLineItem : grnLineItemList) {
          Sku sku = grnLineItem.getSku();
          List<SkuGroup> skuGroups = skuGroupService.getCurrentCheckedInBatchGrn(grn, sku);
          if (skuGroups != null && !skuGroups.isEmpty()) {
            if (skuSkuGroupMap.get(sku) != null) {
              skuSkuGroupMap.get(sku).addAll(skuGroups);
            } else {
              skuSkuGroupMap.put(sku, skuGroups);
            }
          }
        }
      } catch (Exception e) {

        System.out.print("Exception occur while generating Put List" + e.getMessage());
        logger.error("Exception occur while generating Put List" + e.getMessage());
        addRedirectAlertMessage(new SimpleMessage("Exception occur while generating Put List " + grn.getId()));
        return new ForwardResolution("/pages/admin/inventory/putList.jsp");
      }
    }

    return new ForwardResolution("/pages/admin/inventory/putList.jsp");
  }

  public GoodsReceivedNote getGrn() {
    return grn;
  }

  public void setGrn(GoodsReceivedNote grn) {
    this.grn = grn;
  }

  public Map<Sku, List<SkuGroup>> getSkuSkuGroupMap() {
    return skuSkuGroupMap;
  }

  public void setSkuSkuGroupMap(Map<Sku, List<SkuGroup>> skuSkuGroupMap) {
    this.skuSkuGroupMap = skuSkuGroupMap;
  }
}
