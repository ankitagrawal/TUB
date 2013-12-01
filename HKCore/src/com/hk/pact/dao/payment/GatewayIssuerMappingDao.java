package com.hk.pact.dao.payment;

import com.hk.domain.payment.Gateway;
import com.hk.domain.payment.GatewayIssuerMapping;
import com.hk.domain.payment.Issuer;
import com.hk.pact.dao.BaseDao;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: 11/21/12
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */

public interface GatewayIssuerMappingDao extends BaseDao {

    List<Gateway> getGateways(boolean active);

    List<Issuer> getIssuerByType(String issuerType, boolean active);

    List<GatewayIssuerMapping> searchGatewayIssuerMapping(Issuer issuer, Gateway gateway, Boolean activeMapping);

    String getImageOfIssuer(byte[] imageByteArray, String imageName);
}
