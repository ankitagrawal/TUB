package com.hk.admin.pact.service.courier;

import java.util.List;

import com.hk.domain.courier.Awb;
import com.hk.domain.courier.AwbStatus;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.warehouse.Warehouse;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Jul 13, 2012
 * Time: 10:55:34 AM
 * To change this template use File | Settings | File Templates.
 */

public interface AwbService {

    public Awb find(Long id);

    public Awb findByCourierAwbNumber(Courier courier ,String awbNumber);

		public Awb findByCourierAwbNumber(List<Courier> couriers, String awbNumber);

    public List<Awb> getAvailableAwbListForCourierByWarehouseCodStatus(Courier courier, String awbNumber, Warehouse warehouse, Boolean cod, AwbStatus awbStatus);

    public Awb getAvailableAwbForCourierByWarehouseCodStatus(Courier courier, String awbNumber, Warehouse warehouse, Boolean cod, AwbStatus awbStatus);

    public List<Awb> getAllAwb();

    public List<Awb> getAlreadyPresentAwb(Courier courier,List<String> awbNumberList);
    
    /**
     * This method should be used for third party courier integrations, please re-factor getAvailableAwbListForCourierByWarehouseCodStatus for simplification when you get time.
     * @param shippingOrder
     * @param weightInKg
     * @return
     */
    public Awb getAwbForThirdPartyCourier(Courier courier, ShippingOrder shippingOrder, Double weightInKg);

    public boolean deleteAwbForThirdPartyCourier(Awb awb);

    public void preserveAwb(Awb awb);

    public void discardAwb(Awb awb);

    public void delete(Awb awb);

	public Awb save(Awb awb, Integer newStatus);

    public  Awb createAwb(Courier courier, String trackingNumber, Warehouse warehouse, Boolean isCod);

	public void refresh(Awb awb);

    boolean isAwbEligibleForDeletion(Courier courier, String awbNumber, Warehouse warehouse, Boolean cod);

    
}
