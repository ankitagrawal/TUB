package com.hk.web.action.admin.inventory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.slf4j.Logger;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.dao.inventory.StockTransferDao;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.domain.inventory.StockTransfer;
import com.hk.domain.inventory.StockTransferLineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.inventory.ProductVariantInventoryDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;


public class StockTransferAction extends BasePaginatedAction {

    private static Logger logger = Logger.getLogger(StockTransferAction.class);

    StockTransferDao stockTransferDao;
    StockTransferLineItemDao stockTransferLineItemDao;
    UserDao userDao;
    SkuItemDao skuItemDao;
    ProductVariantInventoryDao> productVariantInventoryDao;
    SkuService skuService;
    UserService userService;
    InventoryService inventoryService;

    private StockTransfer stockTransfer;
    private String userLogin;
    Page stockTransferPage;
    private List<StockTransfer> stockTransferList = new ArrayList<StockTransfer>();
    private List<StockTransferLineItem> stockTransferLineItems = new ArrayList<StockTransferLineItem>();
    private Integer defaultPerPage = 30;
    private Date createDate;
    private Date checkOutDate;
    private Warehouse fromWarehouse;
    private Warehouse toWarehouse;

    @DefaultHandler
    public Resolution pre() {


        stockTransferPage = stockTransferDaoProvider.get().searchStockTransfer(createDate, userLogin, fromWarehouse, toWarehouse, getPageNo(), getPerPage());
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
        stockTransfer = stockTransferDaoProvider.get().save(stockTransfer);

        if (stockTransfer != null) {
            logger.debug("stockTransferLineItems@Save: " + stockTransferLineItems.size());

            for (StockTransferLineItem stockTransferLineItem : stockTransferLineItems) {
                if (stockTransferLineItem.getId() != null) {
                    StockTransferLineItem stockTransferLineItemInDb = stockTransferLineItemDaoProvider.get().find(stockTransferLineItem.getId());
                    stockTransferLineItem = stockTransferLineItemDaoProvider.get().save(stockTransferLineItemInDb);
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
                        stockTransferLineItemDaoProvider.get().remove(stockTransferLineItem.getId());
                    }
                    List<SkuItem> instockSkuItems = skuItemDaoProvider.get().getInStockSkuItemsBySku(sku);
                    if (!instockSkuItems.isEmpty()) {
                        stockTransferLineItem.setSku(sku);
                        stockTransferLineItem.setStockTransfer(stockTransfer);
                        try {
                            stockTransferLineItem = stockTransferLineItemDaoProvider.get().save(stockTransferLineItem);
                        } catch (Exception e) {
                            e.printStackTrace();
                            addRedirectAlertMessage(new SimpleMessage("Duplicate variant - " + stockTransferLineItem.getSku().getProductVariant().getId()));
                            return new RedirectResolution(StockTransferAction.class).addParameter("stockTransfer", stockTransfer.getId());
                        }

                        if (productVariantInventoryDaoProvider.get().getPVIForStockTransfer(sku, stockTransferLineItem).isEmpty()) {
                            //Delete from available batches.
                            int counter = 0;
                            for (SkuItem instockSkuItem : instockSkuItems) {
                                if (counter < Math.abs(stockTransferLineItem.getCheckedoutQty())) {
                                    inventoryService.inventoryCheckinCheckout(sku, instockSkuItem, null, null, null, null, stockTransferLineItem, invTxnTypeDaoProvider.get().find(EnumInvTxnType.STOCK_TRANSFER_CHECKOUT.getId()), -1L, loggedOnUser);
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
