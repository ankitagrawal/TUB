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
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.util.BarcodeGenerator;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Awb;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.Address;
import com.hk.domain.user.B2bUserDetails;
import com.hk.domain.core.Pincode;
import com.hk.helper.InvoiceNumHelper;
import com.hk.manager.ReferrerProgramManager;
import com.hk.pact.dao.user.B2bUserDetailsDao;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.order.CartFreebieService;
import com.hk.pact.service.core.PincodeService;
import com.hk.pact.service.UserService;

import java.util.List;

@Component
public class SOInvoiceAction extends BaseAction {

    // private static Logger logger = LoggerFactory.getLogger(SOInvoiceAction.class);

    private boolean printable;
    private Category sexualCareCategory;

    private String trackingId;

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
    @Autowired
    UserService userService;

    private String barcodePath;
    private Coupon coupon;
    private String routingCode;
    private InvoiceDto invoiceDto;
    private B2bUserDetails b2bUserDetails;
    private String freebieItem;

    @DefaultHandler
    public Resolution pre() {

        if (shippingOrder != null) {
            String pin = shippingOrder.getBaseOrder().getAddress().getPin();
            List<Awb> awbList = null;
            if (pin != null) {
                Pincode pincode = pincodeService.getByPincode(pin);
                Courier courier = pincode.getDefaultCourier();
                Awb awb = null;
                if (shippingOrder.getShipment().getAwb() != null && shippingOrder.getShipment().getAwb().getAwbNumber() != null) {
                    awb = shippingOrder.getShipment().getAwb();
                } else {
                    if (shippingOrder.isCOD()) {
                        awbList = awbService.getAvailableAwbForCourierByWarehouseCodStatus(courier, null, shippingOrder.getWarehouse(), true, EnumAwbStatus.Unused.getAsAwbStatus());
                    } else {
                        awbList = awbService.getAvailableAwbForCourierByWarehouseCodStatus(courier, null, shippingOrder.getWarehouse(), false, EnumAwbStatus.Unused.getAsAwbStatus());
                    }

                    if (awbList != null && awbList.size() > 0) {
                        awb = awbList.get(0);
                        awb.setAwbStatus(EnumAwbStatus.Attach.getAsAwbStatus());
                    }
                }
                trackingId = awb.getAwbNumber();
                barcodePath = barcodeGenerator.getBarcodePath(trackingId, 1.3f);
            }
            String invoiceType = InvoiceNumHelper.getInvoiceType(shippingOrder.isServiceOrder(), shippingOrder.getBaseOrder().getB2bOrder());
            if (invoiceType.equals(InvoiceNumHelper.PREFIX_FOR_B2B_ORDER)) {
                b2bUserDetails = b2bUserDetailsDao.getB2bUserDetails(shippingOrder.getBaseOrder().getUser());
            }
            invoiceDto = new InvoiceDto(shippingOrder, b2bUserDetails);
            sexualCareCategory = categoryService.getCategoryByName("sexual-care");
            coupon = referrerProgramManager.getOrCreateRefferrerCoupon(shippingOrder.getBaseOrder().getUser());
            barcodePath = barcodeGenerator.getBarcodePath(shippingOrder.getGatewayOrderId(), 1.0f);
            Address address = getBaseDao().get(Address.class, shippingOrder.getBaseOrder().getAddress().getId());
            // As of now we have routing codes for BlueDart only so printing it by default.
            boolean isCod = shippingOrder.isCOD();
            CourierServiceInfo courierServiceInfo = null;
            if (isCod) {
                courierServiceInfo = courierServiceInfoDao.getCourierServiceByPincodeAndCourier(EnumCourier.BlueDart_COD.getId(), address.getPin(), true);
            } else {
                courierServiceInfo = courierServiceInfoDao.getCourierServiceByPincodeAndCourier(EnumCourier.BlueDart.getId(), address.getPin(), false);
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

    public String getFreebieItem() {
        return freebieItem;
    }

    public void setFreebieItem(String freebieItem) {
        this.freebieItem = freebieItem;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }
}
