package com.hk.admin.impl.dao.courier;

import com.hk.admin.pact.dao.courier.PincodeCourierMappingDao;
import com.hk.admin.pact.dao.shipment.ShipmentDao;
import com.hk.constants.shipment.EnumShipmentServiceType;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.PincodeCourierMapping;
import com.hk.domain.courier.PincodeDefaultCourier;
import com.hk.domain.courier.ShipmentServiceType;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: 12/10/12
 * Time: 1:42 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("unchecked")
@Repository
public class PincodeCourierMappingDaoImpl extends BaseDaoImpl implements PincodeCourierMappingDao {

    @Autowired
    ShipmentDao shipmentDao;

    @Override
    public List<PincodeCourierMapping> getApplicablePincodeCourierMapping(Pincode pincode, List<Courier> couriers, List<ShipmentServiceType> shipmentServiceTypes, Boolean activeCourier) {
        DetachedCriteria pincodeCourierMappingCriteria = DetachedCriteria.forClass(PincodeCourierMapping.class);

        if (pincode != null) {
            pincodeCourierMappingCriteria.add(Restrictions.eq("pincode", pincode));
        }
        if (couriers != null) {
            pincodeCourierMappingCriteria.add(Restrictions.in("courier", couriers));
        }
        if (activeCourier != null) {
            DetachedCriteria courierCriteria = pincodeCourierMappingCriteria.createCriteria("courier");
            courierCriteria.add(Restrictions.eq("active", activeCourier));
        }

        String lhsCondition = "";       //assuming we wont reach a state where more than two shipment service types are applicable
        String rhsCondition = "";

        if (shipmentServiceTypes != null) {
            int size = shipmentServiceTypes.size() - (shipmentServiceTypes.contains(null) ? 1 : 0);
            if (size > 1) {
                lhsCondition = shipmentServiceTypes.get(0).getName();
                rhsCondition = shipmentServiceTypes.get(1).getName();
                pincodeCourierMappingCriteria.add(Restrictions.or(Restrictions.eq(lhsCondition, true), Restrictions.eq(rhsCondition, true)));
            } else if (size > 0) {
                lhsCondition = shipmentServiceTypes.get(0).getName();
                pincodeCourierMappingCriteria.add(Restrictions.eq(lhsCondition, true));
            }
        }
        return findByCriteria(pincodeCourierMappingCriteria);
    }

    @Override
    public List<Courier> getApplicableCouriers(Pincode pincode, List<Courier> couriers, List<ShipmentServiceType> shipmentServiceTypes, Boolean activeCourier) {
        List<PincodeCourierMapping> pincodeCourierMapping = getApplicablePincodeCourierMapping(pincode, couriers, shipmentServiceTypes, activeCourier);
        List<Courier> courierList = new ArrayList<Courier>();
        if (pincodeCourierMapping != null && pincodeCourierMapping.size() > 0) {
            for (PincodeCourierMapping pincodeCourierMap : pincodeCourierMapping) {
                courierList.add(pincodeCourierMap.getCourier());
            }
        }
        return courierList;
    }

    @Override
    public List<Courier> getApplicableCouriers(Pincode pincode, boolean isCod, boolean isGround, Boolean activeCourier) {
        return getApplicableCouriers(pincode, null, Arrays.asList(getShipmentServiceType(isCod, isGround)), true);
    }

    @Override
    public List<ShipmentServiceType> getShipmentServiceType(Set<CartLineItem> productCartLineItems, boolean checkForCod) {
        boolean isGroundShipped = false;
        for (CartLineItem productCartLineItem : productCartLineItems) {
            if (productCartLineItem.getProductVariant().getProduct().isGroundShipping()) {
                isGroundShipped = true;
                break;
            }
        }
        if (isGroundShipped) {
            if (checkForCod) {
                return Arrays.asList(EnumShipmentServiceType.Cod_Ground.asShipmentServiceType());
            }
            return shipmentDao.getShipmentServiceTypes(EnumShipmentServiceType.getGroundEnumShipmentServiceTypes());
        } else {
            if (checkForCod) {
                return Arrays.asList(EnumShipmentServiceType.Cod_Air.asShipmentServiceType());
            }
            return shipmentDao.getShipmentServiceTypes(EnumShipmentServiceType.getAirEnumShipmentServiceTypes());
        }
    }

