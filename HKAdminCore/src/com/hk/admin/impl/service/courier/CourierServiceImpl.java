package com.hk.admin.impl.service.courier;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.admin.pact.dao.courier.CourierDao;
import com.hk.admin.pact.dao.courier.CourierServiceInfoDao;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.order.Order;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.PincodeService;
import com.hk.pact.service.payment.PaymentService;

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
}
