package com.hk.pact.service.payment;

import com.hk.domain.payment.Gateway;
import com.hk.domain.payment.GatewayIssuerMapping;
import com.hk.domain.payment.Issuer;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 11/21/12
 * Time: 5:35 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GatewayIssuerMappingService {

    public List<GatewayIssuerMapping> searchGatewayIssuerMapping(Gateway gateway, Issuer issuer, Boolean activeMapping, Boolean activeGateway, Boolean activeIssuer, String issuerType, String orderUpon, String orderBy);

    public Map<Gateway, Double> getGatewayHitRatio(Issuer issuer, Boolean activeMapping, Boolean activeGateway);


}
