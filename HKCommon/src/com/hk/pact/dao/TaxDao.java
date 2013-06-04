package com.hk.pact.dao;

import java.util.List;

import com.hk.constants.core.EnumTax;
import com.hk.domain.core.Tax;

public interface TaxDao extends BaseDao{

    
    public Tax findByName(String name) ;
        
    public List<Tax> taxListForReport() ;

    public List<Tax> getLocalTaxList() ;

		public List<Tax> getCentralTaxList();

		public List<Tax> getTaxListByType(List<String> taxType);
//    public Tax findByValue(Double taxValue);
	public List<EnumTax> getEnumTaxByType(String type);


}
