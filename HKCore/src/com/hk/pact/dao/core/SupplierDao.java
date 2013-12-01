package com.hk.pact.dao.core;

import java.util.List;

import com.akube.framework.dao.Page;
import com.hk.domain.catalog.Supplier;
import com.hk.pact.dao.BaseDao;

public interface SupplierDao extends BaseDao {

    public Supplier findByName(String name);

    public Supplier findByTIN(String tinNumber);

    public List<Supplier> getListOrderedByName();

    public Page getSupplierByTinAndName(String tinNumber, String name, Boolean status, int page, int perPage);

    public List<Supplier> getSupplierByTinAndName(String tinNumber, String name, Boolean status);

    public boolean doesTinNumberExist(String tinNumber);
    
    public Supplier save(Supplier supplier);
    
    public Supplier findById(Long id);
}