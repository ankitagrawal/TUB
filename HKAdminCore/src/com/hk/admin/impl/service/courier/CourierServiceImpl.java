package com.hk.admin.impl.service.courier;

import com.hk.admin.pact.dao.courier.CourierDao;
import com.hk.admin.pact.dao.courier.CourierServiceInfoDao;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.constants.courier.EnumCourier;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.courier.CourierGroup;
import com.hk.domain.order.Order;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.UserService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.constants.payment.EnumPaymentMode;
import com.akube.framework.dao.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

@Service
public class CourierServiceImpl implements CourierService {

    @Autowired
    private CourierDao            courierDao;
    @Autowired
    private CourierServiceInfoDao courierServiceInfoDao;
    @Autowired
    private PaymentService        paymentService;

    @Autowired
    private UserService           userService;

    @Override
    public Courier getCourierById(Long courierId) {
        List<Courier> courierList = getCourierDao().getCourierByIds(Arrays.asList(courierId));
        return courierList != null && courierList.size() > 0 ? courierList.get(0) : null;
    }

    @Override
    public Courier getCourierByName(String name) {
	    if(name != null){
	    List<String> nameList = new ArrayList<String>();
	    nameList.add(name);
	    List<Courier> courierList = getCourierDao().getCouriers(null, nameList, null);
	    return courierList.size() > 0 ? courierList.get(0) : null;
	    }
	    return null;
    }

    public List<Courier> getAllCouriers() {
        return getCourierDao().getAll(Courier.class);
    }

    public boolean isCodAllowed(String pin) {
        return StringUtils.isNotBlank(pin) && getCourierServiceInfoDao().isCourierServiceInfoAvailable(null, pin, true, false, false);
    }

    public List<Courier> getAvailableCouriers(Order order) {
        boolean isCOD = false;
        if (order.getPayment() == null) {
            isCOD = false;
        } else if (order.getPayment().getPaymentMode().equals(getPaymentService().findPaymentMode(EnumPaymentMode.COD))) {
            isCOD = true;
        }
        return getCourierServiceInfoDao().searchCouriers(order.getAddress().getPin(), isCOD, false, false , false);
    }

     public List<Courier> listOfVendorCouriers(){
         return getCourierDao().listOfVendorCouriers();
     }
    
    public List<Courier> getAvailableCouriers(String pinCode, boolean isCOD, boolean isGroundShipping, boolean isCodAvailableOnGroundShipping , Boolean disabled) {
        return getCourierServiceInfoDao().searchCouriers(pinCode, isCOD, isGroundShipping, isCodAvailableOnGroundShipping , disabled);
    }

    public Courier getDefaultCourierByPincodeForLoggedInWarehouse(Pincode pincode, boolean isCOD, boolean isGroundShipping) {
        Warehouse warehouse = getUserService().getWarehouseForLoggedInUser();
        return getCourierServiceInfoDao().searchDefaultCourier(pincode, isCOD, isGroundShipping, warehouse);
    }

    public Courier getDefaultCourier(Pincode pincode, boolean isCOD, boolean isGroundShipping, Warehouse warehouse) {
        return getCourierServiceInfoDao().searchDefaultCourier(pincode, isCOD, isGroundShipping, warehouse);
    }

    public boolean isGroundShippingAllowed(String pin) {
        return StringUtils.isNotBlank(pin) && getCourierServiceInfoDao().isCourierServiceInfoAvailable(null, pin, false, true, false);
    }

    public boolean isCodAllowedOnGroundShipping(String pin) {
        return StringUtils.isNotBlank(pin) && getCourierServiceInfoDao().isCourierServiceInfoAvailable(null, pin, false, false, true);
    }

    public List<CourierServiceInfo> getCourierServiceInfoList(Long courierId, String pincode, boolean forCOD, boolean forGroundShipping, boolean forCodAvailableOnGroundShipping ,Boolean disabled) {
        return getCourierServiceInfoDao().getCourierServiceInfoList(courierId, pincode, forCOD, forGroundShipping, forCodAvailableOnGroundShipping,null);
    }

    public CourierServiceInfo searchCourierServiceInfo(Long courierId, String pincode, boolean forCOD, boolean forGroundShipping, boolean forCodAvailableOnGroundShipping) {
        return getCourierServiceInfoDao().searchCourierServiceInfo(courierId, pincode, forCOD, forGroundShipping, forCodAvailableOnGroundShipping);
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

    public UserService getUserService() {
        return userService;
    }

	public Courier save(Courier courier){
	 return(Courier)getCourierDao().save(courier);

	}
	public List<Courier> getCouriers(List<Long> courierIds ,List<String> courierNames , Boolean disabled){
	return courierDao.getCouriers(courierIds, courierNames ,disabled);
	}

	public List<Courier>  getCouriers(String pincode ,Boolean isrGroundShipping ,Boolean isCod,Boolean isCodAvailableOnGroundShipping,Boolean disabled){
		return getCourierServiceInfoDao().getCouriers(pincode ,isrGroundShipping ,isCod,isCodAvailableOnGroundShipping, disabled);
	}

	public Page getCouriers(String courierName,Boolean disabled, String courierGroup,int page, int perPage){
		return  courierDao.getCouriers(courierName,disabled,courierGroup,page,perPage);
	}

	public void saveOrUpdate(Courier courier) {
		courierDao.saveOrUpdate(courier);
	}

	public List<Courier> getCouriersForDispatchLot() {
		List<Courier> dispatchLotCourierList = new ArrayList<Courier>(0);
		dispatchLotCourierList.add(EnumCourier.JP_CargoVan_Delhi.asCourier());
		dispatchLotCourierList.add(EnumCourier.Xpress_Logistics.asCourier());
		return dispatchLotCourierList;
	}

}
