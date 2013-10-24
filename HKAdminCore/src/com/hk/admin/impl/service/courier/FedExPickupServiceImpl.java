package com.hk.admin.impl.service.courier;

import com.hk.constants.warehouse.EnumWarehouseIdentifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.hk.constants.core.Keys;
import com.hk.admin.util.courier.thirdParty.FedExPickup;
import com.hk.admin.pact.service.courier.thirdParty.ThirdPartyPickupService;
import com.hk.domain.order.ShippingOrder;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Nov 29, 2012
 * Time: 1:19:38 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class FedExPickupServiceImpl implements ThirdPartyPickupService {

  @Value("#{hkEnvProps['" + Keys.Env.fedExServerUrl + "']}")
  private String                fedExServerUrl;

  @Value("#{hkEnvProps['" + Keys.Env.fedExAuthKeyGGN + "']}")
  private String fedExAuthKeyGGN;

  @Value("#{hkEnvProps['" + Keys.Env.fedExMeterNoGGN + "']}")
  private String fedExMeterNoGGN;

  @Value("#{hkEnvProps['" + Keys.Env.fedExPasswordGGN + "']}")
  private String fedExPasswordGGN;

  @Value("#{hkEnvProps['" + Keys.Env.fedExAccountNoGGN + "']}")
  private String fedExAccountNoGGN;

  @Value("#{hkEnvProps['" + Keys.Env.fedExAuthKeyMUM + "']}")
  private String fedExAuthKeyMUM;

  @Value("#{hkEnvProps['" + Keys.Env.fedExMeterNoMUM + "']}")
  private String fedExMeterNoMUM;

  @Value("#{hkEnvProps['" + Keys.Env.fedExPasswordMUM + "']}")
  private String fedExPasswordMUM;

  @Value("#{hkEnvProps['" + Keys.Env.fedExAccountNoMUM + "']}")
  private String fedExAccountNoMUM;

  @Override
  public List<String> createPickupRequest(ShippingOrder shippingOrder, Date date){
    FedExPickup fedExPickup = null;
    if (EnumWarehouseIdentifier.GGN_Bright_Warehouse.getName().equals(shippingOrder.getWarehouse().getIdentifier())) {
      fedExPickup = new FedExPickup(fedExAuthKeyGGN, fedExAccountNoGGN,fedExMeterNoGGN, fedExPasswordGGN,fedExServerUrl);
    } else {
      fedExPickup = new FedExPickup(fedExAuthKeyMUM, fedExAccountNoMUM,fedExMeterNoMUM,fedExPasswordMUM,fedExServerUrl);
    }
    return fedExPickup.createPickupRequest(shippingOrder, date);
  }

}
