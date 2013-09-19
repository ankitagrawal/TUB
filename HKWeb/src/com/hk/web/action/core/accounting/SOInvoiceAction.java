package com.hk.web.action.core.accounting;

import com.hk.constants.courier.EnumCourierOperations;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.courier.*;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.commons.lang.StringUtils;
import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.dto.accounting.InvoiceDto;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.util.BarcodeGenerator;
import com.hk.constants.core.Keys;
import com.hk.constants.courier.EnumCourier;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.order.ReplacementOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.B2bUserDetails;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.helper.InvoiceNumHelper;
import com.hk.manager.ReferrerProgramManager;
import com.hk.pact.dao.user.B2bUserDetailsDao;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.core.PincodeService;
import com.hk.pact.service.order.CartFreebieService;
import com.hk.util.ShipmentServiceMapper;

import java.util.Arrays;
import java.util.List;

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
	private CartFreebieService cartFreebieService;
	@Autowired
	private B2bUserDetailsDao b2bUserDetailsDao;
	@Autowired
	PincodeService pincodeService;
	@Autowired
	AwbService awbService;
	@Autowired
	ShipmentService shipmentService;
	@Autowired
	ShippingOrderService shippingOrderService;
    @Autowired
    PincodeCourierService pincodeCourierService;
    @Autowired
    CourierService courierService;

	@Value("#{hkEnvProps['" + Keys.Env.barcodeDir + "']}")
	String barcodeDir;

	private String barcodePath;
	private String CODBarCodePath;
	private Coupon coupon;
	private String routingCode;
	private InvoiceDto invoiceDto;
	private B2bUserDetails b2bUserDetails;
	private String freebieItem;
	private boolean groundShipped;
	private Shipment shipment;
	private Double estimatedWeightOfPackage;
	String  zone;
	boolean printZone;
    private boolean installableItemPresent;

    private Supplier supplier;

    //todo courier more refactoring needed

	private void generateBarcodesForInvoice(Awb awb) {
		Long courierId = shipment.getAwb().getCourier().getId();
		if (courierId.equals(EnumCourier.FedEx.getId()) || courierId.equals(EnumCourier.FedEx_Surface.getId())) {
			String awbBarCode = awb.getAwbBarCode();
			if (StringUtils.isNotBlank(awbBarCode)) {
				barcodeGenerator.getBarcodePath(awbBarCode, 2.0f, 200, true);
			}
            ShipmentServiceType shipmentServiceType = pincodeCourierService.getShipmentServiceType(shippingOrder);
            if (ShipmentServiceMapper.isCod(shipmentServiceType)) {
				String codReturnBarCode = awb.getReturnAwbBarCode();
				if (StringUtils.isNotBlank(codReturnBarCode)) {
					barcodeGenerator.getBarcodePath(codReturnBarCode, 2.0f, 200, true);
				}
			}
		} else {
			String trackingId = awb.getAwbNumber();
			barcodePath = barcodeGenerator.getBarcodePath(trackingId, 2.0f, 200, true);
		}
	}

    private void generateRoutingCodeForInvoice(ShippingOrder shippingOrder) {
        PincodeCourierMapping pincodeCourierMapping = pincodeCourierService.getApplicablePincodeCourierMapping(shippingOrder.getBaseOrder().getAddress().getPincode(), Arrays.asList(shippingOrder.getShipment().getAwb().getCourier()), shippingOrder.getShipment().getShipmentServiceType(), null);
        if (pincodeCourierMapping != null) {
            routingCode = pincodeCourierMapping.getRoutingCode();
        }
    }

    @DefaultHandler
	public Resolution pre() {
		if (shippingOrder != null) {
			shipment = shippingOrder.getShipment();
            Awb awb = null;
			if (shipment != null) {
				awb = shipment.getAwb();
				if (awb != null && awb.getAwbNumber() != null) {
					generateBarcodesForInvoice(awb);
				}
				generateRoutingCodeForInvoice(shippingOrder);
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
            if(shippingOrder.isDropShipping()){
                for (LineItem lineItem : shippingOrder.getLineItems()) {
                    if (lineItem != null) {
                        ProductVariant productVariant = lineItem.getSku().getProductVariant();
                        if (productVariant != null) {
                            Product product = productVariant.getProduct();
                            if (product != null && product.getSupplier()!= null) {
                                supplier = product.getSupplier();
                                break;
                            }
                        }
                    }
                }
            }
			if (ShipmentServiceMapper.isGround(pincodeCourierService.getShipmentServiceType(shippingOrder))) {
				setGroundShipped(true);
			}
             if (shipmentService.isShippingOrderHasInstallableItem(shippingOrder)){
                     installableItemPresent = true;
            }
			estimatedWeightOfPackage = shipmentService.getEstimatedWeightOfShipment(shippingOrder);

			freebieItem = cartFreebieService.getFreebieItem(shippingOrder);

			printZone = printZoneOnSOInvoice(awb);
            //This is to resolve the issue of zone mismatching on Invoice and print invoice option
            if( (shipment != null) && (shippingOrder.getBaseOrder().getAddress().getPincode().getZone()!= shipment.getZone()) && (shippingOrder.getOrderStatus().getId() <  EnumShippingOrderStatus.SO_Shipped.getId())){
                shipment.setZone(shippingOrder.getBaseOrder().getAddress().getPincode().getZone());
                shipment=shipmentService.save(shipment);
            }
             //till here
			if(printZone){
				zone = shippingOrder.getShipment().getZone().getName();
			}
            if(isHybridRelease())
                return new ForwardResolution("/pages/shippingOrderInvoiceBeta.jsp");
            else
			    return new ForwardResolution("/pages/shippingOrderInvoice.jsp");
		} else {
			addRedirectAlertMessage(new SimpleMessage("Given shipping order doesnot exist"));
			return new ForwardResolution("pages/admin/adminHome.jsp");
		}
	}

    private boolean printZoneOnSOInvoice(Awb awb) {
        if (awb != null) {
            List<Courier> dispatchLotCouriers = courierService.getCouriers(null, null, null, EnumCourierOperations.DISPATCH_LOT.getId());
            return dispatchLotCouriers != null && !dispatchLotCouriers.isEmpty() && dispatchLotCouriers.contains(awb.getCourier());
        } else {
            return false;
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

	public String getCODBarCodePath() {
		return CODBarCodePath;
	}

	public void setCODBarCodePath(String CODBarCodePath) {
		this.CODBarCodePath = CODBarCodePath;
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

	public boolean isGroundShipped() {
		return groundShipped;
	}

	public void setGroundShipped(boolean groundShipped) {
		this.groundShipped = groundShipped;
	}

	public Double getEstimatedWeightOfPackage() {
		return estimatedWeightOfPackage;
	}

	public void setEstimatedWeightOfPackage(Double estimatedWeightOfPackage) {
		this.estimatedWeightOfPackage = estimatedWeightOfPackage;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public boolean getPrintZone() {
		return printZone;
	}

	public void setPrintZone(boolean printZone) {
		this.printZone = printZone;
	}

    public boolean isInstallableItemPresent() {
        return installableItemPresent;
    }

    public void setInstallableItemPresent(boolean installableItemPresent) {
        this.installableItemPresent = installableItemPresent;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
