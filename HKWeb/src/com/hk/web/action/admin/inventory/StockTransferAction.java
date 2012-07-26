package com.hk.web.action.admin.inventory;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.dao.inventory.AdminProductVariantInventoryDao;
import com.hk.admin.pact.dao.inventory.AdminSkuItemDao;
import com.hk.admin.pact.dao.inventory.StockTransferDao;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.domain.inventory.StockTransfer;
import com.hk.domain.inventory.StockTransferLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import net.sourceforge.stripes.action.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class StockTransferAction extends BasePaginatedAction {

    private static Logger               logger                 = Logger.getLogger(StockTransferAction.class);

    @Autowired
    StockTransferDao                    stockTransferDao;
    @Autowired
    UserDao                             userDao;
    @Autowired
    AdminSkuItemDao                     adminSkuItemDao;
    @Autowired
    AdminProductVariantInventoryDao     adminProductVariantInventoryDao;
    @Autowired
    SkuService                          skuService;
    @Autowired
    UserService                         userService;
    @Autowired
    AdminInventoryService               adminInventoryService;
    @Autowired
    InventoryService                    inventoryService;

    private StockTransfer               stockTransfer;
    private String                      userLogin;
    Page                                stockTransferPage;
    private List<StockTransfer>         stockTransferList      = new ArrayList<StockTransfer>();
    private List<StockTransferLineItem> stockTransferLineItems = new ArrayList<StockTransferLineItem>();
    private Integer                     defaultPerPage         = 30;
    private Date                        createDate;
    private Date                        checkOutDate;
    private Warehouse                   fromWarehouse;
    private Warehouse                   toWarehouse;

    @SuppressWarnings("unchecked")
    @DefaultHandler
    public Resolution pre() {
        stockTransferPage = stockTransferDao.searchStockTransfer(createDate, userLogin, fromWarehouse, toWarehouse, getPageNo(), getPerPage());
        stockTransferList = stockTransferPage.getList();
        return new ForwardResolution("/pages/admin/stockTransferList.jsp");
    }

    public Resolution view() {
        if (stockTransfer != null) {
            logger.debug("stockTransfer@Pre: " + stockTransfer.getId());
        }
        return new ForwardResolution("/pages/admin/stockTransfer.jsp");
    }

    public Resolution save() {
        User loggedOnUser = null;
        if (getPrincipal() != null) {
            loggedOnUser = getUserService().getUserById(getPrincipal().getId());
        }
        if (stockTransfer == null || stockTransfer.getId() == null) {
            stockTransfer = new StockTransfer();
        }
        stockTransfer.setCreateDate(createDate);
        stockTransfer.setCheckoutDate(checkOutDate);
        stockTransfer.setCreatedBy(loggedOnUser);
        stockTransfer.setToWarehouse(toWarehouse);
        stockTransfer.setFromWarehouse(fromWarehouse);
        stockTransfer = (StockTransfer) stockTransferDao.save(stockTransfer);

        if (stockTransfer != null) {
            logger.debug("stockTransferLineItems@Save: " + stockTransferLineItems.size());

            for (StockTransferLineItem stockTransferLineItem : stockTransferLineItems) {
                if (stockTransferLineItem.getId() != null) {
                    StockTransferLineItem stockTransferLineItemInDb = stockTransferDao.get(StockTransferLineItem.class, stockTransferLineItem.getId());
                    stockTransferLineItem = (StockTransferLineItem) stockTransferDao.save(stockTransferLineItemInDb);
                } else {
                    Sku sku = stockTransferLineItem.getSku();
                    if (sku == null) {
                        try {
                            sku = skuService.getSKU(stockTransferLineItem.getProductVariant(), stockTransfer.getFromWarehouse());
                        } catch (Exception e) {
                            addRedirectAlertMessage(new SimpleMessage("SKU doesn't exist for " + stockTransferLineItem.getProductVariant().getId()));
                            return new RedirectResolution(StockTransferAction.class).addParameter("stockTransfer", stockTransfer.getId());
                        }
                    }
                    if (stockTransferLineItem.getCheckedoutQty() != null && stockTransferLineItem.getCheckedoutQty() == 0 && stockTransferLineItem.getId() != null) {
                        stockTransferDao.delete(stockTransferLineItem);
                    }
                    List<SkuItem> instockSkuItems = adminSkuItemDao.getInStockSkuItemsBySku(sku);
                    if (!instockSkuItems.isEmpty()) {
                        stockTransferLineItem.setSku(sku);
                        stockTransferLineItem.setStockTransfer(stockTransfer);
                        try {
                            stockTransferLineItem = (StockTransferLineItem) stockTransferDao.save(stockTransferLineItem);
                        } catch (Exception e) {
                            e.printStackTrace();
                            addRedirectAlertMessage(new SimpleMessage("Duplicate batch and variant - " + stockTransferLineItem.getSku().getProductVariant().getId()));
                            return new RedirectResolution(StockTransferAction.class).addParameter("stockTransfer", stockTransfer.getId());
                        }

                        if (adminProductVariantInventoryDao.getPVIForStockTransfer(sku, stockTransferLineItem).isEmpty()) {
                            // Delete from available batches.
                            int counter = 0;
                            for (SkuItem instockSkuItem : instockSkuItems) {
                                if (counter < Math.abs(stockTransferLineItem.getCheckedoutQty())) {
                                    adminInventoryService.inventoryCheckinCheckout(sku, instockSkuItem, null, null, null, null, stockTransferLineItem,
                                            inventoryService.getInventoryTxnType(EnumInvTxnType.STOCK_TRANSFER_CHECKOUT), -1L, loggedOnUser);
                                    counter++;
                                } else {
                                    break;
                                }
                            }
                        }
                    } else {
                        addRedirectAlertMessage(new SimpleMessage("There is no in stock line item(PVI) for " + stockTransferLineItem.getProductVariant().getId()));
                        return new RedirectResolution(StockTransferAction.class).addParameter("view", stockTransfer.getId());
                    }
                }
            }
        }
        addRedirectAlertMessage(new SimpleMessage("Changes saved."));
        return new RedirectResolution(StockTransferAction.class);
    }

    public Resolution checkinInventoryAgainstStockTransfer() {
        User user = null;
        Warehouse userWarehouse = null;
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
        }
        if (userService.getWarehouseForLoggedInUser() == null) {
            addRedirectAlertMessage(new SimpleMessage("There is no warehouse attached with the logged in user. Please check with the admin."));
            return new RedirectResolution(StockTransferAction.class);
        }
        return new ForwardResolution("/pages/admin/inventoryCheckinAgainstStockTransfer.jsp");
    }

    public int getPerPageDefault() {
        return defaultPerPage;
    }

    public int getPageCount() {
        return stockTransferPage == null ? 0 : stockTransferPage.getTotalPages();
    }

    public int getResultCount() {
        return stockTransferPage == null ? 0 : stockTransferPage.getTotalResults();
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Warehouse getFromWarehouse() {
        return fromWarehouse;
    }

    public void setFromWarehouse(Warehouse fromWarehouse) {
        this.fromWarehouse = fromWarehouse;
    }

    public Warehouse getToWarehouse() {
        return toWarehouse;
    }

    public void setToWarehouse(Warehouse toWarehouse) {
        this.toWarehouse = toWarehouse;
    }

    public List<StockTransfer> getStockTransferList() {
        return stockTransferList;
    }

    public void setStockTransferList(List<StockTransfer> stockTransferList) {
        this.stockTransferList = stockTransferList;
    }

    public StockTransfer getStockTransfer() {
        return stockTransfer;
    }

    public void setStockTransfer(StockTransfer stockTransfer) {
        this.stockTransfer = stockTransfer;
    }

    public List<StockTransferLineItem> getStockTransferLineItems() {
        return stockTransferLineItems;
    }

    public void setStockTransferLineItems(List<StockTransferLineItem> stockTransferLineItems) {
        this.stockTransferLineItems = stockTransferLineItems;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("createDate");
        params.add("userLogin");
        params.add("fromWarehouse");
        params.add("toWarehouse");
        return params;
    }
}
