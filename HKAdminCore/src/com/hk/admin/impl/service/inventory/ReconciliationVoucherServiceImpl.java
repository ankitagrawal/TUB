package com.hk.admin.impl.service.inventory;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.admin.pact.dao.inventory.AdminProductVariantInventoryDao;
import com.hk.admin.pact.dao.inventory.AdminSkuItemDao;
import com.hk.admin.pact.dao.inventory.ReconciliationVoucherDao;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.inventory.ReconciliationVoucherService;
import com.hk.admin.util.ReconciliationVoucherParser;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.constants.inventory.EnumReconciliationType;
import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.catalog.product.ProductVariantDao;
import com.hk.pact.dao.sku.SkuGroupDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.UserService;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Aug 6, 2012
 * Time: 1:26:40 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ReconciliationVoucherServiceImpl implements ReconciliationVoucherService {

	private static Logger logger = LoggerFactory.getLogger(ReconciliationVoucherServiceImpl.class);
	@Autowired
	private BaseDao baseDao;
	@Autowired
	ReconciliationVoucherDao reconciliationVoucherDao;
	@Autowired
	SkuItemDao skuItemDao;
	@Autowired
	UserService userService;

	@Autowired
	ProductVariantDao productVariantDao;
	@Autowired
	ReconciliationVoucherParser rvParser;

	@Autowired
	UserDao userDao;
	@Autowired
	SkuGroupDao skuGroupDao;
	@Autowired
	AdminSkuItemDao adminSkuItemDao;
	@Autowired
	AdminInventoryService adminInventoryService;
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	AdminProductVariantInventoryDao productVariantInventoryDao;
	@Autowired
	SkuService skuService;
	@Autowired
	private ProductVariantService productVariantService;


	public void save(User loggedOnUser, List<RvLineItem> rvLineItems, ReconciliationVoucher reconciliationVoucher) {


		if (reconciliationVoucher == null || reconciliationVoucher.getId() == null) {
			// reconciliationVoucher = new ReconciliationVoucher();
			reconciliationVoucher.setCreateDate(new Date());
			reconciliationVoucher.setCreatedBy(loggedOnUser);
		}
		reconciliationVoucher = (ReconciliationVoucher) reconciliationVoucherDao.save(reconciliationVoucher);

		logger.debug("rvLineItems@Save: " + rvLineItems.size());

		for (RvLineItem rvLineItem : rvLineItems) {
			Sku sku = rvLineItem.getSku();
			if (sku == null) {
				sku = skuService.getSKU(rvLineItem.getProductVariant(), reconciliationVoucher.getWarehouse());
			}
			if (rvLineItem.getQty() != null && rvLineItem.getQty() == 0 && rvLineItem.getId() != null) {
				getBaseDao().delete(rvLineItem);
			} else if (rvLineItem.getId() == null) {
				if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.Add.getId())) {
					rvLineItem.setSku(sku);
					rvLineItem.setReconciliationVoucher(reconciliationVoucher);
					rvLineItem = (RvLineItem) getBaseDao().save(rvLineItem);
					if (productVariantInventoryDao.getPVIForRV(sku, rvLineItem).isEmpty()) {
						// Create batch and checkin inv
						SkuGroup skuGroup = adminInventoryService.createSkuGroup(rvLineItem.getBatchNumber(), rvLineItem.getMfgDate(), rvLineItem.getExpiryDate(), rvLineItem.getCostPrice(), rvLineItem.getMrp(), null, reconciliationVoucher, null, sku);
						adminInventoryService.createSkuItemsAndCheckinInventory(skuGroup, rvLineItem.getQty(), null, null, rvLineItem, null, inventoryService.getInventoryTxnType(EnumInvTxnType.RV_CHECKIN), loggedOnUser);
					}
				} else if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.Subtract.getId())) {
					List<SkuItem> instockSkuItems = adminSkuItemDao.getInStockSkuItemsBySku(sku);
					if (!instockSkuItems.isEmpty()) {
						rvLineItem.setSku(sku);
						rvLineItem.setReconciliationVoucher(reconciliationVoucher);
						rvLineItem = (RvLineItem) getBaseDao().save(rvLineItem);

						if (productVariantInventoryDao.getPVIForRV(sku, rvLineItem).isEmpty()) {
							// Delete from available batches.
							int counter = 0;
							for (SkuItem instockSkuItem : instockSkuItems) {
								if (counter < Math.abs(rvLineItem.getQty())) {
									adminInventoryService.inventoryCheckinCheckout(sku, instockSkuItem, null, null, null, rvLineItem, null,
											inventoryService.getInventoryTxnType(EnumInvTxnType.RV_LOST_PILFERAGE), -1L, loggedOnUser);
									counter++;
								} else {
									break;
								}
							}
						}
					}
				} else if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.Damage.getId())) {
					List<SkuItem> instockSkuItems = adminSkuItemDao.getInStockSkuItemsBySku(sku);
					if (!instockSkuItems.isEmpty()) {
						rvLineItem.setSku(sku);
						rvLineItem.setReconciliationVoucher(reconciliationVoucher);
						rvLineItem = (RvLineItem) getBaseDao().save(rvLineItem);
						if (productVariantInventoryDao.getPVIForRV(sku, rvLineItem).isEmpty()) {
							// Delete from available batches.
							int counter = 0;
							for (SkuItem instockSkuItem : instockSkuItems) {
								if (counter < Math.abs(rvLineItem.getQty())) {
									adminInventoryService.inventoryCheckinCheckout(sku, instockSkuItem, null, null, null, rvLineItem, null,
											inventoryService.getInventoryTxnType(EnumInvTxnType.RV_DAMAGED), -1L, loggedOnUser);
									adminInventoryService.damageInventoryCheckin(instockSkuItem, null);
									counter++;
								} else {
									break;
								}
							}
						}
					}
				} else if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.Expired.getId())) {
					List<SkuItem> instockSkuItems = adminSkuItemDao.getInStockSkuItemsBySku(sku);
					if (!instockSkuItems.isEmpty()) {
						rvLineItem.setSku(sku);
						rvLineItem.setReconciliationVoucher(reconciliationVoucher);
						rvLineItem = (RvLineItem) getBaseDao().save(rvLineItem);
						if (productVariantInventoryDao.getPVIForRV(sku, rvLineItem).isEmpty()) {
							// Delete from available batches.
							int counter = 0;
							for (SkuItem instockSkuItem : instockSkuItems) {
								if (counter < Math.abs(rvLineItem.getQty())) {
									adminInventoryService.inventoryCheckinCheckout(sku, instockSkuItem, null, null, null, rvLineItem, null,
											inventoryService.getInventoryTxnType(EnumInvTxnType.RV_EXPIRED), -1L, loggedOnUser);
									// inventoryService.damageInventoryCheckin(instockSkuItem, null);
									counter++;
								} else {
									break;
								}
							}
						}
					}
				}
				// Check inventory health now.
				inventoryService.checkInventoryHealth(rvLineItem.getSku().getProductVariant());
			}
		}
	}

	public ProductVariantService getProductVariantService() {
		return productVariantService;
	}

	public void setProductVariantService(ProductVariantService productVariantService) {
		this.productVariantService = productVariantService;
	}

	public BaseDao getBaseDao() {
		return baseDao;
	}

	public ReconciliationVoucher save(ReconciliationVoucher reconciliationVoucher) {
		return (ReconciliationVoucher) baseDao.save(reconciliationVoucher);

	}

	public List<RvLineItem> newSave(List<RvLineItem> rvLineItems, ReconciliationVoucher reconciliationVoucher) {
		User loggedOnUser = userService.getLoggedInUser();
		logger.debug("rvLineItems@Save: " + rvLineItems.size());
		 List<RvLineItem> rvNotSaved = new ArrayList<RvLineItem>(0);
		for (RvLineItem rvLineItem : rvLineItems) {
			if (rvLineItem.getQty() != null && rvLineItem.getQty() == 0 && rvLineItem.getId() != null) {
				getBaseDao().delete(rvLineItem);
				continue;
			}
			Sku sku = rvLineItem.getSku();
			if (sku == null) {
				sku = skuService.getSKU(rvLineItem.getProductVariant(), reconciliationVoucher.getWarehouse());
			}
			if (rvLineItem.getReconciliedQty() == null) {

				if (rvLineItem.getId() == null) {
					rvLineItem.setSku(sku);
					rvLineItem.setReconciliationVoucher(reconciliationVoucher);
					rvLineItem = (RvLineItem) getBaseDao().save(rvLineItem);
				}

				String batch = rvLineItem.getBatchNumber().trim();
				SkuGroup skuGroup = skuGroupDao.getSkuGroup(batch);
				if (skuGroup == null) {
					skuGroup = skuGroupDao.getSkuGroupByBatch(batch).get(0);
				}
				List<SkuItem> skuItemList = skuItemDao.getInStockSkuItem(skuGroup);
				if (skuItemList.size() < rvLineItem.getQty()) {
					rvNotSaved.add(rvLineItem);
					continue;
				}
				if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.Damage.getId())) {
					// Delete from entered batches.
					int counter = 0;
					for (SkuItem instockSkuItem : skuItemList) {
						if (counter < Math.abs(rvLineItem.getQty())) {
							adminInventoryService.inventoryCheckinCheckout(sku, instockSkuItem, null, null, null, rvLineItem, null,
									inventoryService.getInventoryTxnType(EnumInvTxnType.RV_DAMAGED), -1L, loggedOnUser);
							adminInventoryService.damageInventoryCheckin(instockSkuItem, null);
							counter++;
						} else {
							break;
						}
					}
					rvLineItem.setReconciliedQty(rvLineItem.getQty());
					rvLineItem = (RvLineItem) getBaseDao().save(rvLineItem);

				} else if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.Expired.getId())) {
					// Delete from available batches.
					int counter = 0;
					for (SkuItem instockSkuItem : skuItemList) {
						if (counter < Math.abs(rvLineItem.getQty())) {
							adminInventoryService.inventoryCheckinCheckout(sku, instockSkuItem, null, null, null, rvLineItem, null,
									inventoryService.getInventoryTxnType(EnumInvTxnType.RV_EXPIRED), -1L, loggedOnUser);
							// inventoryService.damageInventoryCheckin(instockSkuItem, null);
							counter++;
						} else {
							break;
						}
					}
					rvLineItem.setReconciliedQty(rvLineItem.getQty());
					rvLineItem = (RvLineItem) getBaseDao().save(rvLineItem);
				}
				// Check inventory health now.
				inventoryService.checkInventoryHealth(rvLineItem.getSku().getProductVariant());
			}
		}
		return rvNotSaved;
	}
}
