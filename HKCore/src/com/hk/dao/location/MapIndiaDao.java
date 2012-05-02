package com.hk.dao.location;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.MapIndia;

@Repository
public class MapIndiaDao extends BaseDaoImpl {

    public MapIndia findByCity(String city) {
        return (MapIndia) getSession().createQuery("from MapIndia mi where mi.city = :city").setParameter("city", city).uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public List<MapIndia> getTargetCitiesList() {
        return (List<MapIndia>) getSession().createQuery("from MapIndia mi where mi.targetCity = :isTargetCity").setBoolean("isTargetCity", true).list();
    }

    /*
     * public List<Double> getDistanceInMetersBySql(Double originLattitude, Double originLongitude, Double dist, Long
     * lim) throws IOException { String query = "SELECT mi.id, 3956 * 2 * ASIN(SQRT(POWER(SIN((:originLattitude -
     * ABS(mi.lattitude)) * 3.14287/180 / 2),2) +COS(:originLattitude * 3.14287/180 ) * COS(ABS(mi.lattitude) *
     * 3.14287/180) *POWER(SIN((:originLongitude - ABS(mi.longitude)) * 3.14287/180 / 2), 2) )) " + "AS distance FROM
     * MapIndia mi HAVING distance < :dist ORDER BY distance DESC limit :lim"; return getSession().createQuery(query)
     * .setParameter("originLattitude", originLattitude) .setParameter("originLongitude", originLongitude)
     * .setParameter("distance", dist) .setParameter("limit", lim) .list(); }
     */
}
