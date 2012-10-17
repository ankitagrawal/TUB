package com.hk.admin.impl.service.courier;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.admin.dto.courier.thirdParty.ThirdPartyAwbDetails;
import com.hk.admin.factory.courier.thirdParty.ThirdPartyAwbServiceFactory;
import com.hk.admin.pact.dao.courier.AwbDao;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.thirdParty.ThirdPartyAwbService;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.AwbStatus;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.warehouse.Warehouse;

/**
 * Created by IntelliJ IDEA. User:Seema Date: Jul 13, 2012 Time: 10:56:32 AM To change this template use File | Settings |
 * File Templates.
 */
@Service
public class AwbServiceImpl implements AwbService {
    @Autowired
    AwbDao awbDao;


    public Awb find(Long id) {
        return awbDao.get(Awb.class, id);
    }

    public List<Awb> getAvailableAwbListForCourierByWarehouseCodStatus(Courier courier, String awbNumber, Warehouse warehouse, Boolean cod, AwbStatus awbStatus) {
        return awbDao.getAvailableAwbForCourierByWarehouseCodStatus(courier, awbNumber, warehouse, cod, awbStatus);
    }

    public Awb getAvailableAwbForCourierByWarehouseCodStatus(Courier courier, String awbNumber, Warehouse warehouse, Boolean cod, AwbStatus awbStatus) {
        List<Awb> awbList = awbDao.getAvailableAwbForCourierByWarehouseCodStatus(courier, awbNumber, warehouse, cod, awbStatus);
        return awbList != null && !awbList.isEmpty() ? awbList.get(0) : null;
    }

    public Awb getAwbForThirdPartyCourier(Courier courier, ShippingOrder shippingOrder, Double weightInKg) {
        Long courierId = courier.getId();

        ThirdPartyAwbService thirdPartyAwbService = ThirdPartyAwbServiceFactory.getThirdPartyAwbService(courierId);
        ThirdPartyAwbDetails thirdPartyAwbDetails = thirdPartyAwbService.getThirdPartyAwbDetails(shippingOrder, weightInKg);
        if (thirdPartyAwbDetails != null) {
            Awb hkAwb = createAwb(courier, thirdPartyAwbDetails.getTrackingNumber(), shippingOrder.getWarehouse(), shippingOrder.isCOD());
            hkAwb = thirdPartyAwbService.syncHKAwbWithThirdPartyAwb(hkAwb, thirdPartyAwbDetails);

            thirdPartyAwbService.syncHKCourierServiceInfo(courierId, thirdPartyAwbDetails);

            return hkAwb;
        }
        return null;

    }

    public boolean deleteAwbForThirdPartyCourier(Courier courier, String awbNumber) {
        Long courierId = courier.getId();

        ThirdPartyAwbService thirdPartyAwbService = ThirdPartyAwbServiceFactory.getThirdPartyAwbService(courierId);
        return thirdPartyAwbService.deleteThirdPartyAwb(awbNumber);
    }

    public void removeAwbForShipment(Courier courier, Awb awb){

        if(ThirdPartyAwbService.integratedCouriers.contains(courier.getId())){
           deleteAwbForThirdPartyCourier(courier, awb.getAwbNumber());
               //awbDao.delete(awb);
        }
        else{
//           awb.setAwbStatus(EnumAwbStatus.Unused.getAsAwbStatus());
//           awbDao.save(awb);
	     save(awb,EnumAwbStatus.Unused.getId().intValue());
        }
    }

	public Object save(Awb awb, Integer newStatus) {
	return awbDao.save(awb,newStatus);
	}

    public Awb findByCourierAwbNumber(Courier courier, String awbNumber) {
        return awbDao.findByCourierAwbNumber(courier, awbNumber);
    }

    public List<Awb> getAllAwb() {
        return awbDao.getAll(Awb.class);
    }

    public List<Awb> getAlreadyPresentAwb(Courier courier, List<String> awbNumberList) {
        return awbDao.getAlreadyPresentAwb(courier, awbNumberList);
    }

  public  Awb createAwb(Courier courier, String trackingNumber, Warehouse warehouse, Boolean isCod) {
        Awb awb = new Awb();
        awb.setCourier(courier);
        awb.setAwbNumber(trackingNumber);
        awb.setAwbStatus(EnumAwbStatus.Unused.getAsAwbStatus());
        awb.setWarehouse(warehouse);
        awb.setCod(isCod);
        awb.setAwbBarCode(trackingNumber);
        awb.setUsed(false);

	    return awb;

    }

    public void delete(Awb awb){
        awbDao.delete(awb);
    }

	public void refresh(Awb awb){
		awbDao.refresh(awb);
	}

}
