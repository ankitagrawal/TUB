package com.hk.pact.service.inventory;

import java.util.Date;
import java.util.List;

import com.hk.domain.catalog.product.ProductVariant;

public interface InventoryHealthService {

	void checkInventoryHealth(ProductVariant productVariant);
	
	long getAvailableUnbookedInventory(ProductVariant productVariant);

	List<InventoryInfo> getAvailableInventory(ProductVariant productVariant, Double preferredMrp);

	public static class InventoryInfo {
		long skuId;
		double mrp;
		long qty;
		Date checkinDate;

		public long getSkuId() {
			return skuId;
		}
		
		public void setSkuId(long skuId) {
			this.skuId = skuId;
		}

		public double getMrp() {
			return mrp;
		}
		
		public void setMrp(double mrp) {
			this.mrp = mrp;
		}

		public Date getCheckinDate() {
			return checkinDate;
		}
		
		public void setCheckinDate(Date checkinDate) {
			this.checkinDate = checkinDate;
		}

		public long getQty() {
			return qty;
		}
		
		public void setQty(long qty) {
			this.qty = qty;
		}
	}
}
