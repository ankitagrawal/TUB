package com.hk.web.action.admin.crm;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.analytics.EnumReasonType;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.analytics.Reason;
import com.hk.domain.order.ShippingOrder;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.taglibs.Functions;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Ankit Chhabra
 * Date: 9/18/13
 * Time: 6:58 PM
 */
public class MasterResolutionAction extends BaseAction {

  public Resolution pre() {
    return new ForwardResolution("/pages/admin/crm/crmMasterControl.jsp");
  }
}
