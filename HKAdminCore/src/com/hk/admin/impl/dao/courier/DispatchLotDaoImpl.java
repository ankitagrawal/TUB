package com.hk.admin.impl.dao.courier;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.courier.DispatchLotDao;
import com.hk.constants.courier.DispatchLotConstants;
import com.hk.constants.courier.EnumDispatchLotStatus;
import com.hk.domain.courier.*;
import com.hk.impl.dao.BaseDaoImpl;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 12/5/12
 * Time: 11:39 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class DispatchLotDaoImpl extends BaseDaoImpl implements DispatchLotDao {

	public Page searchDispatchLot(DispatchLot dispatchLot, String docketNumber, Courier courier, Zone zone, String source,
	                              String destination, Date deliveryStartDate, Date deliveryEndDate, DispatchLotStatus dispatchLotStatus, int pageNo, int perPage) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DispatchLot.class);

		if(dispatchLot != null) {
			criteria.add(Restrictions.eq("id", dispatchLot.getId()));
		}
		if (!StringUtils.isBlank(docketNumber)) {
			criteria.add(Restrictions.eq("docketNumber", docketNumber));
		}

		if (!StringUtils.isBlank(source)) {
			criteria.add(Restrictions.eq("source", source));
		}

		if (!StringUtils.isBlank(destination)) {
			criteria.add(Restrictions.eq("destination", destination));
		}

		if (deliveryStartDate != null) {
			criteria.add(Restrictions.ge("dispatchDate", deliveryStartDate));
		}

		if(deliveryEndDate != null) {
			criteria.add(Restrictions.le("dispatchDate", deliveryEndDate));
		}

		if(zone != null) {
			DetachedCriteria zoneCriteria = criteria.createCriteria("zone");
			zoneCriteria.add(Restrictions.eq("id", zone.getId()));
		}

		if(dispatchLotStatus != null) {
			DetachedCriteria statusCriteria = criteria.createCriteria("dispatchLotStatus");
			statusCriteria.add(Restrictions.eq("id", dispatchLotStatus.getId()));
		}

		if (courier != null) {
			DetachedCriteria courierCriteria = criteria.createCriteria("courier");
			courierCriteria.add(Restrictions.eq("id", courier.getId()));
		}
		criteria.addOrder(Order.desc("id"));

		return list(criteria, pageNo, perPage);
	}

	@SuppressWarnings("unchecked")
	public List<Shipment> getShipmentsForDispatchLot(DispatchLot dispatchLot) {
		String query = "select ds.shipment from DispatchLotHasShipment ds where ds.dispatchLot = :dispatchLot";
        return (List<Shipment>) findByNamedParams(query, new String[]{"dispatchLot"}, new Object[]{dispatchLot});
	}

	@SuppressWarnings("unchecked")
    public List<DispatchLot> getDispatchLotsForShipment(Shipment shipment) {
		String query = "select ds.dispatchLot from DispatchLotHasShipment ds where ds.shipment = :shipment";
        return (List<DispatchLot>) findByNamedParams(query, new String[]{"shipment"}, new Object[]{shipment});
	}

	public DispatchLotHasShipment getDispatchLotHasShipment(DispatchLot dispatchLot, Shipment shipment) {
		String query = "from DispatchLotHasShipment ds where ds.shipment = :shipment and ds.dispatchLot = :dispatchLot";
        return  (DispatchLotHasShipment)findUniqueByNamedParams(query, new String[]{"shipment", "dispatchLot"}, new Object[]{shipment, dispatchLot});
	}

	@SuppressWarnings("unchecked")
	public List<Shipment> getShipmentListExistingInOtherActiveDispatchLot(DispatchLot dispatchLot, List<Shipment> shipmentList) {
		String query = "select ds.shipment from DispatchLotHasShipment ds join ds.dispatchLot d where ds.shipment in (:shipmentList) " +
				" and ds.dispatchLot != :dispatchLot and d.dispatchLotStatus.id != " + EnumDispatchLotStatus.Cancelled.getId() +
				" and ds.shipmentStatus != '" + DispatchLotConstants.SHIPMENT_LOST + "' and ds.shipmentStatus != '" + DispatchLotConstants.SHIPMENT_RECEIVED + "'";
		return (List<Shipment>) findByNamedParams(query, new String[]{"shipmentList", "dispatchLot"}, new Object[]{shipmentList, dispatchLot});
	}

	public List<DispatchLotHasShipment> getDispatchLotHasShipmentListByDispatchLot(DispatchLot dispatchLot) {
		String query = "from DispatchLotHasShipment ds where ds.dispatchLot = :dispatchLot";
		return  (List<DispatchLotHasShipment>)findByNamedParams(query, new String[]{"dispatchLot"}, new Object[]{dispatchLot});

	}

}
