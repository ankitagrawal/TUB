package com.hk.web.action.core.accounting;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.dto.accounting.InvoiceDto;
import com.hk.admin.pact.dao.courier.CourierServiceInfoDao;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.util.BarcodeGenerator;
import com.hk.constants.courier.EnumCourier;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.ReplacementOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.Address;
import com.hk.domain.user.B2bUserDetails;
import com.hk.helper.InvoiceNumHelper;
import com.hk.manager.ReferrerProgramManager;
import com.hk.pact.dao.user.B2bUserDetailsDao;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.core.PincodeService;
import com.hk.pact.service.order.CartFreebieService;

@Component
public class SOInvoiceAction extends BaseAction {

    private boolean printable;

    @Validate(required = true, encrypted = true)
    private ShippingOrder shippingOrder;
    @Autowired
    private ReferrerProgramManager referrerProgramManager;
    @Autowired
    private BarcodeGenerator barcodeGenerator;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CourierService courierService;
    @Autowired
    private CartFreebieService cartFreebieService;
    @Autowired
    private B2bUserDetailsDao b2bUserDetailsDao;
    @Autowired
    private CourierServiceInfoDao courierServiceInfoDao;
    @Autowired
    PincodeService pincodeService;
    @Autowired
    AwbService awbService;

    private String barcodePath;
    private Coupon coupon;
    private String routingCode;
    private InvoiceDto invoiceDto;
    private B2bUserDetails b2bUserDetails;
    private String freebieItem;

    private Shipment shipment;

    @DefaultHandler
    public Resolution pre() {
        if (shippingOrder != null) {
            shipment = shippingOrder.getShipment();
            if (shipment != null) {
                Awb awb = shipment.getAwb();
                if (awb != null && awb.getAwbNumber() != null ) {
                    String trackingId = awb.getAwbNumber();
                    barcodePath = barcodeGenerator.getBarcodePath(trackingId, 2.0f, 200, true);
                }
            }
            ReplacementOrder replacementOrder = getBaseDao().get(ReplacementOrder.class, shippingOrder.getId());
            String invoiceType = InvoiceNumHelper.getInvoiceType(shippingOrder.isServiceOrder(), shippingOrder.getBaseOrder().getB2bOrder());
            if (invoiceType.equals(InvoiceNumHelper.PREFIX_FOR_B2B_ORDER)) {
                b2bUserDetails = b2bUserDetailsDao.getB2bUserDetails(shippingOrder.getBaseOrder().getUser());
            }
            if (replacementOrder != null) {
                invoiceDto = new InvoiceDto(replacementOrder, b2bUserDetails);
            } else {
                invoiceDto = new InvoiceDto(shippingOrder, b2bUserDetails);
            }
            coupon = referrerProgramManager.getOrCreateRefferrerCoupon(shippingOrder.getBaseOrder().getUser());
            barcodePath = barcodeGenerator.getBarcodePath(shippingOrder.getGatewayOrderId(), 1.0f, 150, false);
            Address address = getBaseDao().get(Address.class, shippingOrder.getBaseOrder().getAddress().getId());
            boolean isCod = shippingOrder.isCOD();
            CourierServiceInfo courierServiceInfo = null;
            if (isCod) {
//                courierServiceInfo = courierServiceInfoDao.getCourierServiceByPincodeAndCourier(EnumCourier.BlueDart_COD.getId(), address.getPin(), true);
                   courierServiceInfo = courierService.getCourierServiceInfoForPincode(EnumCourier.BlueDart_COD.getId(), address.getPin(), true , false, false);
            } else {
//                courierServiceInfo = courierServiceInfoDao.getCourierServiceByPincodeAndCourier(EnumCourier.BlueDart.getId(), address.getPin(), false);
                 courierServiceInfo = courierService.getCourierServiceInfoForPincode(EnumCourier.BlueDart_COD.getId(), address.getPin(), false , false, false);
            }

            if (courierServiceInfo != null) {
                routingCode = courierServiceInfo.getRoutingCode();
            }
            freebieItem = cartFreebieService.getFreebieItem(shippingOrder);
            return new ForwardResolution("/pages/shippingOrderInvoice.jsp");
        } else {
            addRedirectAlertMessage(new SimpleMessage("Given shipping order doesnot exist"));
            return new ForwardResolution("pages/admin/adminHome.jsp");
        }
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

    public String getFreebieItem() {
        return freebieItem;
    }

    public void setFreebieItem(String freebieItem) {
        this.freebieItem = freebieItem;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }
}
