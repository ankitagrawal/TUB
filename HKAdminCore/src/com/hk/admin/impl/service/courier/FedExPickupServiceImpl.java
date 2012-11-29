package com.hk.admin.impl.service.courier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.hk.constants.core.Keys;
import com.hk.admin.util.courier.thirdParty.FedExPickupServiceUtil;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Nov 29, 2012
 * Time: 1:19:38 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class FedExPickupServiceImpl {

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

	public void createPickupRequest(){
		FedExPickupServiceUtil fedExPickupServiceUtil = new FedExPickupServiceUtil(fedExAuthKey,fedExAccountNo,fedExMeterNo,fedExPassword,fedExServerUrl);
	}

}
