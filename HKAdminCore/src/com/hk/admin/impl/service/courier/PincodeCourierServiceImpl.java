package com.hk.admin.impl.service.courier;

import com.hk.admin.pact.dao.courier.PincodeCourierMappingDao;
import com.hk.admin.pact.dao.shipment.ShipmentDao;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.shipment.EnumShipmentServiceType;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.*;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.core.PincodeService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.util.HKDateUtil;
import com.hk.util.ShipmentServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: 12/11/12
 * Time: 12:09 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("NullableProblems")
@Service
public class PincodeCourierServiceImpl implements PincodeCourierService {

    @Autowired
    PincodeService pincodeService;

    @Autowired
    PincodeCourierMappingDao pincodeCourierMappingDao;

    @Autowired
    ShipmentDao shipmentDao;

    @Autowired
    ShipmentService shipmentService;


    @Override
    public boolean isCodAllowed(String pin) {
        Pincode pincode = pincodeService.getByPincode(pin);
        if (pincode == null) {
            return false;
        } else {
            List<ShipmentServiceType> shipmentServiceTypes = shipmentDao.getShipmentServiceTypes(EnumShipmentServiceType.getCodEnumShipmentServiceTypes());
            return isCourierAvailable(pincode, null, shipmentServiceTypes, true);
        }
    }

    @Override
    public boolean isGroundShippingAllowed(String pin) {
        Pincode pincode = pincodeService.getByPincode(pin);
        if (pincode == null) {
            return false;
        } else {
            List<ShipmentServiceType> shipmentServiceTypes = shipmentDao.getShipmentServiceTypes(EnumShipmentServiceType.getGroundEnumShipmentServiceTypes());
            return isCourierAvailable(pincode, null, shipmentServiceTypes, true);
        }
    }

    @Override
    public boolean isCodAllowedOnGroundShipping(String pin) {
        Pincode pincode = pincodeService.getByPincode(pin);
        return pincode != null && isCourierAvailable(pincode, null, Arrays.asList(EnumShipmentServiceType.Cod_Ground.asShipmentServiceType()), true);
    }

    @Override
    public boolean isCourierAvailable(Pincode pincode, List<Courier> couriers, List<ShipmentServiceType> shipmentServiceTypes, Boolean activeCourier) {
        Long startTimeCourierAvaialble = System.currentTimeMillis();
        List<Courier> courierList = pincodeCourierMappingDao.getApplicableCouriers(pincode, couriers, shipmentServiceTypes, activeCourier);
        Long endTimeCourierAvaialble = System.currentTimeMillis();
        System.out.println("courierAvailable elapsed time is : " + (endTimeCourierAvaialble - startTimeCourierAvaialble));
        return courierList != null && !courierList.isEmpty();
    }

    @Override
    public List<ShipmentServiceType> getShipmentServiceType(Set<CartLineItem> productCartLineItems, boolean checkForCod) {
        return pincodeCourierMappingDao.getShipmentServiceType(productCartLineItems, checkForCod);
    }

    @Override
    public List<ShipmentServiceType> getShipmentServiceTypes(List<EnumShipmentServiceType> enumShipmentServiceTypes) {
        return shipmentDao.getShipmentServiceTypes(enumShipmentServiceTypes);
    }

    @Override
    public List<Courier> getAvailableCouriers(Order order) {
        Set<CartLineItem> cartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
        boolean checkForCod = false;
        if (!EnumOrderStatus.InCart.getId().equals(order.getOrderStatus().getId())) {
            checkForCod = order.isCOD();
        }
        List<ShipmentServiceType> applicableShipmentServiceType = getShipmentServiceType(cartLineItems, checkForCod);
        Pincode pincode = order.getAddress().getPincode();
        if (pincode == null) {
            return null;
        }
        return pincodeCourierMappingDao.getApplicableCouriers(pincode, null, applicableShipmentServiceType, true);
    }

    @Override
    public List<Courier> getApplicableCouriers(ShippingOrder shippingOrder) {
        Pincode pincode = shippingOrder.getBaseOrder().getAddress().getPincode();
        return pincodeCourierMappingDao.getApplicableCouriers(pincode, null, Arrays.asList(getShipmentServiceType(shippingOrder)), true);
    }

    @Override
    public ShippingOrder setTargetDeliveryDate(ShippingOrder shippingOrder) {
        ShipmentServiceType shipmentServiceType = getShipmentServiceType(shippingOrder);
        Pincode pincode = shippingOrder.getBaseOrder().getAddress().getPincode();
        PincodeDefaultCourier pincodeDefaultCourier = getPincodeDefaultCourier(pincode, shippingOrder.getWarehouse(), ShipmentServiceMapper.isCod(shipmentServiceType), ShipmentServiceMapper.isGround(shipmentServiceType));
        Integer estimatedDeliveryDays = pincodeDefaultCourier != null ? pincodeDefaultCourier.getEstimatedDeliveryDays() : 3;
        shippingOrder.setTargetDelDate(HKDateUtil.addToDate(new Date(), Calendar.DAY_OF_MONTH, estimatedDeliveryDays));
        return shippingOrder;
    }

