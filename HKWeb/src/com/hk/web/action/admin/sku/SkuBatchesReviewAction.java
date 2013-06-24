package com.hk.web.action.admin.sku;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.constants.sku.EnumSkuGroupStatus;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import net.sourceforge.stripes.action.*;

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
    private LineItem lineItem;
    private SkuGroup searchSkuGroup;
    @Autowired
    private AdminInventoryService adminInventoryService;


    @DefaultHandler
    public Resolution pre() {
        if (skuGroups != null && skuGroups.size() < 1) {
            return new RedirectResolution("/pages/admin/skuGroupReview.jsp");
        }
        skuGroups = adminInventoryService.getInStockSkuGroupsForReview(lineItem);
        return new ForwardResolution("/pages/admin/skuGroupReview.jsp").addParameter("lineItem", lineItem);
    }


    public Resolution markSkuGroupAsUnderReview() {
        if (searchSkuGroup != null) {
            searchSkuGroup.setStatus(EnumSkuGroupStatus.UNDER_REVIEW);
            getBaseDao().save(searchSkuGroup);
        }
        return new RedirectResolution(SkuBatchesReviewAction.class).addParameter("lineItem", lineItem);
    }


    public Resolution reviewBatches() {
        skuGroups = adminInventoryService.getSkuGroupsInReviewState();
        return new ForwardResolution("/pages/admin/skuGroupReviewList.jsp");
    }


    public Resolution ChangeStatus() {
        if (searchSkuGroup == null) {
            addRedirectAlertMessage(new SimpleMessage("Please select Sku group "));
            return new RedirectResolution(SkuBatchesReviewAction.class, "reviewBatches");
        }
         searchSkuGroup.setStatus(EnumSkuGroupStatus.REVIEW_DONE);
         getBaseDao().save(searchSkuGroup);
         addRedirectAlertMessage(new SimpleMessage("Sku Group : " + searchSkuGroup.getId() + " has been reviewed successfully"));
        return new RedirectResolution(SkuBatchesReviewAction.class, "reviewBatches");
    }

    public List<SkuGroup> getSkuGroups() {
        return skuGroups;
    }

    public void setSkuGroups(List<SkuGroup> skuGroups) {
        this.skuGroups = skuGroups;
    }


    public LineItem getLineItem() {
        return lineItem;
    }

    public void setLineItem(LineItem lineItem) {
        this.lineItem = lineItem;
    }

    public SkuGroup getSearchSkuGroup() {
        return searchSkuGroup;
    }

    public void setSearchSkuGroup(SkuGroup searchSkuGroup) {
        this.searchSkuGroup = searchSkuGroup;
    }
}
