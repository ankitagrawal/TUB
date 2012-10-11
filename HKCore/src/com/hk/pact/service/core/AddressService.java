package com.hk.pact.service.core;

import com.hk.domain.user.Address;
import org.springframework.transaction.annotation.Transactional;

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
}
