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

  @Value("#{hkEnvProps['" + Keys.Env.fedExAuthKey + "']}")
  private String                fedExAuthKey;

  @Value("#{hkEnvProps['" + Keys.Env.fedExAccountNoGGN + "']}")
  private String fedExAccountNoGGN;

  @Value("#{hkEnvProps['" + Keys.Env.fedExAccountNoMUM + "']}")
  private String fedExAccountNoMUM;

  @Value("#{hkEnvProps['" + Keys.Env.fedExMeterNo + "']}")
  private String                fedExMeterNo;

  @Value("#{hkEnvProps['" + Keys.Env.fedExPassword + "']}")
  private String                fedExPassword;

  @Value("#{hkEnvProps['" + Keys.Env.fedExServerUrl + "']}")
  private String                fedExServerUrl;

  @Override
  public List<String> createPickupRequest(ShippingOrder shippingOrder, Date date){
    String fedExAccountNo = null;
    if (EnumWarehouseIdentifier.GGN_Bright_Warehouse.getName().equals(shippingOrder.getWarehouse().getIdentifier())) {
      fedExAccountNo = fedExAccountNoGGN;
    } else {
      fedExAccountNo = fedExAccountNoMUM;
    }
    FedExPickup fedExPickup = new FedExPickup(fedExAuthKey, fedExAccountNo,fedExMeterNo,fedExPassword,fedExServerUrl);
    return fedExPickup.createPickupRequest(shippingOrder, date);
  }

}
