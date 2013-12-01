package com.hk.pact.dao;


public interface DoomDayDao extends BaseDao {

    public void saveSnapShot(String barcode, Long qty);

    public Long getCheckedInQty(String barcode);
}