    @Override
    public ShipmentServiceType getShipmentServiceType(ShippingOrder shippingOrder) {
        Shipment shipment = shippingOrder.getShipment();
        ShipmentServiceType shipmentServiceType = null;
        if (shipment != null) {
            shipmentServiceType = shipment.getShipmentServiceType();
        }
        if (shipmentServiceType != null) {
            return shipmentServiceType;
        } else {
            return pincodeCourierMappingDao.getShipmentServiceType(shippingOrder);
        }
    }

    /*@Override
    public List<Courier> getApplicableCouriers(Pincode pincode, List<Courier> couriers, ShipmentServiceType shipmentServiceType, Boolean activeCourier) {
        return pincodeCourierMappingDao.getApplicableCouriers(pincode, couriers, Arrays.asList(shipmentServiceType), activeCourier);
    }*/

    @Override
    public List<Courier> getApplicableCouriers(Pincode pincode, List<Courier> couriers, List<ShipmentServiceType> shipmentServiceTypes, Boolean activeCourier) {
        return pincodeCourierMappingDao.getApplicableCouriers(pincode, couriers, shipmentServiceTypes, activeCourier);
    }

    @Override
    public List<PincodeCourierMapping> getApplicablePincodeCourierMappingList(Pincode pincode, List<Courier> couriers, ShipmentServiceType shipmentServiceType, Boolean activeCourier) {
        return pincodeCourierMappingDao.getApplicablePincodeCourierMapping(pincode, couriers, Arrays.asList(shipmentServiceType), activeCourier);
    }

    @Override
    public Map<String, Boolean> generateDetailedAnalysis(List<PincodeCourierMapping> pincodeCourierMappings) {
        Map<String, Boolean> applicableShipmentServices = new HashMap<String, Boolean>();
        for (PincodeCourierMapping pincodeCourierMapping : pincodeCourierMappings) {
            if (pincodeCourierMapping.isPrepaidAir()) {
                applicableShipmentServices.put("Prepaid-Air", pincodeCourierMapping.isPrepaidAir());
            }
            if (pincodeCourierMapping.isCodAir()) {
                applicableShipmentServices.put("Cod-Air", pincodeCourierMapping.isCodAir());
            }
            if (pincodeCourierMapping.isPrepaidGround()) {
                applicableShipmentServices.put("Prepaid-Ground", pincodeCourierMapping.isPrepaidGround());
            }
            if (pincodeCourierMapping.isCodGround()) {
                applicableShipmentServices.put("Cod-Ground", pincodeCourierMapping.isCodGround());
            }
        }
        return applicableShipmentServices;
    }

    @Override
    public List<Courier> getDefaultCouriers(Pincode pincode, Boolean isCOD, Boolean isGroundShipping, Warehouse warehouse) {
        return pincodeCourierMappingDao.searchDefaultCourier(pincode, isCOD, isGroundShipping, warehouse);
    }

    @Override
    public Courier getDefaultCourier(ShippingOrder shippingOrder) {
        ShipmentServiceType shipmentServiceType = getShipmentServiceType(shippingOrder);
        boolean isCod = ShipmentServiceMapper.isCod(shipmentServiceType);
        boolean isGround = ShipmentServiceMapper.isGround(shipmentServiceType);
        return getDefaultCourier(shippingOrder.getBaseOrder().getAddress().getPincode(), isCod, isGround, shippingOrder.getWarehouse());
    }

    @Override
    public PincodeDefaultCourier createPincodeDefaultCourier(Pincode pincode, Courier courier, Warehouse warehouse, boolean isGroundShippingAvailable, boolean isCODAvailable, Double estimatedShippingCost) {
        PincodeDefaultCourier pincodeDefaultCourier = new PincodeDefaultCourier();
        pincodeDefaultCourier.setPincode(pincode);
        pincodeDefaultCourier.setCourier(courier);
        pincodeDefaultCourier.setWarehouse(warehouse);
        pincodeDefaultCourier.setGroundShipping(isGroundShippingAvailable);
        pincodeDefaultCourier.setCod(isCODAvailable);
        pincodeDefaultCourier.setEstimatedShippingCost(estimatedShippingCost);
        return pincodeDefaultCourier;
    }

    @Override
    public Courier getDefaultCourier(Pincode pincode, boolean isCOD, boolean isGroundShipping, Warehouse warehouse) {
        List<Courier> couriers = pincodeCourierMappingDao.searchDefaultCourier(pincode, isCOD, isGroundShipping, warehouse);
        return couriers != null && !couriers.isEmpty() ? couriers.get(0) : null;
    }

    @Override
    public PincodeCourierMapping getApplicablePincodeCourierMapping(Pincode pincode, List<Courier> couriers, ShipmentServiceType shipmentServiceType, Boolean activeCourier) {
        List<PincodeCourierMapping> pincodeCourierMappings = getApplicablePincodeCourierMappingList(pincode, couriers, shipmentServiceType, activeCourier);
        return pincodeCourierMappings != null && !pincodeCourierMappings.isEmpty() ? pincodeCourierMappings.get(0) : null;
    }

