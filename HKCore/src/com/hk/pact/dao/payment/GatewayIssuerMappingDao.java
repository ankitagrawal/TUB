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

    
   public List<GatewayIssuerMapping> searchGatewayIssuerMapping(Gateway gateway, Issuer issuer, Boolean activeMapping, Boolean activeGateway, Boolean activeIssuer, String issuerType, String orderUpon, String orderBy);


   public List<GatewayIssuerMapping> searchGatewayByIssuer(Issuer issuer, Boolean activeMapping, Boolean activeGateway);

}
