package com.hk.impl.dao.payment;

import com.hk.domain.payment.Gateway;
import com.hk.domain.payment.GatewayIssuerMapping;
import com.hk.domain.payment.Issuer;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.payment.GatewayIssuerMappingDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 11/21/12
 * Time: 2:57 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class GatewayIssuerMappingDaoImpl extends BaseDaoImpl implements GatewayIssuerMappingDao {

    @Override
    public List<GatewayIssuerMapping> searchGatewayIssuerMapping(Gateway gateway, Issuer issuer, Boolean activeMapping, Boolean activeGateway, Boolean activeIssuer, String issuerType, String orderUpon, String orderBy) {
        DetachedCriteria gatewayIssuerMappingCriteria = DetachedCriteria.forClass(GatewayIssuerMapping.class);
        if (gateway != null) {
            gatewayIssuerMappingCriteria.add(Restrictions.eq("gateway", gateway));
        }
        if (issuer != null) {
            gatewayIssuerMappingCriteria.add(Restrictions.eq("issuer", issuer));
        }
        if (activeMapping != null) {
            gatewayIssuerMappingCriteria.add(Restrictions.eq("active", activeMapping));
        }
        if (activeIssuer != null) {
            gatewayIssuerMappingCriteria.add(Restrictions.eq("issuer.active", activeIssuer));
        }
        if (activeGateway != null) {
            gatewayIssuerMappingCriteria.add(Restrictions.eq("gateway.active", activeGateway));
        }
        if(issuerType != null){
            gatewayIssuerMappingCriteria.add(Restrictions.eq("issuer.issuerType", issuerType));
        }

        gatewayIssuerMappingCriteria.setProjection(Projections.groupProperty("issuer"));

        //crude way, think of a better way --ps
        if (orderBy != null) {
            if (orderBy.equals("desc")) {
                gatewayIssuerMappingCriteria.addOrder(Order.desc(orderUpon));
            } else {
                gatewayIssuerMappingCriteria.addOrder(Order.asc(orderUpon));
            }
        }

        return findByCriteria(gatewayIssuerMappingCriteria);
    }

    @Override
    public List<GatewayIssuerMapping> searchGatewayByIssuer(Issuer issuer, Boolean activeMapping, Boolean activeGateway) {

        DetachedCriteria gatewayIssuerMappingCriteria = DetachedCriteria.forClass(GatewayIssuerMapping.class);
        if (activeGateway != null) {
            gatewayIssuerMappingCriteria.add(Restrictions.eq("gateway.active", activeGateway));
        }
        if (issuer != null) {
            gatewayIssuerMappingCriteria.add(Restrictions.eq("issuer", issuer));
        }
        if (activeMapping != null) {
            gatewayIssuerMappingCriteria.add(Restrictions.eq("active", activeMapping));
        }

        gatewayIssuerMappingCriteria.setProjection(Projections.groupProperty("gateway"));

        return findByCriteria(gatewayIssuerMappingCriteria);

    }
}
