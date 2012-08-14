package com.hk.pact.dao.core;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.Supplier;
import com.hk.pact.dao.BaseDao;

import java.util.List;

public interface SupplierDao extends BaseDao {

    public Supplier findByName(String name);

    public Supplier findByTIN(String tinNumber);

    public List<Supplier> getListOrderedByName();

    public Page getSupplierByTinAndName(String tinNumber, String name, int page, int perPage);

    public List<Supplier> getSupplierByTinAndName(String tinNumber, String name);

    public boolean doesTinNumberExist(String tinNumber);
    
    public Supplier save(Supplier supplier);
}