    @Override
    public Courier getApplicableCourier(Pincode pincode, List<Courier> couriers, ShipmentServiceType shipmentServiceType, Boolean activeCourier) {
        List<Courier> courierList = pincodeCourierMappingDao.getApplicableCouriers(pincode, couriers, Arrays.asList(shipmentServiceType), activeCourier);
        return courierList != null && !courierList.isEmpty() ? courierList.get(0) : null;
    }

    @Override
    public PincodeCourierMapping createPincodeCourierMapping(Pincode pincode, Courier courier, boolean prepaidAir, boolean prepaidGround, boolean codAir, boolean codGround) {
        PincodeCourierMapping pincodeCourierMapping = new PincodeCourierMapping();
        pincodeCourierMapping.setPincode(pincode);
        pincodeCourierMapping.setCourier(courier);
        pincodeCourierMapping.setPrepaidAir(prepaidAir);
        pincodeCourierMapping.setPrepaidGround(prepaidGround);
        pincodeCourierMapping.setCodAir(codAir);
        pincodeCourierMapping.setCodGround(codGround);
        return pincodeCourierMapping;
    }

    @Override
    public PincodeCourierMapping savePincodeCourierMapping(PincodeCourierMapping pincodeCourierMapping) {
        return pincodeCourierMappingDao.savePincodeCourierMapping(pincodeCourierMapping);
    }

    @Override
    public void deletePincodeCourierMapping(PincodeCourierMapping pincodeCourierMapping) {
        pincodeCourierMappingDao.deletePincodeCourierMapping(pincodeCourierMapping);
    }

    @Override
    public boolean isDefaultCourierApplicable(Pincode pincode, Courier courier, boolean isGround, boolean isCod) {
        List<Courier> couriers = pincodeCourierMappingDao.getApplicableCouriers(pincode, isCod, isGround, true);
        return couriers != null && !couriers.isEmpty() && couriers.contains(courier) || EnumCourier.MIGRATE.getId().equals(courier.getId());
    }

    @Override
    public List<PincodeDefaultCourier> searchPincodeDefaultCourierList(Pincode pincode, Warehouse warehouse, Boolean isCod, Boolean isGround) {
        return pincodeCourierMappingDao.searchPincodeDefaultCourierList(pincode, warehouse, isCod, isGround);
    }

    @Override
    public PincodeDefaultCourier getPincodeDefaultCourier(Pincode pincode, Warehouse warehouse, Boolean isCod, Boolean isGround) {
        List<PincodeDefaultCourier> pincodeDefaultCouriers = searchPincodeDefaultCourierList(pincode, warehouse, isCod, isGround);
        return pincodeDefaultCouriers != null && !pincodeDefaultCouriers.isEmpty() ? pincodeDefaultCouriers.get(0) : null;
    }

    @Override
    public boolean changePincodeCourierMapping(PincodeCourierMapping pincodeCourierMappingDb, PincodeCourierMapping pincodeCourierMappingSoft) {
        if (pincodeCourierMappingDb.isPrepaidAir() && !pincodeCourierMappingSoft.isPrepaidAir()) {
            List<Courier> couriers = getDefaultCouriers(pincodeCourierMappingSoft.getPincode(), false, false, null);
            if (couriers != null && !couriers.isEmpty() && couriers.contains(pincodeCourierMappingSoft.getCourier())) {
                return false;
            }
        }
        if (pincodeCourierMappingDb.isPrepaidGround() && !pincodeCourierMappingSoft.isPrepaidGround()) {
            List<Courier> couriers = getDefaultCouriers(pincodeCourierMappingSoft.getPincode(), false, true, null);
            if (couriers != null && !couriers.isEmpty() && couriers.contains(pincodeCourierMappingSoft.getCourier())) {
                return false;
            }
        }
        if (pincodeCourierMappingDb.isCodAir() && !pincodeCourierMappingSoft.isCodAir()) {
            List<Courier> couriers = getDefaultCouriers(pincodeCourierMappingSoft.getPincode(), true, false, null);
            if (couriers != null && !couriers.isEmpty() && couriers.contains(pincodeCourierMappingSoft.getCourier())) {
                return false;
            }
        }
        if (pincodeCourierMappingDb.isCodGround() && !pincodeCourierMappingSoft.isCodGround()) {
            List<Courier> couriers = getDefaultCouriers(pincodeCourierMappingSoft.getPincode(), true, true, null);
            if (couriers != null && !couriers.isEmpty() && couriers.contains(pincodeCourierMappingSoft.getCourier())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<Courier> getApplicableCouriers(Pincode pincode, boolean isCod, boolean isGround, Boolean activeCourier) {
        return pincodeCourierMappingDao.getApplicableCouriers(pincode, isCod, isGround, true);
    }

    @Override
    public List<PincodeCourierMapping> getApplicablePincodeCourierMappingList(Pincode pincode, boolean isCod, boolean isGround, Boolean activeCourier) {
        return pincodeCourierMappingDao.getApplicablePincodeCourierMapping(pincode, null, Arrays.asList(pincodeCourierMappingDao.getShipmentServiceType(isCod, isGround)), activeCourier);
    }
}
