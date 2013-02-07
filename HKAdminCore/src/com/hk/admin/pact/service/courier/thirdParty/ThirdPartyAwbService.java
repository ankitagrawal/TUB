package com.hk.admin.pact.service.courier.thirdParty;

import java.util.Arrays;
import java.util.List;

import com.hk.admin.dto.courier.thirdParty.ThirdPartyAwbDetails;
import com.hk.constants.courier.EnumCourier;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.order.ShippingOrder;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public interface ThirdPartyAwbService {
    
    public List<Long> integratedCouriers = Arrays.asList(new Long[]{EnumCourier.FedEx.getId(),EnumCourier.FedEx_Surface.getId()});  
    
    /**
     * Would like to call it in async fashion 
     * @param shippingOrder
     * @param weightInKg
     * @return
     */
    public ThirdPartyAwbDetails getThirdPartyAwbDetails(ShippingOrder shippingOrder,Double weightInKg );
    
    public Awb syncHKAwbWithThirdPartyAwb(Awb hkAwb, ThirdPartyAwbDetails thirdPartyAwbDetails);
    
    /**
     * Would like to call it in async fashion
     *
     * @param courier
     * @param thirdPartyAwbDetails
     * @return
     */
    public boolean syncHKCourierServiceInfo(Courier courier, ThirdPartyAwbDetails thirdPartyAwbDetails);

    public boolean deleteThirdPartyAwb(String awbNumber);

	//public String trackFedExShipment(String trackingId);

}
