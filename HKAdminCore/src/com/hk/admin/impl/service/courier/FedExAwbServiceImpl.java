package com.hk.admin.impl.service.courier;

import com.hk.admin.dto.courier.thirdParty.ThirdPartyAwbDetails;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.admin.pact.service.courier.thirdParty.ThirdPartyAwbService;
import com.hk.admin.util.FedExShipmentDeleteUtil;
import com.hk.admin.util.courier.thirdParty.FedExCourierUtil;
import com.hk.constants.core.Keys;
import com.hk.constants.warehouse.EnumWarehouseIdentifier;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.PincodeCourierMapping;
import com.hk.domain.order.ShippingOrder;
import com.hk.pact.service.core.PincodeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class FedExAwbServiceImpl implements ThirdPartyAwbService {

  @Value("#{hkEnvProps['" + Keys.Env.fedExAuthKey + "']}")
  private String                fedExAuthKey;

  @Value("#{hkEnvProps['" + Keys.Env.fedExAccountNoGGN + "']}")
  private String fedExAccountNoGGN;

  @Value("#{hkEnvProps['" + Keys.Env.fedExMeterNo + "']}")
  private String                fedExMeterNo;

  @Value("#{hkEnvProps['" + Keys.Env.fedExPassword + "']}")
  private String                fedExPassword;

  @Value("#{hkEnvProps['" + Keys.Env.fedExServerUrl + "']}")
  private String                fedExServerUrl;

  @Value("#{hkEnvProps['" + Keys.Env.fedExAccountNoMUM + "']}")
  private String fedExAccountNoMUM;

  @Autowired
  private PincodeCourierService pincodeCourierService;
  @Autowired
  private PincodeService pincodeService;

  @Override
  public ThirdPartyAwbDetails getThirdPartyAwbDetails(ShippingOrder shippingOrder, Double weightInKg) {
    String fedExAccount = null;
    if (EnumWarehouseIdentifier.GGN_Bright_Warehouse.getName().equals(shippingOrder.getWarehouse().getIdentifier())) {
      fedExAccount = fedExAccountNoGGN;
    } else {
      fedExAccount = fedExAccountNoMUM;
    }
    FedExCourierUtil fedExCourierUtil = new FedExCourierUtil(fedExAuthKey, fedExAccount, fedExMeterNo, fedExPassword, fedExServerUrl);
    return fedExCourierUtil.newFedExShipment(shippingOrder, weightInKg);
  }

  @Override
  public Awb syncHKAwbWithThirdPartyAwb(Awb hkAwb, ThirdPartyAwbDetails thirdPartyAwbDetails) {
    List<String> barcodeList = thirdPartyAwbDetails.getBarcodeList();
    String forwardBarCode = barcodeList.get(0);
    hkAwb.setAwbBarCode(forwardBarCode);

    if (thirdPartyAwbDetails.isCod()) {
      String codReturnBarCode = barcodeList.get(1);
      hkAwb.setReturnAwbBarCode(codReturnBarCode);
      String returnAwb = barcodeList.get(2);
      hkAwb.setReturnAwbNumber(returnAwb);

    }

    return hkAwb;
  }

  @Override
  public boolean syncHKCourierServiceInfo(Courier courier, ThirdPartyAwbDetails thirdPartyAwbDetails) {
    // logic to update routing codes in hk, in case there is a change from fedex API.
    Pincode pincode = pincodeService.getByPincode(thirdPartyAwbDetails.getPincode());
    PincodeCourierMapping pincodeCourierMapping = pincodeCourierService.getApplicablePincodeCourierMapping(pincode, Arrays.asList(courier), null, null);

    List<String> routingCode = thirdPartyAwbDetails.getRoutingCode();
    if (pincodeCourierMapping != null) {
      if (StringUtils.isNotBlank(routingCode.get(0))) {
        String routingCodeForPincodeFromFedEx = routingCode.get(0);
        String routingCodeForPincodeInHK = pincodeCourierMapping.getRoutingCode();

        if (StringUtils.isBlank(routingCodeForPincodeInHK)
            || (StringUtils.isNotBlank(routingCodeForPincodeInHK) && !routingCodeForPincodeInHK.equals(routingCodeForPincodeFromFedEx))) {
          pincodeCourierMapping.setRoutingCode(routingCodeForPincodeFromFedEx);
          pincodeCourierService.savePincodeCourierMapping(pincodeCourierMapping);
        }
      }
    }
    return false;
  }

  @Override
  public boolean deleteThirdPartyAwb(String awbNumber){
    FedExShipmentDeleteUtil fedExShipmentDeleteUtil = new FedExShipmentDeleteUtil(fedExAuthKey, fedExAccountNoGGN,
        fedExMeterNo, fedExPassword, fedExServerUrl);
    return fedExShipmentDeleteUtil.deleteShipment(awbNumber);
  }
	/*
	@Override
	public String trackFedExShipment(String trackingId){
		// Tracking can only be done for real fedex shipments 
		FedExTrackShipmentUtil fedExTrack = new FedExTrackShipmentUtil();
		return fedExTrack.trackFedExShipment(trackingId);
	}
    */
}
