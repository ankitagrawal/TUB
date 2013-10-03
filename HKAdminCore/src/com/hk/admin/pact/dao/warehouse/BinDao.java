package com.hk.admin.pact.dao.warehouse;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.domain.inventory.Bin;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

@Repository
public interface BinDao extends BaseDao {

	public Bin getOrCreateBin(Bin bin, Warehouse warehouse);

	public Bin findByBarCodeAndWarehouse(String barcode, Warehouse warehouse);

	public Bin saveBin(Bin bin, Warehouse warehouse);

	public Bin createBin(Bin bin, Warehouse warehouse);

	public Bin getBin(Bin bin, Warehouse warehouse);

	public List<Bin> getAllBinByWarehouse(Warehouse warehouse);
	
}
