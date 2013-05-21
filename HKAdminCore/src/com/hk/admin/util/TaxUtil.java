package com.hk.admin.util;

import com.hk.constants.core.TaxConstants;
import com.hk.constants.courier.StateList;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.core.Tax;
import com.hk.domain.sku.Sku;
import com.hk.dto.TaxComponent;

/**
 * Created by IntelliJ IDEA.
 * User: Rahul
 * Date: Jan 16, 2012
 * Time: 3:16:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaxUtil {

	/**
	 * Supplier and Sku should never be null when supplied to this method.
	 * @param supplier
	 * @param sku
	 * @param taxable
	 * @return
	 */
    public static TaxComponent getSupplierTaxForPV(Supplier supplier, Sku sku, Double taxable) {
		Double tax = 0.0D, surcharge = 0.0D, payable = 0.0D, taxRate = 0.0D;
	    taxRate = sku.getTax().getValue();
		String warehouseState = sku.getWarehouse().getState();
		if (supplier != null && supplier.getState() != null
				&& sku != null && sku.getTax() != null) {

			if (supplier.getState().equalsIgnoreCase(warehouseState)) {
				tax = taxRate * taxable;
				/**
				 * Surchage calculated only for Haryana
				 */
				if (warehouseState.equalsIgnoreCase(StateList.HARYANA)) {
					Double surchargeValue = getSurchargeValue(warehouseState, supplier.getState());
					surcharge = tax * surchargeValue;
				}
			} else {
				if (taxRate != 0.0) {
					taxRate = TaxConstants.CST;
					tax = taxRate * taxable;
					//surcharge = tax * StateList.SURCHARGE;
				}
			}
		}
		payable = taxable + tax + surcharge;
		return new TaxComponent(surcharge, tax, payable, taxRate);
	}

    /**
	 * To be used in case of ExtraInventory only where sku is not present for certain line items.
	 * @param supplier
	 * @param wareHouseState
	 * @param taxValue
	 * @param taxable
	 * @return
	 */
    public static TaxComponent getSupplierTaxForExtraInventory(Supplier supplier, String warehouseState, Tax taxValue, Double taxable) {
		Double tax = 0.0D, surcharge = 0.0D, payable = 0.0D, taxRate = 0.0D;
	    taxRate = taxValue.getValue();
		//String warehouseState = sku.getWarehouse().getState();
		if (supplier != null && supplier.getState() != null) {

			if (supplier.getState().equalsIgnoreCase(warehouseState)) {
				tax = taxRate * taxable;
				/**
				 * Surchage calculated only for Haryana
				 */
				if (warehouseState.equalsIgnoreCase(StateList.HARYANA)) {
					Double surchargeValue = getSurchargeValue(warehouseState, supplier.getState());
					surcharge = tax * surchargeValue;
				}
			} else {
				if (taxRate != 0.0) {
					taxRate = TaxConstants.CST;
					tax = taxRate * taxable;
					//surcharge = tax * StateList.SURCHARGE;
				}
			}
		}
		payable = taxable + tax + surcharge;
		return new TaxComponent(surcharge, tax, payable, taxRate);
	}

    
    public static Double getSurchargeValue(String warehouseState, String supplierState){
      Double surchargeValue = 0.00;
      if(warehouseState.equalsIgnoreCase(supplierState)){
        if(warehouseState.equals(StateList.HARYANA)){
          surchargeValue = TaxConstants.SURCHARGE;
        }
      }
      return surchargeValue;
    }

}