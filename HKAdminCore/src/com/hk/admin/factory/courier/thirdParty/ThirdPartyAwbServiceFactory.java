package com.hk.admin.factory.courier.thirdParty;

import com.hk.admin.pact.service.courier.thirdParty.ThirdPartyAwbService;
import com.hk.constants.courier.EnumCourier;
import com.hk.service.ServiceLocatorFactory;

public class ThirdPartyAwbServiceFactory {
    
    public static ThirdPartyAwbService getThirdPartyAwbService(Long courierId){
        
        
        if(courierId.equals(EnumCourier.FedEx.getId())){
            //TODO:#vaibhav refactor this hard cording string is bad way of using service locator
            return (ThirdPartyAwbService) ServiceLocatorFactory.getService("FedExAwbServiceImpl");
        }
        
        return null;
    }

}
