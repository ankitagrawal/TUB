package com.hk.admin.factory.courier.thirdParty;

import com.hk.admin.pact.service.courier.thirdParty.ThirdPartyAwbService;
import com.hk.admin.pact.service.courier.thirdParty.ThirdPartyPickupService;
import com.hk.constants.courier.EnumCourier;
import com.hk.service.ServiceLocatorFactory;

public class ThirdPartyCourierServiceFactory {
    
    public static ThirdPartyAwbService getThirdPartyAwbService(Long courierId){
        
        
        if(courierId.equals(EnumCourier.FedEx.getId()) || courierId.equals(EnumCourier.FedEx_Surface.getId())){
            //TODO:#vaibhav refactor this hard cording string is bad way of using service locator
            return (ThirdPartyAwbService) ServiceLocatorFactory.getService("FedExAwbServiceImpl");
        }
        
        return null;
    }

	 public static ThirdPartyPickupService getThirdPartyPickupService(Long courierId){


        if(courierId.equals(EnumCourier.FedEx.getId()) || courierId.equals(EnumCourier.FedEx_Surface.getId())){
            return (ThirdPartyPickupService) ServiceLocatorFactory.getService("FedExPickupServiceImpl");
        }

        return null;
    }


}
