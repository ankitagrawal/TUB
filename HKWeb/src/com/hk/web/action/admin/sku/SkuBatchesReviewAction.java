package com.hk.web.action.admin.sku;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.constants.sku.EnumSkuGroupStatus;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import net.sourceforge.stripes.action.DefaultHandler;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Jun 20, 2013
 * Time: 3:10:53 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class SkuBatchesReviewAction extends BaseAction {

    private List<SkuGroup> skuGroups;
     private String   variantId;
     private LineItem lineItem;
     private SkuItem skuItemBarcode;
     private SkuGroup searchSkuGroup;
     @Autowired
     private AdminInventoryService adminInventoryService;


    @DefaultHandler
       public Resolution pre() {
           searchSkuGroup = skuItemBarcode.getSkuGroup();           
           skuGroups = adminInventoryService.getInStockSkuGroupsForReview(lineItem);
           return new ForwardResolution("/pages/admin/skuGroupReview.jsp");
       }


    public Resolution markSkuGroupAsUnderReview() {
         if (searchSkuGroup  != null){
             searchSkuGroup.setStatus(EnumSkuGroupStatus.UNDER_REVIEW);
             getBaseDao().save(searchSkuGroup);

         }

        return new ForwardResolution("/pages/admin/searchSkuBatches.jsp");
    }


    public List<SkuGroup> getSkuGroups() {
        return skuGroups;
    }

    public void setSkuGroups(List<SkuGroup> skuGroups) {
        this.skuGroups = skuGroups;
    }

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    public LineItem getLineItem() {
        return lineItem;
    }

    public void setLineItem(LineItem lineItem) {
        this.lineItem = lineItem;
    }

    public SkuItem getSkuItemBarcode() {
        return skuItemBarcode;
    }

    public void setSkuItemBarcode(SkuItem skuItemBarcode) {
        this.skuItemBarcode = skuItemBarcode;
    }

    public SkuGroup getSearchSkuGroup() {
        return searchSkuGroup;
    }

    public void setSearchSkuGroup(SkuGroup searchSkuGroup) {
        this.searchSkuGroup = searchSkuGroup;
    }
}
