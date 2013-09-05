package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierPricingEngine;
import com.hk.domain.courier.RegionType;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Seema
 * Date: Jun 8, 2012
 * Time: 1:35:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class CreateUpdateCourierPricingAction extends BaseAction {

  @Autowired
  CourierService courierService;

  private Courier courier;
  private RegionType regionType;
  private List<CourierPricingEngine> courierPricingEngineList = new ArrayList<CourierPricingEngine>();
  private List<Courier> courierList = new ArrayList<Courier>();
  private List<RegionType> regionTypeList = new ArrayList<RegionType>();
  private Set<RegionType> regionTypesForCourier= new HashSet<RegionType>();

  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/createUpdatecourierPricing.jsp");
  }

  private void initialize() {
    courierList = courierService.getAllActiveCourier();
    regionTypeList = courierService.getRegionTypeList();
  }

  public Resolution search() {
    if(courier == null) {
      addRedirectAlertMessage(new SimpleMessage("Please select the Courier"));
    }
    courierPricingEngineList = courierService.getCourierPricingInfoByCourier(courier);
    if(courier != null && courierPricingEngineList.size() == 0) {
      addRedirectAlertMessage(new SimpleMessage("No data exist for your selected choice"));
    }
    this.initialize();
    this.regionTypesForCourier.addAll(courierService.getRegionsForCourier(courier));
    return new ForwardResolution("/pages/admin/createUpdatecourierPricing.jsp");
  }

  public Resolution save() {
    CourierPricingEngine duplicatePricingEngine = findDuplicateCourierPricingEngine(courierPricingEngineList);
    if (duplicatePricingEngine == null) {
      courierService.saveUpdateCourierPricingInfo(courierPricingEngineList);
      addRedirectAlertMessage(new SimpleMessage("Courier Info saved"));
    } else {
      addRedirectAlertMessage(new SimpleError("You are trying to save duplicate entry for courier "
          + duplicatePricingEngine.getCourier().getName() + " and region " + duplicatePricingEngine.getRegionType().getName()));
    }
    initialize();
    return new ForwardResolution(CreateUpdateCourierPricingAction.class, "search");
  }

  private CourierPricingEngine findDuplicateCourierPricingEngine(List<CourierPricingEngine> courierPricingEngineList) {
    Map<String, CourierPricingEngine> cpeMap = new HashMap<String, CourierPricingEngine>();
    StringBuilder key;
    String keySeparator = "#";
    for (CourierPricingEngine currentEngine: courierPricingEngineList) {
      key = new StringBuilder();
      key.append(currentEngine.getCourier().getName()).append(keySeparator);
      key.append(currentEngine.getRegionType().getName()).append(keySeparator);
      key.append(currentEngine.getValidUpto().toString());

      if (!cpeMap.containsKey(key.toString())) {
        cpeMap.put(key.toString(),currentEngine);
      } else {
        return currentEngine;
      }
    }
    return null;
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

  public Set<RegionType> getRegionTypesForCourier() {
    return regionTypesForCourier;
  }

  public void setRegionTypesForCourier(Set<RegionType> regionTypesForCourier) {
    this.regionTypesForCourier = regionTypesForCourier;
  }
}
