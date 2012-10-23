package com.hk.impl.service.core;

import com.akube.framework.dao.Page;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.domain.user.UserDetail;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.dao.user.UserDetailDao;
import com.hk.pact.service.core.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    UserDetailDao userDetailsDao;

    @Transactional
    public Address save(Address address) {
        Address addressRec = addressDao.save(address);
        try{
            User user = address.getUser();

            List<Integer> phoneNumbers = new ArrayList<Integer>();
            String[] phones = null;
            String ph = address.getPhone();
            if (ph.contains("-")){
                ph = ph.replace("-","");
            }
            if (ph.contains(",")){
                phones = ph.split(",");
            }else if (ph.contains("/")){
                phones = ph.split("/");
            }else{
                phones = new String[1];
                phones[0] = ph;
            }
            for (String phone : phones){
                UserDetail userDetail = new UserDetail();
                userDetail.setUser(user);
                int start = phone.length() - 10;
                //consider only the last 10 digits
                String userPhone = phone.substring(start, phone.length());
                long phoneNumber = Long.parseLong(userPhone);
                userDetail.setPhone(phoneNumber);
                userDetailsDao.save(userDetail);
            }

        }catch (Exception ex){
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
