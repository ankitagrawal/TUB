package com.hk.pact.dao.location;

import java.io.IOException;
import java.util.List;

import com.hk.domain.LocalityMap;
import com.hk.domain.user.Address;
import com.hk.dto.AddressDistanceDto;
import com.hk.pact.dao.BaseDao;

public interface LocalityMapDao extends BaseDao {

    public LocalityMap findByAddress(Address address);

    public Double getDistanceInMeters(Double originLattitude, Double originLongitude, Double destLattitude, Double destLongitude) throws IOException;

    public List<AddressDistanceDto> getClosestAddressList(Double originLattitude, Double originLongitude, Double distance, Integer lim, List<Address> addresses);

}
