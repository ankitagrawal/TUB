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

    List<Gateway> getGateways(boolean active);

    List<Issuer> getIssuerByType(String issuerType, boolean active);

    List<GatewayIssuerMapping> searchGatewayIssuerMapping(Issuer issuer, Gateway gateway, Boolean activeMapping);

    GatewayIssuerMapping getGatewayIssuerMapping(Issuer issuer, Gateway gateway, Boolean activeMapping);

    String getImageOfIssuer(byte[] imageByteArray, String imageName);

}
