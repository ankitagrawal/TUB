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
    public Consignment getConsignmentByAwbNumber(String awbNumber) {
        String query = "from Consignment cn where cn.awbNumber = :awbNumber";
        return (Consignment) findUniqueByNamedParams(query,new String[]{"awbNumber"},new Object[]{awbNumber});
    }


    @Override
    public List<String> getDuplicateAwbNumbersinRunsheet(List<String> trackingIdList) {
        String query = "select cn.awbNumber from Consignment cn where cn.runsheet != null and cn.runsheet.runsheetStatus.id = :runsheetStatusId and cn.awbNumber in (:trackingIdList)";
        return (List<String>) findByNamedParams(query, new String[]{"runsheetStatusId", "trackingIdList"}, new Object[]{EnumRunsheetStatus.Open.getId(), trackingIdList});
    }

    @Override
    public List<String> getDuplicateAwbNumbersinConsignment(List<String> trackingIdList) {
        String query = "select cn.awbNumber from Consignment cn where cn.awbNumber in (:trackingIdList)";
        return (List<String>)findByNamedParams(query,new String[]{"trackingIdList"},new Object[]{trackingIdList});
    }

    @Override
    public List<Consignment> getConsignmentListByAwbNumbers(List<String> awbNumbers) {
        String query = "from Consignment cn where cn.awbNumber in (:trackingIdList)";
        return (List<Consignment>)findByNamedParams(query,new String[]{"trackingIdList"},new Object[]{awbNumbers});
    }
}

