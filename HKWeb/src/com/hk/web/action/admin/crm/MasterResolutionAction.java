package com.hk.web.action.admin.crm;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.analytics.Reason;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

/**
 * Created with IntelliJ IDEA.
 * User: Ankit Chhabra
 * Date: 9/18/13
 * Time: 6:58 PM
 */
public class MasterResolutionAction extends BaseAction {

  Long shippingOrderId;

  Reason reason;

  Integer actionType;

  @DefaultHandler
  public Resolution pre () {


    return new ForwardResolution("/pages/admin/crm/crmMasterControl.jsp");
  }


}
