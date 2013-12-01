package com.hk.pact.dao.core;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.Manufacturer;
import com.hk.pact.dao.BaseDao;

public interface ManufacturerDao extends BaseDao {

    public Manufacturer findByName(String name);

    public Page findManufacturersOrderedByName(int pageNo, int perPage);

}
