package com.hk.admin.pact.dao.shipment;

import com.hk.constants.shipment.EnumShipmentServiceType;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Shipment;
import com.hk.domain.courier.ShipmentServiceType;
import com.hk.pact.dao.BaseDao;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Jul 18, 2012
 * Time: 5:34:27 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ShipmentDao extends BaseDao {

    public Shipment findByAwb(Awb awb);

    public List<ShipmentServiceType> getShipmentServiceTypes(List<EnumShipmentServiceType> enumShipmentServiceTypes);

}
