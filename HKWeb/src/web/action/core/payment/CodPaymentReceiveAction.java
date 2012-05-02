package web.action.core.payment;

import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.exception.HealthkartPaymentGatewayException;
import com.hk.manager.OrderManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.service.PaymentService;

/**
 * User: kani Time: 12 Feb, 2010 3:59:02 PM
 */
@Secure
@Component
public class CodPaymentReceiveAction extends BaseAction {

    private CourierService courierService;
    private OrderManager   orderManager;
    private PaymentService paymentService;
    
    private PaymentManager paymentManager;

    // @Named(Keys.Env.codMinAmount)
    @Value("#{hkEnvProps['codMinAmount']}")
    private Double         codMinAmount;

    // @Named(Keys.Env.codMaxAmount)
    @Value("#{hkEnvProps['codMaxAmount']}")
    private Double         codMaxAmount;

    @Validate(required = true)
    private Order          order;

    @Validate(required = true)
    private String         codContactName;

    @Validate(required = true)
    private String         codContactPhone;

    private User           user;

    public Resolution pre() {
        Resolution resolution = null;
        if (order.getOrderStatus().getId().equals(EnumOrderStatus.InCart.getId())) {

            if (StringUtils.isBlank(codContactName)) {
                addRedirectAlertMessage(new SimpleMessage("Cod Contact Name cannot be blank"));
                return new RedirectResolution(PaymentModeAction.class);
            } else if (StringUtils.isBlank(codContactPhone)) {
                addRedirectAlertMessage(new SimpleMessage("Cod Contact Phone cannot be blank"));
                return new RedirectResolution(PaymentModeAction.class);
            } else if (codContactName.length() > 80) {
                addRedirectAlertMessage(new SimpleMessage("Cod Contact Name cannot be longer than 80 characters"));
                return new RedirectResolution(PaymentModeAction.class);
            } else if (codContactPhone.length() > 25) {
                addRedirectAlertMessage(new SimpleMessage("Cod Contact Phone cannot be longer than 25 characters"));
                return new RedirectResolution(PaymentModeAction.class);
            }

            // recalculate the pricing before creating a payment.
            order = orderManager.recalAndUpdateAmount(order);
            // first create a payment row, this will also cotain the payment checksum
            Payment payment = getPaymentManager().createNewPayment(order, getPaymentService().findPaymentMode(EnumPaymentMode.COD), BaseUtils.getRemoteIpAddrForUser(getContext()), null);

            String gatewayOrderId = payment.getGatewayOrderId();

            Address address = order.getAddress();
            String pin = address != null ? address.getPin() : null;

            if (!getCourierService().isCodAllowed(pin)) {
                addRedirectAlertMessage(new SimpleMessage("Cod is not available for this location"));
                return new RedirectResolution(PaymentModeAction.class);
            } else if (order.getIsExclusivelyServiceOrder()) {
                addRedirectAlertMessage(new SimpleMessage("Currently Cod is not available for services"));
                return new RedirectResolution(PaymentModeAction.class);
            } else if (order.getContainsServices()) {
                addRedirectAlertMessage(new SimpleMessage("Currently Cod is not available for services, Please make payments separately for products and services"));
                return new RedirectResolution(PaymentModeAction.class);
            } else if (order.getAmount() < codMinAmount || order.getAmount() > codMaxAmount) {
                addRedirectAlertMessage(new SimpleMessage("Cod is only applicable when total item price is between " + codMinAmount + " and " + codMaxAmount));
                return new RedirectResolution(PaymentModeAction.class);
            }

            try {
                getPaymentManager().verifyPayment(gatewayOrderId, order.getAmount(), null);
                getPaymentManager().codSuccess(gatewayOrderId, codContactName, codContactPhone);
                resolution = new RedirectResolution(PaymentSuccessAction.class).addParameter("gatewayOrderId", gatewayOrderId);
            } catch (HealthkartPaymentGatewayException e) {
                getPaymentManager().error(gatewayOrderId, e);
                resolution = e.getRedirectResolution().addParameter("gatewayOrderId", gatewayOrderId);
            }
        } else {
            addRedirectAlertMessage(new SimpleMessage("Payment for the order is already made."));
            resolution = new RedirectResolution(PaymentModeAction.class).addParameter("order", order);
        }
        return resolution;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getCodContactName() {
        return codContactName;
    }

    public void setCodContactName(String codContactName) {
        this.codContactName = codContactName;
    }

    public String getCodContactPhone() {
        return codContactPhone;
    }

    public void setCodContactPhone(String codContactPhone) {
        this.codContactPhone = codContactPhone;
    }

    public CourierService getCourierService() {
        return courierService;
    }

    public void setCourierService(CourierService courierService) {
        this.courierService = courierService;
    }

    public OrderManager getOrderManager() {
        return orderManager;
    }

    public void setOrderManager(OrderManager orderManager) {
        this.orderManager = orderManager;
    }
    
    public PaymentService getPaymentService() {
        return paymentService;
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public PaymentManager getPaymentManager() {
        return paymentManager;
    }

    public void setPaymentManager(PaymentManager paymentManager) {
        this.paymentManager = paymentManager;
    }

    
}
