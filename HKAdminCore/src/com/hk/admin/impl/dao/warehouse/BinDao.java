package com.hk.admin.impl.dao.warehouse;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.inventory.Bin;
import com.hk.domain.warehouse.Warehouse;

@Repository
public class BinDao extends BaseDaoImpl {

  
  @Transactional
  public Bin getOrCreateBin(Bin bin, Warehouse warehouse) {
    String shelf = "";
    String shelfBin = "";
    if(bin.getShelf() != null){
       shelf = bin.getShelf();
    }
    if(bin.getBin() != null){
       shelfBin = bin.getBin();
    }
    String barcode = bin.getAisle() + bin.getRack() + shelf + shelfBin;
    Bin binDb = findByBarCodeAndWarehouse(barcode,warehouse);
    if (binDb == null) {
      return this.saveBin(bin,warehouse);
    } else {
      binDb.setAisle(bin.getAisle());
      binDb.setRack(bin.getRack());
      binDb.setShelf(shelf);
      binDb.setBin(shelfBin);
      return this.saveBin(binDb,warehouse);
    }
  }

  public Bin findByBarCodeAndWarehouse(String barcode, Warehouse warehouse) {
    Criteria criteria = getSession().createCriteria(Bin.class);
    criteria.add(Restrictions.eq("barcode", barcode));
    criteria.add(Restrictions.eq("warehouse", warehouse));
    return (Bin) criteria.uniqueResult();
  }

  @Transactional
  public Bin saveBin(Bin bin, Warehouse warehouse) {
    bin.setBarcode(bin.getAisle() + bin.getRack() + bin.getShelf());
    bin.setRoom("Default");
    bin.setWarehouse(warehouse);
//    bin.setBuilding(warehouse.getLine2());
    bin.setFloor("Ground");
    bin = (Bin) super.save(bin);
    return bin;
  }
}

