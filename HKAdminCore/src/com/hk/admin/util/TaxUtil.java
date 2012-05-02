package com.hk.admin.util;

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

	public static TaxComponent getSupplierTaxForPV(Supplier supplier, Sku sku, Double taxable) {
		Double tax = 0.0D, surcharge = 0.0, payable = 0.0;
		String warehouseState = sku.getWarehouse().getState();
		if (supplier != null && supplier.getState() != null
				&& sku != null && sku.getTax() != null) {
			Tax skuTax = sku.getTax();

			if (supplier.getState().equalsIgnoreCase(warehouseState)) {
				tax = skuTax.getValue() * taxable;
				/**
				 * Surchage calculated only for Haryana
				 */
				if (warehouseState.equalsIgnoreCase(StateList.HARYANA)) {
					surcharge = tax * StateList.SURCHARGE;
				}
			} else {
				if (skuTax.getValue() != 0.0) {
					tax = StateList.CST * taxable;
					//surcharge = tax * StateList.SURCHARGE;
				}
			}
		}
		payable = taxable + tax + surcharge;
		return new TaxComponent(surcharge, tax, payable);
	}

}
