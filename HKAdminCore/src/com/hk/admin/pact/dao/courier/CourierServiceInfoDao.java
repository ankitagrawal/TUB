package com.hk.admin.pact.dao.courier;

import java.util.List;

import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

public interface CourierServiceInfoDao extends BaseDao {

  public List<CourierServiceInfo> getCourierServiceInfo(Long courierId);

  public List<CourierServiceInfo> getCourierServicesForPinCode(String pincode);

  public CourierServiceInfo getCourierServiceByPincodeAndCourier(Long courierId, String pincode, Boolean isCod);

  public List<CourierServiceInfo> getCourierServiceInfo(Long courierId, String pincode, Boolean isCOD);

  public boolean isCodAvailable(String pincode);

//  public List<Courier> getCouriersForPincode(String pincode, boolean forCOD);

//  public Courier getDefaultCourierForPincode(Pincode pincode, boolean forCOD, Warehouse warehouse);
    
  public Courier getDefaultCourierForPincode(Pincode pincode, boolean isCOD, boolean isGroundShipping, Warehouse warehouse) ;

  public List<CourierServiceInfo> getCourierServiceInfoByCourierAndPincode(Long courierId, String pincode);

  public CourierServiceInfo getCourierServiceByPincodeAndCourierWithoutCOD(Long courierId, String pincode);

  public boolean isGroundShippingAvailable(String pincode);

  public boolean isCodAvailableOnGroundShipping(String pincode);  

   public List<Courier> getCouriersForPincode(String pincode, boolean forCOD , boolean forGroundShipping);

   public List<CourierServiceInfo> getDifferentCourierDetailsWithPincode(Long courierId, String pincode, boolean forCOD, boolean forGroundShipping, boolean forCodAvailableOnGroundShipping) ;
    
}
