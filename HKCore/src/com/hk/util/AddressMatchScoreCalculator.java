package com.hk.util;

import org.springframework.stereotype.Component;

import com.hk.domain.user.Address;
import com.hk.pact.dao.core.AddressDao;

@Component
public class AddressMatchScoreCalculator {

   AddressDao addressDao;

  public boolean isDuplicateAddress(Address a) {
    boolean isDuplicate = false;
    /*List<Address> duplicateAddressList = addressDao.getDuplicateAddresses(a);
    if (duplicateAddressList != null && !duplicateAddressList.isEmpty()) {
      isDuplicate = true;
    }*/
    return isDuplicate;
  }


}
