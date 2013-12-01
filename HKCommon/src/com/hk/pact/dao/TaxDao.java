package com.hk.pact.dao;

import java.util.List;

import com.hk.constants.core.EnumTax;
import com.hk.domain.core.Tax;

public interface TaxDao extends BaseDao{

    
    public Tax findByName(String name) ;

	public Tax findById(Long id);
        
    public List<Tax> taxListForReport() ;

    public List<Tax> getLocalTaxList() ;

		public List<Tax> getCentralTaxList();

		public List<Tax> getTaxListByType(List<String> taxType);

	public List<Tax> getTaxList();
//    public Tax findByValue(Double taxValue);


}
