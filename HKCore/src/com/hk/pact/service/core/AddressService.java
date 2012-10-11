package com.hk.pact.service.core;

import com.akube.framework.dao.Page;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    List<Address> getVisibleAddresses(User user);

    Page getVisibleAddressesForManufacturer(Long manufacturerId, String city, int pageNo, int perPage);
}
