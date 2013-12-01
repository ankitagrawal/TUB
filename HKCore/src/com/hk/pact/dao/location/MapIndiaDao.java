package com.hk.pact.dao.location;

import java.util.List;

import com.hk.domain.MapIndia;
import com.hk.pact.dao.BaseDao;

public interface MapIndiaDao extends BaseDao {

    public MapIndia findByCity(String city) ;

    public List<MapIndia> getTargetCitiesList() ;

}
