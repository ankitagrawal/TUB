package com.hk.pact.service.core;

import com.hk.constants.core.EnumTax;
import com.hk.domain.core.Tax;

import java.util.List;

public interface TaxService {

    
    public Tax findByName(EnumTax enumTax);
    
    public Tax findByName(String taxName);
    
    
    public Tax getServiceTax(); 

		public List<Tax> getTaxList() ;

		public List<Tax> getSurchargeList();
}
