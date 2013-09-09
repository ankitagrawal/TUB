package com.hk.web.action.admin.queue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.manager.BinManager;
import com.hk.admin.pact.dao.inventory.AdminSkuItemDao;
import com.hk.admin.pact.dao.inventory.GoodsReceivedNoteDao;
import com.hk.admin.pact.dao.inventory.GrnLineItemDao;
import com.hk.admin.pact.dao.warehouse.BinDao;
import com.hk.admin.util.BarcodeGenerator;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.courier.Courier;
import com.hk.domain.inventory.Bin;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.manager.OrderManager;
import com.hk.manager.ReferrerProgramManager;
import com.hk.pact.dao.catalog.category.CategoryDao;
import com.hk.pact.dao.catalog.product.ProductVariantDao;
import com.hk.pact.dao.payment.PaymentModeDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.dao.sku.SkuDao;
import com.hk.pact.dao.sku.SkuGroupDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.web.action.admin.AdminHomeAction;
import com.hk.web.action.admin.inventory.GRNAction;
import com.hk.web.action.admin.inventory.InventoryCheckinAction;

/**
 * Created by IntelliJ IDEA. User: PRATHAM Date: 1/18/12 Time: 4:44 PM To change this template use File | Settings |
 * File Templates.
 */
@Component
public class JobCartAction extends BaseAction {

    @Autowired
    CategoryDao                             categoryDao;
    @Autowired
    ReferrerProgramManager                  referrerProgramManager;
    @Autowired
    UserDao                                 userDao;
    @Autowired
    BarcodeGenerator                        barcodeGenerator;
    @Autowired
    OrderManager                            orderManager;
    @Autowired
    PaymentModeDao                          paymentModeDao;
    @Autowired
    SkuItemDao                              skuItemDao;
    @Autowired
    AdminSkuItemDao                         adminSkuItemDao;
    @Autowired
    BinDao                                  binDao;
    @Autowired
    ShippingOrderService                    shippingOrderService;
    @Autowired
    UserService                             userService;
    @Autowired
    ShippingOrderStatusService              shippingOrderStatusService;

    @Autowired
    BinManager                              binManager;
    @Autowired
    GrnLineItemDao                          grnLineItemDao;
    @Autowired
    SkuService                              skuService;
    @Autowired
    ProductVariantDao                       productVariantDao;
    @Autowired
    SkuGroupService                         skuGroupService;
    @Autowired
    SkuDao                                  skuDao;
    @Autowired
    GoodsReceivedNoteDao                    goodReceivedNotesdao;
    @Autowired
    ShippingOrderDao                        shippingOrderDao;

    @Validate(required = true, on = "save")
    private String                          upc;
    @Validate(required = true, minvalue = 1.0, on = "save")
    private Long                            qty;

    private String                          batch;
    private Date                            mfgDate;
    private Date                            expiryDate;
    private GoodsReceivedNote               grn;
    private String                          invoiceNumber;
    private Date                            invoiceDate;
    private Bin                             bin;
    private String                          gatewayOrderId;
    private ShippingOrder                   shippingOrder;
    private List<ShippingOrder>             pickingQueueOrders         = new ArrayList<ShippingOrder>();
    private String                          barcodePath;
    private Map<Long, List<ProductVariant>> binHasPVs                  = new HashMap<Long, List<ProductVariant>>();
    public Page                             pickingQueueOrdersPage;

    private Sku                             sku;
    private ProductVariant                  productVariant;
    private SkuGroup                        skuGroup;
    private String                          barcode;
    private List<List<SkuGroup>>            listOfSkuGroupListPerGrn   = new ArrayList<List<SkuGroup>>();
    private Map<Long, Integer>              skuGroupQtyMapping         = new HashMap<Long, Integer>();
    private Map<Long, Integer>              skuGroupQtyMappingPerGrn   = new HashMap<Long, Integer>();
    private Map<SkuGroup, Integer>          skuGroupRemainedQtyMapping = new HashMap<SkuGroup, Integer>();
    private Map<Long, Bin>                  skuBinMapping              = new HashMap<Long, Bin>();
    private Map<String, Long>               productVariantQty          = new HashMap<String, Long>();
    private Map<Long, Bin>                  idBinMap                   = new HashMap<Long, Bin>();
    List<SkuGroup>                          skuGroupListsuggested      = new ArrayList<SkuGroup>();
    private Map<String, SkuGroup>           skuGroupMapProductVariant  = new HashMap<String, SkuGroup>();
    private SkuGroup                        skuGroupField;
    private static Logger                   logger                     = Logger.getLogger(InventoryCheckinAction.class);

