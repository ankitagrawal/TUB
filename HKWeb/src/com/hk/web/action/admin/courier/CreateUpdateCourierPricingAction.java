package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.dao.courier.CourierPricingEngineDao;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierPricingEngine;
import com.hk.domain.courier.RegionType;
import com.hk.pact.dao.BaseDao;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Seema
 * Date: Jun 8, 2012
 * Time: 1:35:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class CreateUpdateCourierPricingAction extends BaseAction {
  @Autowired
  CourierPricingEngineDao courierPricingEngineDao;
  @Autowired
  BaseDao baseDao;
  private CourierPricingEngine courierPricingEngine;

  private Courier courier;
  private RegionType regionType;

  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/createUpdatecourierPricing.jsp");
  }

  public Resolution getCourierPricing(){
   courierPricingEngine = courierPricingEngineDao.getCourierPricingInfo(courier,regionType,null);
   return new ForwardResolution("/pages/admin/createUpdatecourierPricing.jsp");
  }

  public Resolution save() {
    courierPricingEngineDao.save(courierPricingEngine);
    addRedirectAlertMessage(new SimpleMessage("courier info saved"));
    return new ForwardResolution("/pages/admin/createUpdatecourierPricing.jsp");
  }


  public CourierPricingEngine getCourierPricingEngine() {
    return courierPricingEngine;
  }

  public void setCourierPricingEngine(CourierPricingEngine courierPricingEngine) {
    this.courierPricingEngine = courierPricingEngine;
  }

  public RegionType getRegionType() {
    return regionType;
  }

  public void setRegionType(RegionType regionType) {
    this.regionType = regionType;
  }

  public Courier getCourier() {
    return courier;
  }

  public void setCourier(Courier courier) {
    this.courier = courier;
  }
}
