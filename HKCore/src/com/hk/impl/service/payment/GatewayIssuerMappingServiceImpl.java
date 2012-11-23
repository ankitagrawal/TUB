package com.hk.impl.service.payment;

import com.hk.domain.payment.Gateway;
import com.hk.domain.payment.GatewayIssuerMapping;
import com.hk.domain.payment.Issuer;
import com.hk.pact.dao.payment.GatewayIssuerMappingDao;
import com.hk.pact.service.payment.GatewayIssuerMappingService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: 11/21/12
 * Time: 5:35 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class GatewayIssuerMappingServiceImpl implements GatewayIssuerMappingService {

    GatewayIssuerMappingDao gatewayIssuerMappingDao;

    @Override
    public List<GatewayIssuerMapping> searchGatewayIssuerMapping(Gateway gateway, Issuer issuer, Boolean activeMapping, Boolean activeGateway, Boolean activeIssuer, String issuerType, String orderUpon, String orderBy) {
        return gatewayIssuerMappingDao.searchGatewayIssuerMapping(gateway, issuer, activeMapping, activeGateway, activeIssuer, issuerType, orderUpon, orderBy);
    }

    @Override
    public Map<Gateway, Double> getGatewayHitRatio(Issuer issuer, Boolean activeMapping, Boolean activeGateway) {
        List<GatewayIssuerMapping> gatewayIssuerMappings = gatewayIssuerMappingDao.searchGatewayByIssuer(issuer, activeMapping, activeGateway);

        Map<Gateway, Double> gatewayHitRatioMap = new HashMap<Gateway, Double>();

        Double baseTotal = 0D;

        for (GatewayIssuerMapping gatewayIssuerMapping : gatewayIssuerMappings) {
            baseTotal += gatewayIssuerMapping.getPriority();
        }

        if (baseTotal == 0D) return null;

        for (GatewayIssuerMapping gatewayIssuerMapping : gatewayIssuerMappings) {
            gatewayHitRatioMap.put(gatewayIssuerMapping.getGateway(), (gatewayIssuerMapping.getPriority() / baseTotal));
        }

        gatewayHitRatioMap.put(null,baseTotal);


        return gatewayHitRatioMap;
    }
}
