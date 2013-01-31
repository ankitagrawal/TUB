package com.hk.admin.impl.service.inventory;

import java.util.*;

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
			reconciliationVoucher.setReconciliationType(EnumReconciliationType.Add.asReconciliationType());
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
						adminInventoryService.createSkuItemsAndCheckinInventory(skuGroup, rvLineItem.getQty(), null, null, rvLineItem, null, EnumInvTxnType.RV_CHECKIN.asInvTxnType(), loggedOnUser);
					}
				rvLineItem.setReconciledQty(rvLineItem.getQty());
				rvLineItem = (RvLineItem) getBaseDao().save(rvLineItem);					

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
											EnumInvTxnType.RV_LOST_PILFERAGE.asInvTxnType(), -1L, loggedOnUser);
									counter++;
								} else {
									break;
								}
							}
						}
					}
				} else if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.Damage.getId())) {
					List<SkuItem> instockSkuItems = adminSkuItemDao.getInStockSkuItemsBySku(sku);
					int counter = 0;
					if (!instockSkuItems.isEmpty()) {
						rvLineItem.setSku(sku);
						rvLineItem.setReconciliationVoucher(reconciliationVoucher);
						rvLineItem = (RvLineItem) getBaseDao().save(rvLineItem);
						if (productVariantInventoryDao.getPVIForRV(sku, rvLineItem).isEmpty()) {
							// Delete from available batches.
							for (SkuItem instockSkuItem : instockSkuItems) {
								if (counter < Math.abs(rvLineItem.getQty())) {
									adminInventoryService.inventoryCheckinCheckout(sku, instockSkuItem, null, null, null, rvLineItem, null,
											EnumInvTxnType.RV_DAMAGED.asInvTxnType(), -1L, loggedOnUser);
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
					int counter = 0;
					if (!instockSkuItems.isEmpty()) {
						rvLineItem.setSku(sku);
						rvLineItem.setReconciliationVoucher(reconciliationVoucher);
						rvLineItem = (RvLineItem) getBaseDao().save(rvLineItem);
						if (productVariantInventoryDao.getPVIForRV(sku, rvLineItem).isEmpty()) {
							// Delete from available batches.
							for (SkuItem instockSkuItem : instockSkuItems) {
								if (counter < Math.abs(rvLineItem.getQty())) {
									adminInventoryService.inventoryCheckinCheckout(sku, instockSkuItem, null, null, null, rvLineItem, null,
											EnumInvTxnType.RV_EXPIRED.asInvTxnType(), -1L, loggedOnUser);
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

	public RvLineItem reconcile(RvLineItem rvLineItem, ReconciliationVoucher reconciliationVoucher, List<SkuItem> skuItemList) {
		User loggedOnUser = userService.getLoggedInUser();		
			if (rvLineItem.getId() == null) {
				rvLineItem.setReconciliationVoucher(reconciliationVoucher);
				rvLineItem = (RvLineItem) getBaseDao().save(rvLineItem);
			}

			try {
				int targetReconciledQty = rvLineItem.getQty().intValue() - rvLineItem.getReconciledQty().intValue();
				int counter = 0;
				if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.Damage.getId())) {
					// Delete from entered batches.
					for (SkuItem instockSkuItem : skuItemList) {
						if (counter < Math.abs(targetReconciledQty)) {
							adminInventoryService.inventoryCheckinCheckout(rvLineItem.getSku(), instockSkuItem, null, null, null, rvLineItem, null,
									EnumInvTxnType.RV_DAMAGED.asInvTxnType(), -1L, loggedOnUser);
							adminInventoryService.damageInventoryCheckin(instockSkuItem, null);
							counter++;
						} else {
							break;
						}
					}

				} else if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.Expired.getId())) {
					// Delete from available batches.
					for (SkuItem instockSkuItem : skuItemList) {
						if (counter < Math.abs(targetReconciledQty)) {
							adminInventoryService.inventoryCheckinCheckout(rvLineItem.getSku(), instockSkuItem, null, null, null, rvLineItem, null,
									EnumInvTxnType.RV_EXPIRED.asInvTxnType(), -1L, loggedOnUser);
							// inventoryService.damageInventoryCheckin(instockSkuItem, null);
							counter++;
						} else {
							break;
						}
					}

				} else if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.Lost.getId())) {
					// Delete from available batches.
					for (SkuItem instockSkuItem : skuItemList) {
						if (counter < Math.abs(targetReconciledQty)) {
							adminInventoryService.inventoryCheckinCheckout(rvLineItem.getSku(), instockSkuItem, null, null, null, rvLineItem, null,
									EnumInvTxnType.RV_LOST_PILFERAGE.asInvTxnType(), -1L, loggedOnUser);
							// inventoryService.damageInventoryCheckin(instockSkuItem, null);
							counter++;
						} else {
							break;
						}
					}

				}
				else if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.BatchMismatch.getId())) {
					// Delete from available batches.
					for (SkuItem instockSkuItem : skuItemList) {
						if (counter < Math.abs(targetReconciledQty)) {
							adminInventoryService.inventoryCheckinCheckout(rvLineItem.getSku(), instockSkuItem, null, null, null, rvLineItem, null,
									EnumInvTxnType.RV_BATCH_MISMATCH.asInvTxnType(), -1L, loggedOnUser);
							// inventoryService.damageInventoryCheckin(instockSkuItem, null);
							counter++;
						} else {
							break;
						}
					}

				}
				else if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.MrpMismatch.getId())) {
					// Delete from available batches.
					for (SkuItem instockSkuItem : skuItemList) {
						if (counter < Math.abs(targetReconciledQty)) {
							adminInventoryService.inventoryCheckinCheckout(rvLineItem.getSku(), instockSkuItem, null, null, null, rvLineItem, null,
									EnumInvTxnType.RV_MRP_MISMATCH.asInvTxnType(), -1L, loggedOnUser);
							// inventoryService.damageInventoryCheckin(instockSkuItem, null);
							counter++;
						} else {
							break;
						}
					}

				}
				else if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.NonMoving.getId())) {
					// Delete from available batches.
					for (SkuItem instockSkuItem : skuItemList) {
						if (counter < Math.abs(targetReconciledQty)) {
							adminInventoryService.inventoryCheckinCheckout(rvLineItem.getSku(), instockSkuItem, null, null, null, rvLineItem, null,
									EnumInvTxnType.RV_NON_MOVING.asInvTxnType(), -1L, loggedOnUser);
							// inventoryService.damageInventoryCheckin(instockSkuItem, null);
							counter++;
						} else {
							break;
						}
					}

				}
				long alreadyReconciledQty = rvLineItem.getReconciledQty().intValue()+counter;
				rvLineItem.setReconciledQty(Long.valueOf(alreadyReconciledQty));
				rvLineItem = (RvLineItem) getBaseDao().save(rvLineItem);

			} catch (Exception e) {
				return null;
			}
			// Check inventory health now.
			inventoryService.checkInventoryHealth(rvLineItem.getSku().getProductVariant());


		return rvLineItem;
	}


	public void save(List<RvLineItem> rvLineItems, ReconciliationVoucher reconciliationVoucher) {
		logger.debug("rvLineItems@Save: " + rvLineItems.size());

		for (RvLineItem rvLineItem : rvLineItems) {
			if (rvLineItem.getQty() != null && rvLineItem.getQty() == 0 && rvLineItem.getId() != null) {
				getBaseDao().delete(rvLineItem);
				continue;
			}
			if (rvLineItem.getId() == null) {
				rvLineItem.setReconciliationVoucher(reconciliationVoucher);
				getBaseDao().save(rvLineItem);
			} else {
				getBaseDao().saveOrUpdate(rvLineItem);
			}
			baseDao.saveOrUpdate(reconciliationVoucher);

		}
	}

	public void delete(ReconciliationVoucher reconciliationVoucher){
		getBaseDao().delete(reconciliationVoucher);
	}



        public RvLineItem createRVLineItemWithBasicDetails(SkuGroup skuGroup, Sku sku) {
        RvLineItem rvLineItem = new RvLineItem();
        rvLineItem.setProductVariant(sku.getProductVariant());
        rvLineItem.setBatchNumber(skuGroup.getBatchNumber());
        rvLineItem.setQty(1L);
        rvLineItem.setMrp(skuGroup.getMrp());
        rvLineItem.setCostPrice(skuGroup.getCostPrice());
        rvLineItem.setMfgDate(skuGroup.getMfgDate());
        rvLineItem.setExpiryDate(skuGroup.getExpiryDate());
        rvLineItem.setSku(sku);
        rvLineItem.setSkuGroup(skuGroup);
        return rvLineItem;
    }


    public RvLineItem reconcileSKUItems(ReconciliationVoucher reconciliationVoucher, RvLineItem rvLineItem, SkuItem skuItem) {
        RvLineItem rvLineItem1 = null;
        SkuGroup skuGroup = skuItem.getSkuGroup();
        Sku sku = skuGroup.getSku();


        if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.Damage.getId())) {
            rvLineItem1 = reconciliationVoucherDao.getRvLineItems(reconciliationVoucher, sku, skuGroup, rvLineItem.getReconciliationType());
            if (rvLineItem1 == null) {
                rvLineItem1 = createRVLineItemWithBasicDetails(skuGroup, sku);
            }
            rvLineItem1.setReconciliationType(EnumReconciliationType.Damage.asReconciliationType());
            rvLineItem1.setReconciliationVoucher(reconciliationVoucher);
            if (rvLineItem1.getReconciledQty() != null) {
                rvLineItem1.setReconciledQty(rvLineItem1.getReconciledQty() + 1L);
                rvLineItem1.setQty(rvLineItem1.getQty() + 1L);
            } else {
                rvLineItem1.setReconciledQty(1L);
                rvLineItem1.setQty(1L);
            }
            rvLineItem1 = (RvLineItem) getBaseDao().save(rvLineItem1);
            adminInventoryService.inventoryCheckinCheckout(sku, skuItem, null, null, null, rvLineItem1, null,
                    inventoryService.getInventoryTxnType(EnumInvTxnType.RV_DAMAGED), -1L, userService.getLoggedInUser());
            adminInventoryService.damageInventoryCheckin(skuItem, null);
        } else if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.Lost.getId())) {
            rvLineItem1 = reconciliationVoucherDao.getRvLineItems(reconciliationVoucher, sku, skuGroup, rvLineItem.getReconciliationType());
            if (rvLineItem1 == null) {
                rvLineItem1 = createRVLineItemWithBasicDetails(skuGroup, sku);
            }
            rvLineItem1.setReconciliationType(EnumReconciliationType.Lost.asReconciliationType());
            rvLineItem1.setReconciliationVoucher(reconciliationVoucher);
            if (rvLineItem1.getReconciledQty() != null) {
                rvLineItem1.setReconciledQty(rvLineItem1.getReconciledQty() + 1L);
                rvLineItem1.setQty(rvLineItem1.getQty() + 1L);
            } else {
                rvLineItem1.setReconciledQty(1L);
                rvLineItem1.setQty(1L);
            }
            rvLineItem1 = (RvLineItem) getBaseDao().save(rvLineItem1);
            adminInventoryService.inventoryCheckinCheckout(sku, skuItem, null, null, null, rvLineItem1, null,
                    inventoryService.getInventoryTxnType(EnumInvTxnType.RV_LOST_PILFERAGE), -1L, userService.getLoggedInUser());

        } else if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.Expired.getId())) {
            rvLineItem1 = reconciliationVoucherDao.getRvLineItems(reconciliationVoucher, sku, skuGroup, rvLineItem.getReconciliationType());
            if (rvLineItem1 == null) {
                rvLineItem1 = createRVLineItemWithBasicDetails(skuGroup, sku);
            }

            rvLineItem1.setReconciliationType(EnumReconciliationType.Expired.asReconciliationType());
            rvLineItem1.setReconciliationVoucher(reconciliationVoucher);
            if (rvLineItem1.getReconciledQty() != null) {
                rvLineItem1.setReconciledQty(rvLineItem1.getReconciledQty() + 1L);
                rvLineItem1.setQty(rvLineItem1.getQty() + 1L);
            } else {
                rvLineItem1.setReconciledQty(1L);
                rvLineItem1.setQty(1L);
            }
            rvLineItem1 = (RvLineItem) getBaseDao().save(rvLineItem1);
            adminInventoryService.inventoryCheckinCheckout(sku, skuItem, null, null, null, rvLineItem1, null,
                    inventoryService.getInventoryTxnType(EnumInvTxnType.RV_EXPIRED), -1L, userService.getLoggedInUser());

        } else if (rvLineItem.getReconciliationType().getId().equals(EnumReconciliationType.BatchMismatch.getId())) {
            rvLineItem1 = reconciliationVoucherDao.getRvLineItems(reconciliationVoucher, sku, skuGroup, rvLineItem.getReconciliationType());
            if (rvLineItem1 == null) {
                rvLineItem1 = createRVLineItemWithBasicDetails(skuGroup, sku);
            }

            rvLineItem1.setReconciliationType(EnumReconciliationType.BatchMismatch.asReconciliationType());
            rvLineItem1.setReconciliationVoucher(reconciliationVoucher);
            if (rvLineItem1.getReconciledQty() != null) {
                rvLineItem1.setReconciledQty(rvLineItem1.getReconciledQty() + 1L);
                rvLineItem1.setQty(rvLineItem1.getQty() + 1L);
            } else {
                rvLineItem1.setReconciledQty(1L);
                rvLineItem1.setQty(1L);
            }
            rvLineItem1 = (RvLineItem) getBaseDao().save(rvLineItem1);
            adminInventoryService.inventoryCheckinCheckout(sku, skuItem, null, null, null, rvLineItem1, null,
                    inventoryService.getInventoryTxnType(EnumInvTxnType.RV_BATCH_MISMATCH), -1L, userService.getLoggedInUser());
        }

        return rvLineItem1;
    }

}