    private Category                        category;
    private String                          baseGatewayOrderId;
    private Date                            startDate;
    private Date                            endDate;
    private Courier                         courier;

    @DefaultHandler
    public Resolution jobCart() {
        User user = null;
        Warehouse userWarehouse;
        if (getPrincipal() != null) {
            user = userDao.get(User.class, getPrincipal().getId());
        }
        if (userService.getWarehouseForLoggedInUser() != null) {
            userWarehouse = userService.getWarehouseForLoggedInUser();
        } else {
            addRedirectAlertMessage(new SimpleMessage("There is no warehouse attached with the logged in user. Please check with the admin."));
            return new RedirectResolution(AdminHomeAction.class);
        }
        binHasPVs = new HashMap<Long, List<ProductVariant>>();
        Map<String, Bin> pvhasBin = new HashMap<String, Bin>();

        ShippingOrderSearchCriteria shippingOrderSearchCriteria = getShippingOrderSearchCriteria(EnumShippingOrderStatus.getStatusForPicking());
        /*
         * shippingOrderSearchCriteria.setShippingOrderStatusList(shippingOrderStatusService.getOrderStatuses());
         * shippingOrderSearchCriteria.setBasketCategory(category.getName());
         */

        pickingQueueOrdersPage = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, 1, 10);
        // printingPickingQueueOrdersPage = shippingOrderService.getPickingQueueOrders(1, 10);
        pickingQueueOrders = pickingQueueOrdersPage.getList();
        Bin defaultBin = new Bin();
        defaultBin.setAisle("D");
        defaultBin.setRack("D");
        defaultBin.setShelf("D");
        Bin defaultBinAllocated = binDao.createBin(defaultBin, userWarehouse);
        binDao.refresh(defaultBinAllocated);
        for (ShippingOrder shippingOrder : pickingQueueOrders) {
            Set<LineItem> lineItems = shippingOrder.getLineItems();
            for (LineItem productLineItem : lineItems) {
                String productVariantId = productLineItem.getSku().getProductVariant().getId();
                Long Qty = productLineItem.getQty();
                if (productVariantQty.containsKey(productVariantId)) {
                    Qty = Qty + productVariantQty.get(productVariantId);
                }
                productVariantQty.put(productVariantId, Qty);

                Sku sku = productLineItem.getSku();
                // productVariantQty.put(productLineItem.getSku().getProductVariant().getId(),
                // productLineItem.getQty());
                SkuGroup suggestedSkuGroup;
                List<SkuGroup> skuGroupListPerSku = adminSkuItemDao.getInStockSkuGroups(sku);
                Bin alreadyallocated = null;
                if (skuGroupListPerSku != null && skuGroupListPerSku.size() > 0) {
                    suggestedSkuGroup = skuGroupListPerSku.get(0);
                    if (suggestedSkuGroup != null) {
                        skuGroupMapProductVariant.put(productLineItem.getSku().getProductVariant().getId(), suggestedSkuGroup);
                        List<Long> binIds = binManager.getListOfBinForSkuItemList(suggestedSkuGroup.getSkuItems());
                        if (binIds == null || binIds.size() == 0) {
                            alreadyallocated = defaultBinAllocated;
                            idBinMap.put(alreadyallocated.getId(), alreadyallocated);
                        }
                        if (binIds != null && binIds.size() > 0) {
                            alreadyallocated = binDao.get(Bin.class, binIds.get(0));
                            idBinMap.put(binIds.get(0), alreadyallocated);
                        }

                        pvhasBin.put(sku.getProductVariant().getId(), alreadyallocated);

                    }
                }
            }

        }

