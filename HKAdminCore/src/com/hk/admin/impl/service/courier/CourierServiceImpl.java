package com.hk.admin.impl.service.courier;

import com.hk.admin.engine.ShipmentPricingEngine;
import com.hk.admin.pact.dao.courier.CityCourierTATDao;
import com.hk.admin.pact.dao.courier.CourierDao;
import com.hk.admin.pact.dao.courier.CourierServiceInfoDao;
import com.hk.admin.pact.service.courier.CourierCostCalculator;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.CityCourierTAT;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierPricingEngine;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.order.Order;
import com.hk.domain.warehouse.Warehouse;
import com.hk.dto.pricing.PricingDto;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.PincodeService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.payment.PaymentService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class CourierServiceImpl implements CourierService {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserService userService;
    @Autowired
    private PincodeService pincodeService;
    @Autowired
    private CourierDao courierDao;
    @Autowired
    private CourierServiceInfoDao courierServiceInfoDao;

     @Autowired
    WarehouseService  warehouseService;

    @Autowired
    PincodeDao pincodeDao;
      @Autowired
      CourierCostCalculator courierCostCalculator;

    @Autowired
    ShipmentPricingEngine       shipmentPricingEngine;


    @Override
    public Courier getCourierById(Long courierId) {
        List<Courier> courierList = getCourierDao().getCourierByIds(Arrays.asList(courierId));
        return courierList != null && courierList.size() > 0 ? courierList.get(0) : null;
    }

    @Override
    public Courier getCourierByName(String name) {
        return getCourierDao().getCourierByName(name);
    }

    public List<Courier> getAllCouriers() {
        return getCourierDao().getAllCouriers();
    }

    public CourierServiceInfo getCourierServiceByPincodeAndCourier(Long courierId, String pincode, Boolean isCod) {
        return getCourierServiceInfoDao().getCourierServiceByPincodeAndCourier(courierId, pincode, isCod);
    }

    // cod not available if either courier service not available there or order is exclusively service order -->
    // changing to if any service is there
    public boolean isCodAllowed(String pin) {
        if (StringUtils.isNotEmpty(pin)) {
            return getCourierServiceInfoDao().isCodAvailable(pin);
        }
        return false;
    }

    public List<Courier> getAvailableCouriers(Order order) {

        boolean isCOD = false;
        if (order.getPayment() == null) {
            isCOD = false;
        } else if (order.getPayment().getPaymentMode().equals(getPaymentService().findPaymentMode(EnumPaymentMode.COD))) {
            isCOD = true;
        }
        return getCourierServiceInfoDao().getCouriersForPincode(order.getAddress().getPin(), isCOD);
    }

    public List<Courier> getAvailableCouriers(String pinCode, boolean isCOD) {
        return getCourierServiceInfoDao().getCouriersForPincode(pinCode, isCOD);
    }

    public Courier getDefaultCourierByPincodeForLoggedInWarehouse(Pincode pincode, boolean isCOD) {
        Warehouse warehouse = getUserService().getWarehouseForLoggedInUser();
        return getCourierServiceInfoDao().getDefaultCourierForPincode(pincode, isCOD, warehouse);
    }

    public Courier getDefaultCourier(Pincode pincode, boolean isCOD, Warehouse warehouse) {
        return getCourierServiceInfoDao().getDefaultCourierForPincode(pincode, isCOD, warehouse);
    }

    public Double getCashbackOnGroundShippedItem(Double groundshipItemAmount, Order order, Double groundshipItemweight) {
        String pincode = order.getAddress().getPin();
        Pincode pincodeObj = pincodeDao.getByPincode(pincode);
        if (pincodeObj == null) {
            return null;
        }
        List<Warehouse> warehouses = warehouseService.getAllWarehouses();

        Double[] arrShipmentCost = new Double[warehouses.size()];
        int index = 0;
        for (Warehouse warehouse : warehouses) {
            if (warehouse.getId() != warehouseService.getCorporateOffice().getId()) {
                Courier courier = getDefaultCourier(pincodeObj, true, warehouse);
                CourierPricingEngine courierPricingInfo = courierCostCalculator.getCourierPricingInfo(courier, pincodeObj, warehouse);
                if (courierPricingInfo == null) {
                    return null;
                }
                arrShipmentCost[index] = shipmentPricingEngine.calculateShipmentCost(courierPricingInfo, groundshipItemweight) + shipmentPricingEngine.calculateReconciliationCost(courierPricingInfo,groundshipItemAmount,true) ; 

                index++;
            }
        }
        return minimumShippingCost(arrShipmentCost);
    }


    public CourierDao getCourierDao() {
        return courierDao;
    }

    public void setCourierDao(CourierDao courierDao) {
        this.courierDao = courierDao;
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public CourierServiceInfoDao getCourierServiceInfoDao() {
        return courierServiceInfoDao;
    }

    public void setCourierServiceInfoDao(CourierServiceInfoDao courierServiceInfoDao) {
        this.courierServiceInfoDao = courierServiceInfoDao;
    }

    public PincodeService getPincodeService() {
        return pincodeService;
    }

    public void setPincodeService(PincodeService pincodeService) {
        this.pincodeService = pincodeService;
    }

    public Double minimumShippingCost(Double arrShipmentCost[]) {
        if (arrShipmentCost == null || arrShipmentCost.length == 0) return null;
        Double min = arrShipmentCost[0];
        for (int i = 1; i < arrShipmentCost.length - 1; i++) {
            if (min > arrShipmentCost[i]) min = arrShipmentCost[i];
        }
        return min;
    }
}
