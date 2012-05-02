package com.hk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.constants.core.EnumTax;
import com.hk.dao.impl.TaxDao;
import com.hk.domain.core.Tax;
import com.hk.service.TaxService;

@Service
public class TaxServiceImpl implements TaxService {

    @Autowired
    private TaxDao taxDao;

    @Override
    public Tax findByName(EnumTax enumTax) {
        return getTaxDao().findByName(enumTax.getName());
    }


    @Override
    public Tax findByName(String taxName) {
        return getTaxDao().findByName(taxName);      
    }
    
    @Override
    public Tax getServiceTax() {
        return getTaxDao().findByName(EnumTax.SERVICE_10_3.getName());
    }

    
    public TaxDao getTaxDao() {
        return taxDao;
    }

    public void setTaxDao(TaxDao taxDao) {
        this.taxDao = taxDao;
    }


}
