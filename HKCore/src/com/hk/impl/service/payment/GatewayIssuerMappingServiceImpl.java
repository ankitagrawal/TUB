package com.hk.impl.service.payment;

import com.hk.domain.payment.Gateway;
import com.hk.domain.payment.GatewayIssuerMapping;
import com.hk.domain.payment.Issuer;
import com.hk.pact.dao.payment.GatewayIssuerMappingDao;
import com.hk.pact.service.payment.GatewayIssuerMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: 11/21/12
 * Time: 5:35 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class GatewayIssuerMappingServiceImpl implements GatewayIssuerMappingService {

    @Autowired
    GatewayIssuerMappingDao gatewayIssuerMappingDao;


    @Override
    public List<Gateway> getGateways(boolean active) {
        return gatewayIssuerMappingDao.getGateways(active);
    }

    @Override
    public List<Issuer> getIssuerByType(String issuerType, boolean active) {
        return gatewayIssuerMappingDao.getIssuerByType(issuerType, active);
    }

    @Override
    public List<GatewayIssuerMapping> searchGatewayIssuerMapping(Issuer issuer, Gateway gateway, Boolean activeMapping) {
        return gatewayIssuerMappingDao.searchGatewayIssuerMapping(issuer, gateway, activeMapping);
    }

    @Override
    public GatewayIssuerMapping getGatewayIssuerMapping(Issuer issuer, Gateway gateway, Boolean activeMapping) {
        List<GatewayIssuerMapping> gatewayIssuerMappings =  searchGatewayIssuerMapping(issuer, gateway, activeMapping);
        return gatewayIssuerMappings != null && !gatewayIssuerMappings.isEmpty() ? gatewayIssuerMappings.get(0) : null;
    }

    @Override
    public String getImageOfIssuer(byte[] imageByteArray, String imageName) {
         return gatewayIssuerMappingDao.getImageOfIssuer(imageByteArray, imageName);
    }
}
