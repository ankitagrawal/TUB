package com.hk.admin.impl.service.shippingOrder;

import com.hk.admin.engine.ShipmentPricingEngine;
import com.hk.admin.pact.dao.courier.AwbDao;
import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.courier.PincodeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ShipmentServiceImpl implements ShipmentService {

    @Autowired
    private BaseDao baseDao;
    CourierService courierService;
    @Autowired
    PincodeDao pincodeDao;
    @Autowired
    AwbDao awbDao;
    @Autowired
    CourierGroupService courierGroupService;
    @Autowired
    ShipmentPricingEngine shipmentPricingEngine;

    public Shipment createShipment(ShippingOrder shippingOrder) {                 //todo handle null checks
        Shipment shipment = new Shipment();
        Order order = shippingOrder.getBaseOrder();
        Pincode pincode = pincodeDao.getByPincode(order.getAddress().getPin());
        Courier suggestedCourier = courierService.getDefaultCourier(pincode, shippingOrder.isCOD(), shippingOrder.getWarehouse());
        shipment.setCourier(suggestedCourier);
        List<Awb> suggestedAwbList = awbDao.getAvailableAwbForCourierByWarehouseAndCod(suggestedCourier, shippingOrder.getWarehouse(), shippingOrder.isCOD());
        if (suggestedAwbList != null && suggestedAwbList.size() > 0) {
            Awb suggestedAwb = suggestedAwbList.get(0);
            shipment.setTrackingId(suggestedAwb.getAwbNumber());
            suggestedAwb.setUsed(true);
            awbDao.save(suggestedAwb);
        }
        if (courierGroupService.getCourierGroup(shipment.getCourier()) != null) {
            shipment.setEstmShipmentCharge(shipmentPricingEngine.calculateShipmentCost(shippingOrder));
            shipment.setEstmCollectionCharge(shipmentPricingEngine.calculateReconciliationCost(shippingOrder));
            shipment.setExtraCharge(shipmentPricingEngine.calculatePackagingCost(shippingOrder));
        }
        Double estimatedWeight = 0D;
        for (LineItem lineItem : shippingOrder.getLineItems()) {
            ProductVariant productVariant = lineItem.getSku().getProductVariant();
            Double variantWeight = productVariant.getWeight();
            if (variantWeight == null || variantWeight == 0D) {
                estimatedWeight += 125D;
            } else {
                estimatedWeight += variantWeight;
            }
        }
        shipment.setBoxWeight(estimatedWeight);
        return save(shipment);
    }

    public Shipment saveShipmentDate(Shipment shipment) {
        shipment.setShipDate(new Date());
        return (Shipment) getBaseDao().save(shipment);
    }

    public Shipment save(Shipment shipment) {
        return (Shipment) getBaseDao().save(shipment);
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }


}
