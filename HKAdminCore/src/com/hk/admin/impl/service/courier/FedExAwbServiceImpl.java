package com.hk.admin.impl.service.courier;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hk.admin.dto.courier.thirdParty.ThirdPartyAwbDetails;
import com.hk.admin.pact.dao.courier.CourierServiceInfoDao;
import com.hk.admin.pact.service.courier.thirdParty.ThirdPartyAwbService;
import com.hk.admin.util.courier.thirdParty.FedExCourierUtil;
import com.hk.admin.util.FedExShipmentDeleteUtil;
import com.hk.constants.core.Keys;
import com.hk.constants.courier.EnumCourier;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.order.ShippingOrder;

@Service
public class FedExAwbServiceImpl implements ThirdPartyAwbService {

    @Value("#{hkEnvProps['" + Keys.Env.fedExAuthKey + "']}")
    private String                fedExAuthKey;

    @Value("#{hkEnvProps['" + Keys.Env.fedExAccountNo + "']}")
    private String                fedExAccountNo;

    @Value("#{hkEnvProps['" + Keys.Env.fedExMeterNo + "']}")
    private String                fedExMeterNo;

    @Value("#{hkEnvProps['" + Keys.Env.fedExPassword + "']}")
    private String                fedExPassword;

    @Value("#{hkEnvProps['" + Keys.Env.fedExServerUrl + "']}")
    private String                fedExServerUrl;

    @Autowired
    private CourierServiceInfoDao courierServiceInfoDao;

    @Override
    public ThirdPartyAwbDetails getThirdPartyAwbDetails(ShippingOrder shippingOrder, Double weightInKg) {
        FedExCourierUtil fedExCourierUtil = new FedExCourierUtil(fedExAuthKey, fedExAccountNo, fedExMeterNo, fedExPassword, fedExServerUrl);
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
    public boolean syncHKCourierServiceInfo(ThirdPartyAwbDetails thirdPartyAwbDetails) {

        // logic to update routing codes in hk, in case there is a change from fedex API.
        CourierServiceInfo courierServiceInfo = courierServiceInfoDao.searchCourierServiceInfo(EnumCourier.FedEx.getId(), thirdPartyAwbDetails.getPincode(), false, false, false);

        List<String> routingCode = thirdPartyAwbDetails.getRoutingCode();
        if (courierServiceInfo != null) {
            if (StringUtils.isNotBlank(routingCode.get(0))) {
                String routingCodeForPincodeFromFedEx = routingCode.get(0);
                String routingCodeForPincodeInHK = courierServiceInfo.getRoutingCode();

                if (StringUtils.isBlank(routingCodeForPincodeInHK)
                        || (StringUtils.isNotBlank(routingCodeForPincodeInHK) && !routingCodeForPincodeInHK.equals(routingCodeForPincodeFromFedEx))) {
                    courierServiceInfo.setRoutingCode(routingCodeForPincodeFromFedEx);
                    getCourierServiceInfoDao().save(courierServiceInfo);
                }
            }
        }

        return false;
    }

    @Override
    public boolean deleteThirdPartyAwb(String awbNumber){
       FedExShipmentDeleteUtil fedExShipmentDeleteUtil = new FedExShipmentDeleteUtil(fedExAuthKey, fedExAccountNo, fedExMeterNo, fedExPassword, fedExServerUrl);
       return fedExShipmentDeleteUtil.deleteShipment(awbNumber);
    }

    public CourierServiceInfoDao getCourierServiceInfoDao() {
        return courierServiceInfoDao;
    }

}
