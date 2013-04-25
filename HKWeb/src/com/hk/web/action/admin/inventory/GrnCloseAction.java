package com.hk.web.action.admin.inventory;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.dao.inventory.GoodsReceivedNoteDao;
import com.hk.admin.pact.dao.inventory.PoLineItemDao;
import com.hk.admin.pact.service.rtv.ExtraInventoryService;
import com.hk.web.action.admin.AdminHomeAction;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.constants.inventory.EnumGrnStatus;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.action.RedirectResolution;


import java.util.Date;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Mar 15, 2013
 * Time: 11:52:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class GrnCloseAction extends BaseAction {

    @Autowired
    private GoodsReceivedNoteDao goodsReceivedNoteDao;
    @Autowired
    private AdminEmailManager adminEmailManager;
    @Autowired 
    private ExtraInventoryService extraInventoryService;
    @Autowired
	PoLineItemDao poLineItemDao;

    public Resolution pre() {
        int dayAgo = 21;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -dayAgo);
        Date startDate = cal.getTime();
        List<GoodsReceivedNote> checkedInGrns = goodsReceivedNoteDao.checkinCompletedGrns(startDate);
        if (checkedInGrns != null && checkedInGrns.size() > 0) {
        	for(PoLineItem poLineItem: checkedInGrns.get(0).getPurchaseOrder().getPoLineItems()){
				if(poLineItemDao.getPoLineItemCountBySku(poLineItem.getSku()) <= 1) {
					poLineItem.setFirstTimePurchased(true);
				}
            }
            for (GoodsReceivedNote grn : checkedInGrns) {
                grn.setGrnStatus(EnumGrnStatus.Closed.asGrnStatus());
                if(grn.getPurchaseOrder().isExtraInventoryCreated()){
                	PurchaseOrder po = grn.getPurchaseOrder();
                	Long id = getExtraInventoryService().getExtraInventoryByPoId(po.getId()).getId();
                	po.setExtraInventoryId(id);
                }
    			getAdminEmailManager().sendGRNEmail(grn);
            }
            getBaseDao().saveOrUpdate(checkedInGrns);
            
            addRedirectAlertMessage(new SimpleMessage(checkedInGrns.size() + " Grns created : " + dayAgo + " days ago are closed now."));
        } else {
            addRedirectAlertMessage(new SimpleMessage("No Grn has been found in Checkin Completed State"));
        }

        return new RedirectResolution(AdminHomeAction.class);
    }

	public AdminEmailManager getAdminEmailManager() {
		return adminEmailManager;
	}

	public void setAdminEmailManager(AdminEmailManager adminEmailManager) {
		this.adminEmailManager = adminEmailManager;
	}

	public ExtraInventoryService getExtraInventoryService() {
		return extraInventoryService;
	}

	public void setExtraInventoryService(ExtraInventoryService extraInventoryService) {
		this.extraInventoryService = extraInventoryService;
	}
	
    
}
