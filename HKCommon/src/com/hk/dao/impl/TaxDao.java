package com.hk.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.constants.core.EnumTax;
import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.core.Tax;

@Repository
public class TaxDao extends BaseDaoImpl {

    public Tax findByName(String name) {
        return (Tax) getSession().createQuery("from Tax t where t.name = :name").setString("name", name).uniqueResult();
    }

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
        return taxList;
    }

}
