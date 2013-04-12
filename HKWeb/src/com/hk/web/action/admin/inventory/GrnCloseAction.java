package com.hk.web.action.admin.inventory;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.dao.inventory.GoodsReceivedNoteDao;
import com.hk.web.action.admin.AdminHomeAction;
import com.hk.domain.accounting.PoLineItem;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
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

    public Resolution pre() {
        int dayAgo = 21;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -dayAgo);
        Date startDate = cal.getTime();
        List<GoodsReceivedNote> checkedInGrns = goodsReceivedNoteDao.checkinCompletedGrns(startDate);
        if (checkedInGrns != null && checkedInGrns.size() > 0) {
            for (GoodsReceivedNote grn : checkedInGrns) {
                grn.setGrnStatus(EnumGrnStatus.Closed.asGrnStatus());
                for(GrnLineItem grnLineItem : grn.getGrnLineItems()){
    				for(PoLineItem poLineItem: grn.getPurchaseOrder().getPoLineItems()){
    					if(grnLineItem.getSku().getId().equals(poLineItem.getSku().getId())){
    						grnLineItem.setFillRate(poLineItem.getFillRate());
    					}
    				}
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
    
}
