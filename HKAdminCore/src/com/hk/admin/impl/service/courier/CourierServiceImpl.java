package com.hk.admin.impl.service.courier;

import com.hk.admin.pact.dao.courier.CourierDao;
import com.hk.admin.pact.dao.courier.CourierServiceInfoDao;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.order.Order;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.UserService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.constants.payment.EnumPaymentMode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CourierServiceImpl implements CourierService {

	@Autowired
	private CourierDao courierDao;
	@Autowired
	private CourierServiceInfoDao courierServiceInfoDao;
    @Autowired
    private PaymentService paymentService;

	@Autowired
	UserService userService;

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

	public boolean isCodAllowed(String pin) {
		return StringUtils.isNotEmpty(pin) && getCourierServiceInfoDao().isCourierServiceInfoAvailable(null, pin, true, false, false);
	}

	public List<Courier> getAvailableCouriers(Order order) {
          boolean isCOD = false;
        if (order.getPayment() == null) {
            isCOD = false;
        } else if (order.getPayment().getPaymentMode().equals(getPaymentService().findPaymentMode(EnumPaymentMode.COD))) {
            isCOD = true;
        }
		return getCourierServiceInfoDao().getCouriersForPincode(order.getAddress().getPin(), isCOD, false, false);
	}


	public List<Courier> getAvailableCouriers(String pinCode, boolean isCOD, boolean isGroundShipping, boolean isCodAvailableOnGroundShipping) {
		return getCourierServiceInfoDao().getCouriersForPincode(pinCode, isCOD, isGroundShipping, isCodAvailableOnGroundShipping);
	}

	public Courier getDefaultCourierByPincodeForLoggedInWarehouse(Pincode pincode, boolean isCOD, boolean isGroundShipping) {
		Warehouse warehouse = userService.getWarehouseForLoggedInUser();
		return getCourierServiceInfoDao().getDefaultCourierForPincode(pincode, isCOD, isGroundShipping, warehouse);
	}

	public Courier getDefaultCourier(Pincode pincode, boolean isCOD, boolean isGroundShipping, Warehouse warehouse) {
		return getCourierServiceInfoDao().getDefaultCourierForPincode(pincode, isCOD, isGroundShipping, warehouse);
	}


	public boolean isGroundShippingAllowed(String pin) {
		return StringUtils.isNotEmpty(pin) && getCourierServiceInfoDao().isCourierServiceInfoAvailable(null, pin, false, true, false);
	}

	public boolean isCodAllowedOnGroundShipping(String pin) {
		return StringUtils.isNotEmpty(pin) && getCourierServiceInfoDao().isCourierServiceInfoAvailable(null, pin, false, false, true);
	}


	public List<CourierServiceInfo> getCourierServiceInfoList(Long courierId, String pincode, boolean forCOD, boolean forGroundShipping, boolean forCodAvailableOnGroundShipping) {
		return getCourierServiceInfoDao().getCourierServiceInfoList(courierId, pincode, forCOD, forGroundShipping, forCodAvailableOnGroundShipping);
	}

	public CourierServiceInfo getCourierServiceInfoForPincode(Long courierId, String pincode, boolean forCOD, boolean forGroundShipping, boolean forCodAvailableOnGroundShipping) {
		return getCourierServiceInfoDao().getCourierServiceInfoForPincode(courierId, pincode, forCOD, forGroundShipping, forCodAvailableOnGroundShipping);
	}

	public CourierDao getCourierDao() {
		return courierDao;
	}

	public void setCourierDao(CourierDao courierDao) {
		this.courierDao = courierDao;
	}

	public CourierServiceInfoDao getCourierServiceInfoDao() {
		return courierServiceInfoDao;
	}

	public void setCourierServiceInfoDao(CourierServiceInfoDao courierServiceInfoDao) {
		this.courierServiceInfoDao = courierServiceInfoDao;
	}

      public PaymentService getPaymentService() {
        return paymentService;
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
