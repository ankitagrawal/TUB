package com.hk.admin.impl.dao.hkDelivery;

import com.hk.domain.courier.Awb;
import com.hk.domain.hkDelivery.*;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.admin.pact.dao.hkDelivery.ConsignmentDao;
import com.hk.domain.courier.Shipment;
import com.hk.domain.user.User;
import com.hk.constants.hkDelivery.EnumConsignmentStatus;
import com.hk.constants.hkDelivery.EnumRunsheetStatus;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public class ConsignmentDaoImpl extends BaseDaoImpl implements ConsignmentDao {

    @Override
    public Consignment createConsignment(String awbNumber,String cnnNumber ,double amount, String paymentMode ,Hub hub) {
        Consignment consignmentObj = new Consignment();
        consignmentObj.setHub(hub);
        consignmentObj.setConsignmentStatus(get(ConsignmentStatus.class, EnumConsignmentStatus.ShipmentReceivedAtHub.getId()));
        consignmentObj.setAwbNumber(awbNumber);
        consignmentObj.setAmount(amount);
        consignmentObj.setCnnNumber(cnnNumber);
        consignmentObj.setCreateDate(new Date());
        consignmentObj.setPaymentMode(paymentMode);
        consignmentObj = (Consignment)save(consignmentObj);
        return consignmentObj;
    }

    @Override
    public List<String> getAwbNumbersInConsignment() {
        return (List<String>) getSession().createQuery(
                "select distinct cn.awbNumber from Consignment cn ").list();
    }

    @Override
    public Consignment getConsignmentByAwbNumber(String awbNumber) {
        return (Consignment) getSession().createQuery("from Consignment cn where cn.awbNumber = :awbNumber").setParameter("awbNumber", awbNumber).uniqueResult();
    }

    public void updateConsignmentTracking(Hub sourceHub, Hub destinationHub, User user, Set<Consignment> consignments) {
        for (Consignment consignment : consignments) {
            ConsignmentTracking consignmntTracking = new ConsignmentTracking();
            consignmntTracking.setConsignment(consignment);
            consignmntTracking.setCreateDate(new Date());
            consignmntTracking.setSourceHub(sourceHub);
            consignmntTracking.setDestinationHub(destinationHub);
            consignmntTracking.setUser(user);
            save(consignmntTracking);
        }
    }

    @Override
    public void updateConsignmentTracking(Hub sourceHub, Hub destinationHub, User user, Consignment consignment) {
        ConsignmentTracking consignmntTracking = new ConsignmentTracking();
        consignmntTracking.setConsignment(consignment);
        consignmntTracking.setCreateDate(new Date());
        consignmntTracking.setSourceHub(sourceHub);
        consignmntTracking.setDestinationHub(destinationHub);
        consignmntTracking.setUser(user);
        save(consignmntTracking);
    }

    @Override
    public List<String> getAllAwbNumbersWithRunsheet() {

        return (List<String>) getSession().createQuery(
                "select distinct cn.awbNumber from Consignment cn where cn.runsheet != null and cn.runsheet.runsheetStatus.id = :runsheetStatusId").setLong("runsheetStatusId",EnumRunsheetStatus.Open.getId()).list();

    }
}

