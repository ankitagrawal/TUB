package com.hk.constants.store;

import com.hk.constants.core.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 9/4/12
 * Time: 12:08 AM
 */
@Component
public class StoreConstants {
    private static Long storeId;
    public final static Long HK_STORE_ID=1L;
    public final static Long MIH_STORE_ID=2L;

    @Value("#{hkEnvProps['" + Keys.Env.storeId + "']}")
    private String        storeIdString;

    @PostConstruct
    public void postConstruction() {

        storeId = new Long(storeIdString);

    }

    public static Long getStoreId(){
        return storeId;
    }

}
