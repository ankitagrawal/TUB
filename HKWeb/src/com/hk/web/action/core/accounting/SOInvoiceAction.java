package com.hk.web.action.core.accounting;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.constants.shipment.EnumCourier;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.Address;
import com.hk.manager.ReferrerProgramManager;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.report.dto.accounting.InvoiceDto;
import com.hk.util.BarcodeGenerator;

@Component
public class SOInvoiceAction extends BaseAction {

    private static Logger          logger = LoggerFactory.getLogger(SOInvoiceAction.class);

    private boolean                printable;
    private Category               sexualCareCategory;

    @Validate(required = true, encrypted = true)
    private ShippingOrder          shippingOrder;
    @Autowired
    private ReferrerProgramManager referrerProgramManager;
    @Autowired
    private BarcodeGenerator       barcodeGenerator;
    @Autowired
    private CategoryService        categoryService;
    @Autowired
    private CourierService         courierService;

    private String                 barcodePath;
    private Coupon                 coupon;
    private String                 routingCode;
    private InvoiceDto             invoiceDto;

    @DefaultHandler
    public Resolution pre() {
        invoiceDto = new InvoiceDto(shippingOrder);
        sexualCareCategory = getCategoryService().getCategoryByName("sexual-care");
        coupon = getReferrerProgramManager().getOrCreateRefferrerCoupon(shippingOrder.getBaseOrder().getUser());
        barcodePath = getBarcodeGenerator().getBarcodePath(shippingOrder.getGatewayOrderId());
        Address address = getBaseDao().get(Address.class, shippingOrder.getBaseOrder().getAddress().getId());
        // As of now we have routing codes for BlueDart only so printing it by default.
        boolean isCod = shippingOrder.isCOD();
        CourierServiceInfo courierServiceInfo = null;
        if (isCod) {
            courierServiceInfo = getCourierService().getCourierServiceByPincodeAndCourier(EnumCourier.BlueDart_COD.getId(), address.getPin(), true);
        } else {
            courierServiceInfo = getCourierService().getCourierServiceByPincodeAndCourier(EnumCourier.BlueDart.getId(), address.getPin(), false);
        }

        if (courierServiceInfo != null) {
            routingCode = courierServiceInfo.getRoutingCode();
        }
        return new ForwardResolution("/pages/shippingOrderInvoice.jsp");
    }

    public Category getSexualCareCategory() {
        return sexualCareCategory;
    }

    public boolean isPrintable() {
        return printable;
    }

    public void setPrintable(boolean printable) {
        this.printable = printable;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public String getBarcodePath() {
        return barcodePath;
    }

    public void setBarcodePath(String barcodePath) {
        this.barcodePath = barcodePath;
    }

    public String getRoutingCode() {
        return routingCode;
    }

    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
    }

    public InvoiceDto getInvoiceDto() {
        return invoiceDto;
    }

    public ReferrerProgramManager getReferrerProgramManager() {
        return referrerProgramManager;
    }

    public void setReferrerProgramManager(ReferrerProgramManager referrerProgramManager) {
        this.referrerProgramManager = referrerProgramManager;
    }

    public BarcodeGenerator getBarcodeGenerator() {
        return barcodeGenerator;
    }

    public void setBarcodeGenerator(BarcodeGenerator barcodeGenerator) {
        this.barcodeGenerator = barcodeGenerator;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public CourierService getCourierService() {
        return courierService;
    }

    public void setCourierService(CourierService courierService) {
        this.courierService = courierService;
    }


}
