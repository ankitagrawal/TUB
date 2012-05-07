package com.hk.impl.dao.location;

import java.io.IOException;
import java.util.List;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.hk.domain.LocalityMap;
import com.hk.domain.user.Address;
import com.hk.dto.AddressDistanceDto;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.location.LocalityMapDao;

@Repository
public class LocalityMapDaoImpl extends BaseDaoImpl implements LocalityMapDao {

    public LocalityMap findByAddress(Address address) {
        return (LocalityMap) getSession().createQuery("from LocalityMap l where l.address = :address").setEntity("address", address).uniqueResult();
    }

    public Double getDistanceInMeters(Double originLattitude, Double originLongitude, Double destLattitude, Double destLongitude) throws IOException {

        return 3956 * 2 * Math.asin(Math.sqrt(Math.pow(Math.sin((originLattitude - Math.abs(destLattitude)) * Math.PI / 180 / 2), 2) + Math.cos(originLattitude * Math.PI / 180)
                * Math.cos(Math.abs(destLattitude) * Math.PI / 180) * Math.pow(Math.sin((originLongitude - Math.abs(destLongitude)) * Math.PI / 180 / 2), 2)));
    }

    @SuppressWarnings("unchecked")
    public List<AddressDistanceDto> getClosestAddressList(Double originLattitude, Double originLongitude, Double distance, Integer lim, List<Address> addresses) {

        String query = "SELECT mi as localityMap, 3956 * 2 * ASIN(SQRT(POWER(SIN((:originLattitude - ABS(mi.lattitude)) * 3.14287/180 / 2),2) + "
                + "COS(:originLattitude * 3.14287/180 ) * COS(ABS(mi.lattitude) * 3.14287/180) *POWER(SIN((:originLongitude - ABS(mi.longitude)) * 3.14287/180 / 2), 2) )) "
                + "AS distance FROM LocalityMap mi where mi.address in (:addresses) and 3956 * 2 * ASIN(SQRT(POWER(SIN((:originLattitude - ABS(mi.lattitude)) * 3.14287/180 / 2),2) + "
                + "COS(:originLattitude * 3.14287/180 ) * COS(ABS(mi.lattitude) * 3.14287/180) *POWER(SIN((:originLongitude - ABS(mi.longitude)) * 3.14287/180 / 2), 2) )) < :distance";

        return getSession().createQuery(query).setParameter("originLattitude", originLattitude).setParameter("originLongitude", originLongitude).setParameterList("addresses",
                addresses).setParameter("distance", distance).setMaxResults(lim).setResultTransformer(Transformers.aliasToBean(AddressDistanceDto.class)).list();
    }

    /*
     * String query = "SELECT 3956 * 2 * ASIN(SQRT(POWER(SIN((:originLattitude - ABS(:destLattitude)) * 3.14287/180 /
     * 2),2) +COS(:originLattitude * 3.14287/180 ) * COS(ABS(:destLattitude) * 3.14287/180) *POWER(SIN((:originLongitude -
     * ABS(:destLongitude)) * 3.14287/180 / 2), 2) ))" + " FROM MapIndia mi"; return (Double)
     * getSession().createQuery(query) .setParameter("originLattitude", originLattitude)
     * .setParameter("originLongitude", originLongitude) .setParameter("destLongitude", destLongitude)
     * .setParameter("destLattitude", destLattitude) .list().get(0);
     */
}
