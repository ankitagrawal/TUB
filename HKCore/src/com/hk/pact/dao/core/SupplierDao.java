package com.hk.pact.dao.core;

import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.Supplier;
import com.hk.pact.dao.BaseDao;

public interface SupplierDao extends BaseDao {

    public Supplier findByName(String name);

    public Supplier findByTIN(String tinNumber);

    public List<Supplier> getListOrderedByName();

    public Page getSupplierByTinAndName(String tinNumber, String name, int page, int perPage);

    public boolean doesTinNumberExist(String tinNumber);
}