package com.hk.impl.service.core;

import java.util.List;
import java.util.Set;

import com.hk.domain.core.City;
import com.hk.domain.core.Pincode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.dao.Page;
import com.akube.framework.util.StringUtils;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.domain.user.UserDetail;
import com.hk.domain.user.BillingAddress;
import com.hk.domain.core.Country;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.service.core.AddressService;
import com.hk.pact.service.user.UserDetailService;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/11/12
 * Time: 8:10 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class AddressServiceImpl implements AddressService {

    @SuppressWarnings("unused")
    private static Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

    @Autowired
    AddressDao addressDao;

    @Autowired
    UserDetailService userDetailsService;

    @Transactional
    public Address save(Address address) {
        Address addressRec = addressDao.save(address);
        try{
            User user = addressRec.getUser();
            if (user != null){
                //List<Integer> phoneNumbers = new ArrayList<Integer>();
                String[] phones = null;
                String ph = address.getPhone();
                phones = StringUtils.getUserPhoneList(ph);
                for (String phone : phones){
                    long phoneNumber = StringUtils.getUserPhone(phone);
                    UserDetail dbUserDetail = userDetailsService.findByPhoneAndUser(phoneNumber, user);
                    
                    if (dbUserDetail !=null && addressRec.getDeleted()){
                        userDetailsService.delete(dbUserDetail);
                    }
                    else{
                        if (dbUserDetail == null){
                            UserDetail userDetail = new UserDetail();
                            userDetail.setUser(user);
                            userDetail.setPhone(phoneNumber);
                            userDetailsService.save(userDetail);
                        }
                    }
                }
            }

        }catch (Exception ex){
            //Nothing can be done if user enters some weird mobile..Logging here will only give false alarm
            //logger.error("Unable to save user information in UserDetail table ", ex);
        }
        return addressRec;
    }

     public BillingAddress save(BillingAddress billingAddress){
         return (BillingAddress) addressDao.save(billingAddress);         
     }
    public List<Address> getVisibleAddresses(User user) {
        return addressDao.getVisibleAddresses(user);
    }

    public Page getVisibleAddressesForManufacturer(Long manufacturerId, String city, int pageNo, int perPage) {
        return addressDao.getVisibleAddressesForManufacturer(manufacturerId, city, pageNo, perPage);
    }

    public Set<BillingAddress> getVisibleBillingAddress (User user){
        return addressDao.getVisibleBillingAddresses(user);
    }

       public BillingAddress getBillingAddressById(Long billingAddressId){
           return addressDao.getBillingAddressById(billingAddressId) ;
       }

    public List<Country> getAllCountry() {
        return addressDao.getAllCountry();
    }

     public Country getCountry(Long countryId){
         return  addressDao.getCountry(countryId);
     }
}
