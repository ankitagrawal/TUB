package com.hk.pact.service.inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.hk.domain.catalog.product.ProductVariant;

public interface InventoryHealthService {

	void checkInventoryHealth(ProductVariant productVariant);
	
	long getAvailableUnbookedInventory(ProductVariant productVariant);

	InventoryInfo getAvailableInventory(ProductVariant productVariant, Double preferredMrp);
	
	public static class InventoryInfo {
		private Collection<SkuInfo> skuList = new ArrayList<SkuInfo>();
		private double mrp;
		private long qty;

		public Collection<SkuInfo> getSkuInfoList() {
			return skuList;
		}
		
		public void addSkuInfo(SkuInfo skuInfo) {
			skuList.add(skuInfo);
		}
		
		public double getMrp() {
			return mrp;
		}

		public void setMrp(double mrp) {
			this.mrp = mrp;
		}

		public long getQty() {
			return qty;
		}

		public void setQty(long qty) {
			this.qty = qty;
		}
		
		public SkuInfo getMaxQtySkuInfo() {
			SkuInfo info = null;
			for (SkuInfo skuInfo : skuList) {
				if(info != null && info.getQty() < skuInfo.getQty()) {
					info = skuInfo;
				}
			}
			return info;
		}
	}
	
	public static class SkuInfo {
		private long skuId;
		private double mrp;
		private long qty;
		private Date checkinDate;

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
