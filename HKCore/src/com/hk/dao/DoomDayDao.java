package com.hk.dao;


import java.util.Date;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.DoomDay;

@Repository
public class DoomDayDao extends BaseDaoImpl {

  @Transactional
  public void saveSnapShot(String barcode, Long qty) {
    DoomDay snapshot = new DoomDay();
    snapshot.setUpcVariantId(barcode);
    snapshot.setQty(qty);
    snapshot.setTimeStamp(new Date());
    save(snapshot);
  }

  public Long getCheckedInQty(String barcode) {
    return (Long) getSession().createQuery("select sum(qty) from DoomDay dd where dd.upcVariantId = :barcode").setParameter("barcode", barcode).uniqueResult();
  }
}

