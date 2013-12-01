package com.hk.admin.impl.dao.warehouse;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hk.admin.pact.dao.warehouse.BinDao;
import com.hk.domain.inventory.Bin;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;

@Repository
public class BinDaoImpl extends BaseDaoImpl implements BinDao {

  @Transactional
  public Bin getOrCreateBin(Bin bin, Warehouse warehouse) {
    String shelf = "";
    String shelfBin = "";
    if (bin.getShelf() != null) {
      shelf = bin.getShelf();
    }
    if (bin.getBin() != null) {
      shelfBin = bin.getBin();
    }
    String barcode = bin.getAisle() + bin.getRack() + shelf + shelfBin;
    Bin binDb = findByBarCodeAndWarehouse(barcode, warehouse);
    if (binDb == null) {
      return this.saveBin(bin, warehouse);
    } else {
      binDb.setAisle(bin.getAisle());
      binDb.setRack(bin.getRack());
      binDb.setShelf(shelf);
      binDb.setBin(shelfBin);
      return this.saveBin(binDb, warehouse);
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
    // bin.setBuilding(warehouse.getLine2());
    bin.setFloor("Ground");
    bin = (Bin) super.save(bin);
    return bin;
  }


  // added
  @Transactional
  public Bin createBin(Bin bin, Warehouse warehouse) {
    String shelf = "";
    String shelfBin = "";
    String aisle = "";
    String rack = "";

    if (bin.getShelf() != null) {
      shelf = bin.getShelf();
    }
    if (bin.getBin() != null) {
      shelfBin = bin.getBin();
    }
    if (bin.getAisle() != null) {
      aisle = bin.getAisle();
    }
    if (bin.getRack() != null) {
      rack = bin.getRack();
    }
    String barcode = aisle + rack + shelf + shelfBin;
    if (!barcode.equals("")) {
      barcode = barcode.trim();
      Bin binDb = findByBarCodeAndWarehouse(barcode, warehouse);
      if (binDb != null) {
        return binDb;
      } else {

        return this.saveBin(bin, warehouse);
      }

    }
    return null;
  }


  //added
  @Transactional
  public Bin getBin(Bin bin, Warehouse warehouse) {
    String shelf = "";
    String shelfBin = "";
    String aisle = "";
    String rack = "";
    if (bin != null) {
      if (bin.getShelf() != null) {
        shelf = bin.getShelf();
      }
      if (bin.getBin() != null) {
        shelfBin = bin.getBin();
      }
      String barcode = bin.getAisle() + bin.getRack() + shelf + shelfBin;
      if (!barcode.equals("")) {
        Bin binDb = findByBarCodeAndWarehouse(barcode, warehouse);
        if (binDb != null) {
          return binDb;
        }
      }
    }
    return null;
  }
   @SuppressWarnings("unchecked")
  public List<Bin> getAllBinByWarehouse(Warehouse warehouse) {
    DetachedCriteria criteria =DetachedCriteria.forClass(Bin.class);
     criteria.add(Restrictions.eq("warehouse", warehouse));
    return (List<Bin>) findByCriteria(criteria);
  }
}
