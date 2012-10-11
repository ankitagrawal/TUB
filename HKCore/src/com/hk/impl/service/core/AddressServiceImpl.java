package com.hk.impl.service.core;

import com.akube.framework.dao.Page;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.domain.user.UserDetail;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.dao.user.UserDetailsDao;
import com.hk.pact.service.core.AddressService;
import com.hk.pact.service.user.UserDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/11/12
 * Time: 8:10 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class AddressServiceImpl implements AddressService {

    private static Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

    @Autowired
    AddressDao addressDao;

    @Autowired
    UserDetailsDao userDetailsDao;

    @Transactional
    public Address save(Address address) {
        Address addressRec = addressDao.save(address);
        try{
            User user = address.getUser();
            UserDetail userDetail = new UserDetail();
            userDetail.setUser(user);
            int phoneNumber = Integer.parseInt(address.getPhone());
            userDetail.setPhone(phoneNumber);
            userDetailsDao.save(userDetailsDao);
        }catch (NumberFormatException ex){
            logger.error("Unable to save user information in UserDetail table ", ex);
        }
        return addressRec;
    }

    public List<Address> getVisibleAddresses(User user) {
        return addressDao.getVisibleAddresses(user);
    }

    public Page getVisibleAddressesForManufacturer(Long manufacturerId, String city, int pageNo, int perPage) {
        return addressDao.getVisibleAddressesForManufacturer(manufacturerId, city, pageNo, perPage);
    }
}