    @Override
    public ShipmentServiceType getShipmentServiceType(ShippingOrder shippingOrder) {
        boolean isGroundShipped = false;
        for (LineItem lineItem : shippingOrder.getLineItems()) {
            if (lineItem.getSku().getProductVariant().getProduct().isGroundShipping()) {
                isGroundShipped = true;
                break;
            }
        }
        return getShipmentServiceType(shippingOrder.isCOD(), isGroundShipped);
    }

    @Override
    public ShipmentServiceType getShipmentServiceType(boolean isCod, boolean isGround) {
        if (isCod) {
            if (isGround) {
                return EnumShipmentServiceType.Cod_Ground.asShipmentServiceType();
            }
            return EnumShipmentServiceType.Cod_Air.asShipmentServiceType();
        } else {
            if (isGround) {
                return EnumShipmentServiceType.Prepaid_Ground.asShipmentServiceType();
            }
            return EnumShipmentServiceType.Prepaid_Air.asShipmentServiceType();
        }
    }

    @Override
    public PincodeCourierMapping savePincodeCourierMapping(PincodeCourierMapping pincodeCourierMapping) {
        return (PincodeCourierMapping) save(pincodeCourierMapping);
    }

    public List<PincodeDefaultCourier> searchPincodeDefaultCourierList(Pincode pincode, Warehouse warehouse, Boolean isCod, Boolean isGroundshipping) {

        Criteria pincodeDefaultCourierCriteria = getSession().createCriteria(PincodeDefaultCourier.class);

        if (warehouse != null && StringUtils.isNotBlank(Long.toString(warehouse.getId()))) {
            pincodeDefaultCourierCriteria.add(Restrictions.eq("warehouse", warehouse));
        }
        if (pincode != null && StringUtils.isNotBlank(pincode.getPincode())) {
            pincodeDefaultCourierCriteria.add(Restrictions.eq("pincode", pincode));
        }
        if (isCod != null) {
            pincodeDefaultCourierCriteria.add(Restrictions.eq("cod", isCod));
        }
        if (isGroundshipping != null) {
            pincodeDefaultCourierCriteria.add(Restrictions.eq("groundShipping", isGroundshipping));
        }
        return pincodeDefaultCourierCriteria.list();
    }

    public PincodeDefaultCourier searchPincodeDefaultCourier(Pincode pincode, Warehouse warehouse, Boolean isCod, Boolean isGroundshipping) {
        List<PincodeDefaultCourier> pincodeDefaultCouriers = searchPincodeDefaultCourierList(pincode, warehouse, isCod, isGroundshipping);
        return pincodeDefaultCouriers.isEmpty() ? null : pincodeDefaultCouriers.get(0);
    }

    public List<Courier> searchDefaultCourier(Pincode pincode, Boolean isCOD, Boolean isGroundShipping, Warehouse warehouse) {
        List<PincodeDefaultCourier> pincodeDefaultCouriers = searchPincodeDefaultCourierList(pincode, warehouse, isCOD, isGroundShipping);

        List<Courier> courierList = new ArrayList<Courier>();
        if (pincodeDefaultCouriers != null && pincodeDefaultCouriers.size() > 0) {
            for (PincodeDefaultCourier pincodeDefaultCourier : pincodeDefaultCouriers) {
                courierList.add(pincodeDefaultCourier.getCourier());
            }
        }
        return courierList;
    }

    public void deletePincodeCourierMapping(PincodeCourierMapping pincodeCourierMapping) {
        delete(pincodeCourierMapping);
    }
}
