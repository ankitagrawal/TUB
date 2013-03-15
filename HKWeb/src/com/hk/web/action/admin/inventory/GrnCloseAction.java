package com.hk.web.action.admin.inventory;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.dao.inventory.GoodsReceivedNoteDao;
import com.hk.web.action.admin.AdminHomeAction;
import com.hk.domain.inventory.GoodsReceivedNote;
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

    public Resolution pre() {
        int dayAgo = 21;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, - dayAgo);
        Date twoWeekBeforedate = cal.getTime();
        List<GoodsReceivedNote> checkinCompltedGrns = goodsReceivedNoteDao.listGRNsCheckinCompletedTwoWeekBefore(twoWeekBeforedate);
        if (checkinCompltedGrns != null && checkinCompltedGrns.size() > 0) {
            for (GoodsReceivedNote grn : checkinCompltedGrns) {
                grn.setGrnStatus(EnumGrnStatus.Closed.asGrnStatus());
            }
            getBaseDao().saveOrUpdate(checkinCompltedGrns);
            addRedirectAlertMessage(new SimpleMessage("Grns created" + dayAgo + " days before are now closed"));
        } else {
            addRedirectAlertMessage(new SimpleMessage("No Grn founds in Checkin Completed State"));
        }

        return new RedirectResolution(AdminHomeAction.class);
    }


}
