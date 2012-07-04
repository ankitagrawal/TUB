package com.hk.admin.impl.service.courier;

import com.hk.admin.pact.dao.courier.CourierDao;
import com.hk.admin.pact.dao.courier.CourierServiceInfoDao;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.CityCourierTAT;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.order.Order;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.PincodeService;
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
    BaseDao baseDao;

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

    public Courier getSuggestedCourierService(String pincode, boolean isCOD) {

/*
        Pincode pincodeObj = getPincodeService().getByPincode(pincode);
        if (pincodeObj != null) {
            Courier courier;
            if (pincode.startsWith("122")) { // Gurgaon
                courier = getCourierById(EnumCourier.Delhivery.getId());
                return courier;
            } else if (pincode.startsWith("110")) { // New Delhi
                Integer random = (new Random()).nextInt(100);
                if (random % 2 == 0) {
                    courier = getCourierById(EnumCourier.Chhotu.getId());
                } else {
                    courier = getCourierById(EnumCourier.Delhivery.getId());
                }
                return courier;
            } else if (pincode.startsWith("2013")) { // Noida
                courier = getCourierById(EnumCourier.Delhivery.getId());
                return courier;
            } else {
                Courier stateCourier = getCourierDao().getPreferredCourierForState(pincodeObj.getState());
                if (stateCourier != null) {
                    CourierServiceInfo courierServiceInfo = getCourierServiceInfoDao().getCourierServiceByPincodeAndCourier(stateCourier.getId(), pincode, isCOD);
                    if (courierServiceInfo != null) {
                        return stateCourier;
                    } else {
                        return selectCourierViaRoundRobin(pincode, isCOD);
                    }
                } else {
                    return selectCourierViaRoundRobin(pincode, isCOD);
                }
            }
        }
*/
        return null;
    }

    private Courier selectCourierViaRoundRobin(String pinCode, boolean isCod) {
        // List<Courier> courierList = getCourierDao().listRestOfIndiaAvailableCouriers();

        Courier dtdcPlus = getCourierById(EnumCourier.DTDC_Plus.getId());
        Courier dtdcLite = getCourierById(EnumCourier.DTDC_Lite.getId());

        List<Courier> applicableCourierList = this.getAvailableCouriers(pinCode, isCod);

        if (applicableCourierList != null && !applicableCourierList.isEmpty()) {
            Integer random = (new Random()).nextInt(applicableCourierList.size());
            Courier serviceProviderCourier = applicableCourierList.get(random);
            if (isCod) {
                if (serviceProviderCourier.equals(dtdcPlus) || serviceProviderCourier.equals(dtdcLite)) {
                    return getCourierById(EnumCourier.DTDC_COD.getId());
                } else if (serviceProviderCourier.equals(getCourierById(EnumCourier.BlueDart.getId()))) {
                    return getCourierById(EnumCourier.BlueDart_COD.getId());
                } else {
                    return serviceProviderCourier; // AFLWIz COD
                }
            } else {
                if (serviceProviderCourier.equals(dtdcPlus) || serviceProviderCourier.equals(dtdcLite)) {
                    if (applicableCourierList.contains(dtdcPlus)) {
                        return dtdcPlus;
                    } else {
                        return dtdcLite;
                    }
                } else {
                    return serviceProviderCourier;
                }
            }
        }
        return getCourierById(EnumCourier.Speedpost.getId());
    }

    public Courier getDefaultCourierByPincodeAndWarehouse(Pincode pincode, boolean isCOD) {
        Warehouse warehouse = getUserService().getWarehouseForLoggedInUser();
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

    public void saveCityCourierTAT(CityCourierTAT cityCourierTAT) {
        baseDao.save(cityCourierTAT);
    }

}
