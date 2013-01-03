package com.hk.pact.dao;

import java.util.List;

import com.hk.domain.core.Tax;

public interface TaxDao extends BaseDao{

    
    public Tax findByName(String name) ;
        
    public List<Tax> taxListForReport() ;

    public List<Tax> getTaxList() ;

    public Tax findByValue(Double taxValue);
       

}
