package com.hk.impl.dao;

import java.util.ArrayList;
import java.util.List;

import com.hk.constants.core.TaxConstants;
import org.springframework.stereotype.Repository;

import com.hk.constants.core.EnumTax;
import com.hk.domain.core.Tax;
import com.hk.pact.dao.TaxDao;

@Repository
public class TaxDaoImpl extends BaseDaoImpl implements TaxDao {

    public Tax findByName(String name) {
        return (Tax) getSession().createQuery("from Tax t where t.name = :name").setString("name", name).uniqueResult();
    }
//     public Tax findByValue(Double taxValue) {
//        List<Tax> taxes = (List<Tax>)getSession().createQuery("from Tax t where t.value = :taxValue").setDouble("taxValue", taxValue).list();
//        if(taxes.size()>1)
//         return (Tax) taxes.get(1);
//       else if(taxes.size()==1)
//        return (Tax) taxes.get(0);
//       else
//          return null;
//    }
    
    //TODO: change these querry styles execute one query only.  
    public List<Tax> taxListForReport() {
        List<Tax> taxList = new ArrayList<Tax>();
        taxList.add(this.findByName(EnumTax.VAT_0.getName()));
        taxList.add(this.findByName(EnumTax.VAT_5.getName()));
        taxList.add(this.findByName(EnumTax.VAT_12_5.getName()));
        taxList.add(this.findByName(EnumTax.SERVICE_10_3.getName()));
        return taxList;
    }

    public List<Tax> getLocalTaxList() {
	      List<String> type = new ArrayList<String>();
	      type.add(TaxConstants.VAT_TYPE);
        return getTaxListByType(type);
    }

	public List<Tax> getCentralTaxList() {
		List<String> type = new ArrayList<String>();
		type.add(TaxConstants.CST_TYPE);
		type.add(TaxConstants.VAT_SECONDARY_TYPE);
		return getTaxListByType(type);
	}

	@SuppressWarnings("unchecked")
	public List<Tax> getTaxListByType(List<String> type) {
		String query = "from Tax t where t.type in (:type)";
		return (List<Tax>) findByNamedParams(query, new String[]{"type"}, new Object[]{type});
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
