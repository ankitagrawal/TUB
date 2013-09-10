package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.hkDelivery.HubService;
import com.hk.domain.hkDelivery.HKReachPricingEngine;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.core.WarehouseService;
import com.hk.util.HKCollectionUtils;
import net.sourceforge.stripes.action.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Ankit Chhabra
 * Date: 8/27/13
 * Time: 12:17 PM
 */
public class CreateUpdateHKReachPricingEngineAction extends BaseAction {

  @Autowired
  WarehouseService warehouseService;

  @Autowired
  HubService hubService;

  @Autowired
  CourierService courierService;

  private HKReachPricingEngine hkReachPricingEngine;

  private List<Warehouse> onlineWarehouses;

  private  List<Hub>  hubs;

  private Warehouse warehouseParam;

  private Hub hubParam;

  private List<HKReachPricingEngine> hkReachEngines;

  Warehouse warehouse;

  Hub hub;

  @DefaultHandler
  public Resolution pre() {
    this.prepareEngineData();
    return new ForwardResolution("/pages/admin/createUpdateHKReachEngine.jsp");
  }

  public Resolution save() {
    HKReachPricingEngine duplicateEngine =
          (HKReachPricingEngine) HKCollectionUtils.findDuplicate(hkReachEngines,null,"warehouse","hub","validFrom");
    if (duplicateEngine != null ) {
      addRedirectAlertMessage(new SimpleMessage("You provided duplicate values for " + duplicateEngine.getWarehouse().getIdentifier() +
          " corresponding to " + duplicateEngine.getHub().getName() + " and valid from " +
          new SimpleDateFormat("yyyy-MM-dd").format(duplicateEngine.getValidFrom())));
    } else {
      for(HKReachPricingEngine hkReachPricingEngine : hkReachEngines) {
        if (hkReachPricingEngine.isSelected()) {
          hkReachPricingEngine.setUpdateTime(Calendar.getInstance().getTime());
          courierService.saveHKReachPricingEngine(hkReachPricingEngine);
        }
      }
      addRedirectAlertMessage(new SimpleMessage("Pricing info updated"));
    }
    prepareEngineData();
    return new RedirectResolution(CreateUpdateHKReachPricingEngineAction.class, "search")
        .addParameter("warehouseParam", warehouseParam).addParameter("hubParam", hubParam);
  }

  public Resolution add() {
    List<HKReachPricingEngine> localEngines= courierService.searchHKReachPricing(hkReachPricingEngine.getWarehouse(),
            hkReachPricingEngine.getHub());
    localEngines.add(hkReachPricingEngine);
    HKReachPricingEngine duplicateEngine =
        (HKReachPricingEngine) HKCollectionUtils.findDuplicate(localEngines,null,"warehouse","hub","validFrom");

    if(duplicateEngine != null) {
      addRedirectAlertMessage(new SimpleMessage("Entry already exists for " + duplicateEngine.getWarehouse().getIdentifier() +
          " corresponding to " + duplicateEngine.getHub().getName() + " and valid from " +
          new SimpleDateFormat("yyyy-MM-dd").format(duplicateEngine.getValidFrom())));
    } else {
      if(hkReachPricingEngine.getValidFrom() == null) {
        addRedirectAlertMessage(new SimpleMessage("Entry could not be saved. Please select a valid from date"));
      } else {
        hkReachPricingEngine.setUpdateTime(Calendar.getInstance().getTime());
        courierService.saveHKReachPricingEngine(hkReachPricingEngine);
        addRedirectAlertMessage(new SimpleMessage("Successfully added entry for " + hkReachPricingEngine.getWarehouse().getIdentifier() +
            " corresponding to " + hkReachPricingEngine.getHub().getName()));
      }
    }
    prepareEngineData();
    return new RedirectResolution(CreateUpdateHKReachPricingEngineAction.class, "search")
        .addParameter("warehouseParam", warehouseParam).addParameter("hubParam", hubParam);
  }

  public Resolution search() {
    hkReachEngines = courierService.searchHKReachPricing(warehouseParam, hubParam);
    if (hkReachEngines == null || hkReachEngines.isEmpty()) {
      addRedirectAlertMessage(new SimpleMessage("No results match your search."));
    }
    this.prepareEngineData();
    return new ForwardResolution("/pages/admin/createUpdateHKReachEngine.jsp");
  }

  private void prepareEngineData() {
    onlineWarehouses = warehouseService.getServiceableWarehouses();
    hubs = hubService.getAllHubs();

  }

  public HKReachPricingEngine getHkReachPricingEngine() {
    return hkReachPricingEngine;
  }

  public void setHkReachPricingEngine(HKReachPricingEngine hkReachPricingEngine) {
    this.hkReachPricingEngine = hkReachPricingEngine;
  }

  public List<Warehouse> getOnlineWarehouses() {
    return onlineWarehouses;
  }

  public void setOnlineWarehouses(List<Warehouse> onlineWarehouses) {
    this.onlineWarehouses = onlineWarehouses;
  }

  public List<Hub> getHubs() {
    return hubs;
  }

  public void setHubs(List<Hub> hubs) {
    this.hubs = hubs;
  }

  public Warehouse getWarehouseParam() {
    return warehouseParam;
  }

  public void setWarehouseParam(Warehouse warehouseParam) {
    this.warehouseParam = warehouseParam;
  }

  public Hub getHubParam() {
    return hubParam;
  }

  public void setHubParam(Hub hubParam) {
    this.hubParam = hubParam;
  }

  public List<HKReachPricingEngine> getHkReachEngines() {
    return hkReachEngines;
  }

  public void setHkReachEngines(List<HKReachPricingEngine> hkReachEngines) {
    this.hkReachEngines = hkReachEngines;
  }

  public Hub getHub() {
    return hub;
  }

  public void setHub(Hub hub) {
    this.hub = hub;
  }

  public Warehouse getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(Warehouse warehouse) {
    this.warehouse = warehouse;
  }
}