        if (pvhasBin.size() > 0) {
            for (Map.Entry<String, Bin> entry : pvhasBin.entrySet()) {
                List<ProductVariant> pvList = new ArrayList<ProductVariant>();
                ProductVariant pv = productVariantDao.get(ProductVariant.class, entry.getKey());
                Bin bin = entry.getValue();
                if (binHasPVs.containsKey(bin.getId())) {
                    pvList = binHasPVs.get(bin.getId());
                    pvList.add(pv);

                } else {
                    pvList.add(pv);
                }
                binHasPVs.put(bin.getId(), pvList);
            }
        }
        return new ForwardResolution("/pages/admin/jobCart.jsp");
    }

    public Resolution saveBins() {

        User user;
        Warehouse userWarehouse;
        if (bin == null) {
            addRedirectAlertMessage(new SimpleMessage("No bin selected  " + productVariant.getId()));
            return new RedirectResolution(JobCartAction.class);
        }
        if (getPrincipal() != null) {
            user = userDao.get(User.class, getPrincipal().getId());
        }
        if (userService.getWarehouseForLoggedInUser() != null) {
            userWarehouse = userService.getWarehouseForLoggedInUser();
        } else {
            addRedirectAlertMessage(new SimpleMessage("There is no warehouse attached with the logged in user. Please check with the admin."));
            return new RedirectResolution(InventoryCheckinAction.class);
        }

        try {
            if (productVariant != null && skuGroupField != null) {
                Sku sku = skuService.getSKU(productVariant, grn.getWarehouse());
                logger.debug("Sku :  " + sku);
                if (bin != null) {
                    boolean status = binManager.assignBinToSkuItems(skuGroupField.getSkuItems(), bin);
                    if (status) {
                        addRedirectAlertMessage(new SimpleMessage("Bin assisgned for" + productVariant.getProduct().getName() + "With Barcode :" + skuGroupField.getBarcode()
                                + " at bin " + bin.getBarcode()));
                        return new RedirectResolution(JobCartAction.class).addParameter("putList").addParameter("grn", grn.getId());
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("execption occurred in addBinForNewProdcutVariant() method ");
            logger.equals("Exception in populating bin details : " + e.getMessage());

        }
        addRedirectAlertMessage(new SimpleMessage("Error in assigning Bin "));
        return new ForwardResolution(JobCartAction.class).addParameter("putList").addParameter("grn", grn.getId());

    }

    public Resolution addBinForNewProdcutVariant() {

        User user;
        Warehouse userWarehouse;
        List<SkuGroup> skuGroupList;
        if (bin == null) {
            addRedirectAlertMessage(new SimpleMessage("Please Enter Aisle ,Shelf , Rack For  " + productVariant.getId()));
            return new ForwardResolution("/pages/modal/addBin.jsp");
        }
        if (getPrincipal() != null) {
            user = userDao.get(User.class, getPrincipal().getId());
        }
        if (userService.getWarehouseForLoggedInUser() != null) {
            userWarehouse = userService.getWarehouseForLoggedInUser();
        } else {
            addRedirectAlertMessage(new SimpleMessage("There is no warehouse attached with the logged in user. Please check with the admin."));
            return new RedirectResolution(InventoryCheckinAction.class);
        }
        logger.debug("upc: " + upc);
        if (productVariant == null) {
            addRedirectAlertMessage(new SimpleMessage(" With Barcode : " + skuGroupField.getBarcode() + " No  Product Variant  " + productVariant.getId() + " is Available"));
            return new ForwardResolution("/pages/modal/addBin.jsp");
        }

        try {
            if (productVariant != null) {
                Sku sku = skuService.getSKU(productVariant, grn.getWarehouse());
                logger.debug("Sku :  " + sku);
                if (skuGroupField != null) {
                    Bin updatedBin = binDao.createBin(bin, userWarehouse);
                    boolean status = binManager.assignBinToSkuItems(skuGroupField.getSkuItems(), updatedBin);
                    if (status) {
                        addRedirectAlertMessage(new SimpleMessage("Bin assisgned for  " + productVariant.getProduct().getName() + "With Barcode :" + skuGroupField.getBarcode()));
                        return new ForwardResolution("/pages/modal/addBin.jsp");
                    }
                }

            }
        } catch (Exception e) {
            System.out.println("execption occurred in addBinForNewProdcutVariant() method ");
            logger.error("Exception in populating bin details : " + e.getMessage());

        }

        if (bin.getAisle() == null && bin.getRack() == null && bin.getShelf() == null && bin.getBin() == null) {
            addRedirectAlertMessage(new SimpleMessage(" atleast one of fields  should be entered " + productVariant.getId()));
        }
        addRedirectAlertMessage(new SimpleMessage("Error in assigning Bin for product : " + productVariant.getId()));
        return new ForwardResolution("/pages/modal/addBin.jsp");
    }

    public Resolution closeWindow() {
        User user;
        Warehouse userWarehouse;
        if (getPrincipal() != null) {
            user = userDao.get(User.class, getPrincipal().getId());
        }
        if (userService.getWarehouseForLoggedInUser() != null) {
            userWarehouse = userService.getWarehouseForLoggedInUser();
        } else {
            addRedirectAlertMessage(new SimpleMessage("There is no warehouse attached with the logged in user. Please check with the admin."));
            return new RedirectResolution(InventoryCheckinAction.class);
        }
        if (grn != null) {
            List<SkuGroup> skuGroupListPerSku;
            List<Sku> skuList = new ArrayList<Sku>();
            for (GrnLineItem grnLineItem : grn.getGrnLineItems()) {
                skuList.add(grnLineItem.getSku());
            }
            for (Sku sku : skuList) {
                skuGroupListPerSku = skuGroupService.getCurrentCheckedInBatchGrn(grn, sku);
                listOfSkuGroupListPerGrn.add(skuGroupListPerSku);
            }

            if (listOfSkuGroupListPerGrn.size() > 0) {
                for (List<SkuGroup> grpList : listOfSkuGroupListPerGrn) {
                    for (SkuGroup skugroup : grpList) {
                        List<Long> binIds = binManager.getListOfBinForSkuItemList(skugroup.getSkuItems());
                        if (binIds == null) {
                            addRedirectAlertMessage(new SimpleMessage("Please Assign Bin to Product Variant  with Batch  : " + skugroup.getBatchNumber() + " having Barcode :  "
                                    + skugroup.getBarcode()));
                            return new RedirectResolution(JobCartAction.class).addParameter("putList").addParameter("grn", grn.getId());
                        }

                    }
                }
            }
        }
        addRedirectAlertMessage(new SimpleMessage("Bin has been assisgned for Grn " + grn.getId()));
        return new ForwardResolution(GRNAction.class);

    }

    public Resolution putList() {
        List<Sku> skuList = new ArrayList<Sku>();
        List<SkuGroup> skuGroupListPerSku = new ArrayList<SkuGroup>();
        List<GrnLineItem> grnLineItemList = grn.getGrnLineItems();
        if (grnLineItemList != null) {
            try {
                for (GrnLineItem grnLineItem : grnLineItemList) {
                    skuList.add(grnLineItem.getSku());
                }
                logger.debug("Number of Sku List : " + skuList.size());
                logger.debug("Number of Sku Groups : " + skuGroupListPerSku.size());
                for (Sku sku : skuList) {
                    skuGroupListPerSku = skuGroupService.getCurrentCheckedInBatchNotInGrn(grn, sku);
                    if (skuGroupListPerSku != null && skuGroupListPerSku.size() > 0) {
                        SkuGroup skuGroup = getSkuGroupWithMaxId(skuGroupListPerSku);
                        skuGroupListsuggested.add(skuGroup);
                    }
                }
                for (Sku sku : skuList) {

                    skuGroupListPerSku = skuGroupService.getCurrentCheckedInBatchGrn(grn, sku);
                    listOfSkuGroupListPerGrn.add(skuGroupListPerSku);
                }
                for (List<SkuGroup> skugrouplist : listOfSkuGroupListPerGrn) {
                    for (SkuGroup skuGroup : skugrouplist) {
                        List<SkuItem> skuItemListPerSkuGroup = adminSkuItemDao.getCheckedInSkuItems(skuGroup); // NO.
                        // of
                        // sku
                        // item
                        // per
                        // sku
                        // group
                        skuGroupQtyMappingPerGrn.put(skuGroup.getId(), skuItemListPerSkuGroup.size());
                    }
                }

                for (List<SkuGroup> skugrouplist : listOfSkuGroupListPerGrn) {
                    for (SkuGroup skuGroup : skugrouplist) {
                        List<SkuItem> skuItemListPerSkuGroup = adminSkuItemDao.getCheckedInSkuItems(skuGroup);
                        List<Bin> qtyPutAtBin = new ArrayList<Bin>();
                        for (SkuItem skuItem : skuItemListPerSkuGroup) {
                            List<Bin> binList = skuItem.getBins();
                            if (binList == null || binList.size() < 1) {
                                break;
                            }
                            qtyPutAtBin.add(binList.get(0));

                        }
                        skuGroupRemainedQtyMapping.put(skuGroup, qtyPutAtBin.size());
                    }
                }

                if (skuGroupListsuggested.size() > 0) {
                    for (SkuGroup skugroup : skuGroupListsuggested) {
                        Sku skuv = skugroup.getSku();
                        List<SkuItem> skuItemListPerSkuGroup = adminSkuItemDao.getInStockSkuItems(skugroup);
                        // skuGroupQtyMapping.put(skuv.getId(), skuItemListPerSkuGroup.size());
                        List<Long> BinLocationPerSkuGroup = binManager.getListOfBinForSkuItemList(new HashSet<SkuItem>(skuItemListPerSkuGroup));
                        Bin binWithMaxId = null;
                        if (null != BinLocationPerSkuGroup && BinLocationPerSkuGroup.size() > 0) {
                            binWithMaxId = binDao.get(Bin.class, BinLocationPerSkuGroup.get(0));
                            skuBinMapping.put(skuv.getId(), binWithMaxId);
                        }
                        logger.debug("Sku Group Id : " + skugroup.getId());

                    }
                }

            } catch (Exception E) {

                System.out.print("Exception occur while generating Put List" + E.getMessage());
                logger.error("Exception occur while generating Put List" + E.getMessage());
                addRedirectAlertMessage(new SimpleMessage("Exception occur while generating Put List " + grn.getId()));
                return new ForwardResolution("/pages/admin/putCart.jsp");
            }
        }

        return new ForwardResolution("/pages/admin/putCart.jsp");
    }

    public SkuGroup getSkuGroupWithMaxId(List<SkuGroup> skuGroupList) {
        Long maxId = 0l;
        SkuGroup skugroup = null;
        for (SkuGroup skugp : skuGroupList) {
            if (skugp.getId() > maxId) {
                maxId = skugp.getId();
                skugroup = skugp;
            }

        }
        if (skugroup != null) {
            return skugroup;
        }

        return null;
    }

    private ShippingOrderSearchCriteria getShippingOrderSearchCriteria(List<EnumShippingOrderStatus> shippingOrderStatuses) {
        ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
	    shippingOrderSearchCriteria.setSearchForPrinting(true);
        shippingOrderSearchCriteria.setShippingOrderStatusList(shippingOrderStatusService.getOrderStatuses(shippingOrderStatuses));

        if (baseGatewayOrderId != null) {
            shippingOrderSearchCriteria.setBaseGatewayOrderId(baseGatewayOrderId);
        } else if (gatewayOrderId != null) {
            shippingOrderSearchCriteria.setGatewayOrderId(gatewayOrderId);
        } else if (startDate != null && endDate != null) {
            shippingOrderSearchCriteria.setLastEscStartDate(startDate);
            shippingOrderSearchCriteria.setLastEscEndDate(endDate);
        }else if(courier !=null){
            List<Courier> couriers = new ArrayList<Courier>();
            couriers.add(courier);
            shippingOrderSearchCriteria.setCourierList(couriers);
        }
        else {
            shippingOrderSearchCriteria.setBasketCategory(category.getName()).setBaseGatewayOrderId(baseGatewayOrderId).setGatewayOrderId(gatewayOrderId);
        }

        return shippingOrderSearchCriteria;
    }

    public Map<Long, Integer> getSkuGroupQtyMappingPerGrn() {
        return skuGroupQtyMappingPerGrn;
    }

    public void setSkuGroupQtyMappingPerGrn(Map<Long, Integer> skuGroupQtyMappingPerGrn) {
        this.skuGroupQtyMappingPerGrn = skuGroupQtyMappingPerGrn;
    }

    public List<List<SkuGroup>> getListOfSkuGroupListPerGrn() {
        return listOfSkuGroupListPerGrn;
    }

    public void setListOfSkuGroupListPerGrn(List<List<SkuGroup>> listOfSkuGroupListPerGrn) {
        this.listOfSkuGroupListPerGrn = listOfSkuGroupListPerGrn;
    }

    public Map<String, Long> getProductVariantQty() {
        return productVariantQty;
    }

    public void setProductVariantQty(Map<String, Long> productVariantQty) {
        this.productVariantQty = productVariantQty;
    }

    public Bin getBin() {
        return bin;
    }

    public void setBin(Bin bin) {
        this.bin = bin;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public Date getMfgDate() {
        return mfgDate;
    }

    public SkuGroup getSkuGroupField() {
        return skuGroupField;
    }

    public void setSkuGroupField(SkuGroup skuGroupField) {
        this.skuGroupField = skuGroupField;
    }

    public void setMfgDate(Date mfgDate) {
        this.mfgDate = mfgDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public GoodsReceivedNote getGrn() {
        return grn;
    }

    public void setGrn(GoodsReceivedNote grn) {
        this.grn = grn;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Map<Long, Bin> getIdBinMap() {
        return idBinMap;
    }

    public void setIdBinMap(Map<Long, Bin> idBinMap) {
        this.idBinMap = idBinMap;
    }

    public Map<Long, Integer> getSkuGroupQtyMapping() {
        return skuGroupQtyMapping;
    }

    public void setSkuGroupQtyMapping(Map<Long, Integer> skuGroupQtyMapping) {
        this.skuGroupQtyMapping = skuGroupQtyMapping;
    }

    public Map<Long, Bin> getSkuBinMapping() {
        return skuBinMapping;
    }

    public void setSkuBinMapping(Map<Long, Bin> skuBinMapping) {
        this.skuBinMapping = skuBinMapping;
    }

    public SkuGroup getSkuGroup() {
        return skuGroup;
    }

    public void setSkuGroup(SkuGroup skuGroup) {
        this.skuGroup = skuGroup;
    }

    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Map<String, SkuGroup> getSkuGroupMapProductVariant() {
        return skuGroupMapProductVariant;
    }

    public void setSkuGroupMapProductVariant(Map<String, SkuGroup> skuGroupMapProductVariant) {
        this.skuGroupMapProductVariant = skuGroupMapProductVariant;
    }

    public String getGatewayOrderId() {
        return gatewayOrderId;
    }

    public String getBaseGatewayOrderId() {
        return baseGatewayOrderId;
    }

    public void setBaseGatewayOrderId(String baseGatewayOrderId) {
        this.baseGatewayOrderId = baseGatewayOrderId;
    }

    public void setGatewayOrderId(String gatewayOrderId) {
        this.gatewayOrderId = gatewayOrderId;
    }

    public String getBarcodePath() {
        return barcodePath;
    }

    public void setBarcodePath(String barcodePath) {
        this.barcodePath = barcodePath;
    }

    public Map<Long, List<ProductVariant>> getBinHasPVs() {
        return binHasPVs;
    }

    public void setBinHasPVs(Map<Long, List<ProductVariant>> binHasPVs) {
        this.binHasPVs = binHasPVs;

    }

    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
    }

    public List<ShippingOrder> getPickingQueueOrders() {
        return pickingQueueOrders;
    }

    public void setPickingQueueOrders(List<ShippingOrder> pickingQueueOrders) {
        this.pickingQueueOrders = pickingQueueOrders;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Map<SkuGroup, Integer> getSkuGroupRemainedQtyMapping() {
        return skuGroupRemainedQtyMapping;
    }

    public void setSkuGroupRemainedQtyMapping(Map<SkuGroup, Integer> skuGroupRemainedQtyMapping) {
        this.skuGroupRemainedQtyMapping = skuGroupRemainedQtyMapping;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

}
