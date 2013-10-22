package com.hk.api.edge.constants;

import com.hk.api.edge.integration.pact.service.LoadPropertyService;
import com.hk.service.ServiceLocatorFactory;

public class EnvConstants {


    public static final String API_SERVER_URL           = "API_SERVER_URL";

    public static final String DEV_ENVIRONMENT          = "dev";


    public static boolean isDevEnv() {
        LoadPropertyService loadPropertyService = ServiceLocatorFactory.getService(LoadPropertyService.class);
        String env = (String) loadPropertyService.getProperty("project.env");
        return (env.equals(DEV_ENVIRONMENT));
    }

}
