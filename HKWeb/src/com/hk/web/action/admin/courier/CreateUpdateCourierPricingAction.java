package com.hk.web.action.admin.courier;

import com.hk.admin.pact.service.courier.CourierPricingEngineService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierPricingEngine;
import com.hk.domain.courier.RegionType;

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
  CourierPricingEngineService courierPricingEngineService;

  private Courier courier;
  private RegionType regionType;
  private List<CourierPricingEngine> courierPricingEngineList = new ArrayList<CourierPricingEngine>();
  private List<Courier> courierList = new ArrayList<Courier>();
  private List<RegionType> regionTypeList = new ArrayList<RegionType>();

  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/createUpdatecourierPricing.jsp");
  }

  private void initialize() {
    courierList = courierPricingEngineService.getAvailableCouriers();
    regionTypeList = courierPricingEngineService.getRegionTypeList();
  }

  public Resolution search() {
    if(courier == null) {
      addRedirectAlertMessage(new SimpleMessage("Please select the Courier"));
    }
    courierPricingEngineList = courierPricingEngineService.getCourierPricingInfoByCourier(courier);
    if(courier != null && courierPricingEngineList.size() == 0) {
      addRedirectAlertMessage(new SimpleMessage("No data exist for your selected choice"));
    }
    this.initialize();
    return new ForwardResolution("/pages/admin/createUpdatecourierPricing.jsp");
  }

  public Resolution save() {
    for(CourierPricingEngine courierPricingEngine : courierPricingEngineList) {
      if(courierPricingEngine.getId() != null) {
        CourierPricingEngine localEngine = courierPricingEngineService.getCourierPricingInfoById(courierPricingEngine.getId());
        if (localEngine != null) {
          localEngine.setCourier(courierPricingEngine.getCourier());
          localEngine.setRegionType(courierPricingEngine.getRegionType());
          localEngine.setFirstBaseWt(courierPricingEngine.getFirstBaseWt());
          localEngine.setFirstBaseCost(courierPricingEngine.getFirstBaseCost());
          localEngine.setSecondBaseCost(courierPricingEngine.getSecondBaseCost());
          localEngine.setSecondBaseWt(courierPricingEngine.getSecondBaseWt());
          localEngine.setAdditionalCost(courierPricingEngine.getAdditionalCost());
          localEngine.setFuelSurcharge(courierPricingEngine.getFuelSurcharge());
          localEngine.setMinCodCharges(courierPricingEngine.getMinCodCharges());
          localEngine.setCodCutoffAmount(courierPricingEngine.getCodCutoffAmount());
          localEngine.setVariableCodCharges(courierPricingEngine.getVariableCodCharges());
          localEngine.setValidUpto(courierPricingEngine.getValidUpto());
          courierPricingEngineService.saveCourierPricingInfo(localEngine);
        }
      } else {
        courierPricingEngineService.saveCourierPricingInfo(courierPricingEngine);
      }
    }
    addRedirectAlertMessage(new SimpleMessage("Courier Info saved"));
    initialize();
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
