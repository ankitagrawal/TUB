package com.hk.web.action.admin.courier;

import com.hk.pact.dao.MasterDataDao;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.dao.courier.CourierPricingEngineDao;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierPricingEngine;
import com.hk.domain.courier.RegionType;
import com.hk.pact.dao.BaseDao;

import java.util.ArrayList;
import java.util.List;

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
  @Autowired
  MasterDataDao masterDataDao;

  private Courier courier;
  private RegionType regionType;
  private List<CourierPricingEngine> courierPricingEngineList = new ArrayList<CourierPricingEngine>();
  private List<Courier> courierList = new ArrayList<Courier>();
  private List<RegionType> regionTypeList = new ArrayList<RegionType>();

  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/createUpdatecourierPricing.jsp");
  }

  private void param() {
    courierList = masterDataDao.getAvailableCouriers();
    regionTypeList = masterDataDao.getRegionTypeList();
  }

  public Resolution search() {
      if(courier == null) {
          addRedirectAlertMessage(new SimpleMessage("Please select the Courier"));
      }
      courierPricingEngineList = courierPricingEngineDao.getCourierPricingInfoByCourier(courier);
      if(courier != null && courierPricingEngineList.size() == 0) {
          addRedirectAlertMessage(new SimpleMessage("No data exist for your selected choice"));
      }
      param();
      return new ForwardResolution("/pages/admin/createUpdatecourierPricing.jsp");
  }

  public Resolution save() {
      for(CourierPricingEngine courierPricingEngine : courierPricingEngineList) {
          courierPricingEngineDao.save(courierPricingEngine);
      }
      addRedirectAlertMessage(new SimpleMessage("Courier Info saved"));
      param();
      return new ForwardResolution(CreateUpdateCourierPricingAction.class, "search");
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

  public List<CourierPricingEngine> getCourierPricingEngineList() {
    return courierPricingEngineList;
  }

  public void setCourierPricingEngineList(List<CourierPricingEngine> courierPricingEngineList) {
    this.courierPricingEngineList = courierPricingEngineList;
  }

  public List<Courier> getCourierList() {
    return courierList;
  }

  public void setCourierList(List<Courier> courierList) {
    this.courierList = courierList;
  }

  public List<RegionType> getRegionTypeList() {
    return regionTypeList;
  }

  public void setRegionTypeList(List<RegionType> regionTypeList) {
    this.regionTypeList = regionTypeList;
  }
}
