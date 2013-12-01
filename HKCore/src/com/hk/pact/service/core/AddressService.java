package com.hk.pact.service.core;

import com.akube.framework.dao.Page;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.domain.user.BillingAddress;
import com.hk.domain.core.Country;
import com.hk.domain.core.Pincode;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/11/12
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AddressService {
    @Transactional
    Address save(Address address);
    @Transactional
    BillingAddress save(BillingAddress billingAddress);

    List<Address> getVisibleAddresses(User user);

    Page getVisibleAddressesForManufacturer(Long manufacturerId, String city, int pageNo, int perPage);

    Set<BillingAddress> getVisibleBillingAddress (User user);

    public BillingAddress getBillingAddressById(Long billingAddressId);

     public List<Country> getAllCountry();

     public Country getCountry(Long countryId);
}
