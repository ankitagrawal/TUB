package com.hk.impl.dao.payment;

import com.hk.constants.core.Keys;
import com.hk.domain.payment.Gateway;
import com.hk.domain.payment.GatewayIssuerMapping;
import com.hk.domain.payment.Issuer;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.payment.GatewayIssuerMappingDao;
import com.hk.web.AppConstants;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: 11/21/12
 * Time: 2:57 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class GatewayIssuerMappingDaoImpl extends BaseDaoImpl implements GatewayIssuerMappingDao {

    @Override
    public List<Issuer> getIssuerByType(String issuerType, boolean active) {
        DetachedCriteria issuerCriteria = DetachedCriteria.forClass(Issuer.class);
        if (issuerType != null) {
            issuerCriteria.add(Restrictions.eq("issuerType", issuerType));
        }
        issuerCriteria.add(Restrictions.eq("active", active));
        issuerCriteria.addOrder(Order.asc("priority"));

        return findByCriteria(issuerCriteria);
    }

    @Override
    public List<Gateway> getGateways(boolean active) {
        DetachedCriteria gatewayCriteria = DetachedCriteria.forClass(Gateway.class);
        gatewayCriteria.add(Restrictions.eq("active", active));

        return findByCriteria(gatewayCriteria);
    }

    @Override
    public List<GatewayIssuerMapping> searchGatewayIssuerMapping(Issuer issuer, Gateway gateway, boolean activeMapping) {
        DetachedCriteria gatewayIssuerMappingCriteria = DetachedCriteria.forClass(GatewayIssuerMapping.class);
        if (issuer != null) {
            gatewayIssuerMappingCriteria.add(Restrictions.eq("issuer", issuer));
        }
        if (gateway != null) {
            gatewayIssuerMappingCriteria.add(Restrictions.eq("gateway", gateway));
        }
        gatewayIssuerMappingCriteria.add(Restrictions.eq("active", activeMapping));

        //remember this order by is pretty important, please be careful if any changes are made to this, since I am using a sorted priority list to find the random number generated
        gatewayIssuerMappingCriteria.addOrder(Order.asc("priority"));

        return findByCriteria(gatewayIssuerMappingCriteria);
    }

    public String getImageOfIssuer(byte[] imageByteArray, String imageName) {
        try{
            String imageIconRelativePath = "images\\gateway\\" + imageName + ".jpg";
            String imageIconAbsolutePath = AppConstants.appBasePath + imageIconRelativePath;
            FileOutputStream fos = new FileOutputStream(imageIconAbsolutePath);
            fos.write(imageByteArray);
            fos.close();
            return "\\"+ imageIconRelativePath;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
