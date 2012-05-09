package com.hk.impl.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.constants.core.EnumTax;
import com.hk.domain.core.Tax;
import com.hk.pact.dao.TaxDao;

@Repository
public class TaxDaoImpl extends BaseDaoImpl implements TaxDao {

    public Tax findByName(String name) {
        return (Tax) getSession().createQuery("from Tax t where t.name = :name").setString("name", name).uniqueResult();
    }

    
    //TODO: change these querry styles execute one query only.
    public List<Tax> taxListForReport() {
        List<Tax> taxList = new ArrayList<Tax>();
        taxList.add(this.findByName(EnumTax.VAT_0.getName()));
        taxList.add(this.findByName(EnumTax.VAT_5.getName()));
        taxList.add(this.findByName(EnumTax.VAT_12_5.getName()));
        taxList.add(this.findByName(EnumTax.SERVICE_10_3.getName()));
        return taxList;
    }

    public List<Tax> getTaxList() {
        List<Tax> taxList = new ArrayList<Tax>();
        taxList.add(this.findByName(EnumTax.VAT_0.getName()));
        taxList.add(this.findByName(EnumTax.VAT_5.getName()));
        taxList.add(this.findByName(EnumTax.VAT_12_5.getName()));
        taxList.add(this.findByName(EnumTax.SERVICE_10_3.getName()));
        taxList.add(this.findByName(EnumTax.VAT_12_36.getName()));
        taxList.add(this.findByName(EnumTax.VAT_12_5.getName()));
        return taxList;
    }

}
