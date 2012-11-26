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

    // List<Gateway> get all valid gateways (no parameters)  --> select * from gateway where active = 1 order by priority asc;
    
    List<Gateway> getGateways(boolean active);

    //List<Issuer> get all valid issuers based on issuer type (issuer_type) --> select * from issuer where active = 1 and issuer_type = 'issuer_type' order by priority asc;

    List<Issuer> getIssuerByType(String issuerType, boolean active);

    //List<GatewayIssuerMapping> get all gateway issuer mappings by issuer or by gateway if needed (i.e visa ke saare gateways ki saari details, or SBI ke gateway

    List<GatewayIssuerMapping> searchGatewayIssuerMapping(Issuer issuer, Gateway gateway, boolean activeMapping);

//   public List<GatewayIssuerMapping> searchGatewayIssuerMapping(Gateway gateway, Issuer issuer, Boolean activeMapping, Boolean activeGateway, Boolean activeIssuer, String issuerType, String orderUpon, String orderBy);
//   public List<GatewayIssuerMapping> searchGatewayByIssuer(Issuer issuer, Boolean activeMapping, Boolean activeGateway);

}
