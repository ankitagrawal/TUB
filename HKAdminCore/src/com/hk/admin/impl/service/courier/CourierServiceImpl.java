package com.hk.admin.impl.service.courier;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.courier.CourierDao;
import com.hk.admin.pact.dao.courier.PincodeCourierMappingDao;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CourierServiceImpl implements CourierService {

    @Autowired
    private CourierDao courierDao;
    @Autowired
    private UserService userService;
    @Autowired
    PincodeCourierMappingDao pincodeCourierMappingDao;

    @Override
    public Courier getCourierById(Long courierId) {
        List<Courier> courierList = getCourierDao().getCourierByIds(Arrays.asList(courierId));
        return courierList != null && courierList.size() > 0 ? courierList.get(0) : null;
    }

    @Override
    public Courier getCourierByName(String name) {
        if (name != null) {
            List<String> nameList = new ArrayList<String>();
            nameList.add(name);
            List<Courier> courierList = getCourierDao().getCouriers(null, nameList, null, null);
            return courierList.size() > 0 ? courierList.get(0) : null;
        }
        return null;
    }

    public List<Courier> getCouriers(List<Long> courierIds, List<String> courierNames, Boolean active, Long operationBitset) {
        return courierDao.getCouriers(courierIds, courierNames, active, operationBitset);
    }

    public Page getCouriers(String courierName, Boolean active, String courierGroup, int page, int perPage, Long operationsBitset) {
        return courierDao.getCouriers(courierName, active, courierGroup, page, perPage, operationsBitset);
    }

    public void saveOrUpdate(Courier courier) {
        courierDao.saveOrUpdate(courier);
    }

    public List<Courier> getAllCouriers() {
        return getCourierDao().getAll(Courier.class);
    }

    public Courier getDefaultCourier(Pincode pincode, boolean isCOD, boolean isGroundShipping, Warehouse warehouse) {
        List<Courier> couriers = pincodeCourierMappingDao.searchDefaultCourier(pincode, isCOD, isGroundShipping, warehouse);
        return couriers != null && !couriers.isEmpty() ? couriers.get(0) : null;
    }

    @Override
    public List<Courier> getDefaultCouriers(Pincode pincode, boolean isCOD, boolean isGroundShipping, Warehouse warehouse) {
        return pincodeCourierMappingDao.searchDefaultCourier(pincode, isCOD, isGroundShipping, warehouse);
    }

    public CourierDao getCourierDao() {
        return courierDao;
    }

    public void setCourierDao(CourierDao courierDao) {
        this.courierDao = courierDao;
    }

    public UserService getUserService() {
        return userService;
    }

    public Courier save(Courier courier) {
        return (Courier) getCourierDao().save(courier);

    }

}
