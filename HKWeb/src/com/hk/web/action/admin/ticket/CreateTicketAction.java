package com.hk.web.action.admin.ticket;

import java.util.Date;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.EmailTypeConverter;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.Breadcrumb;
import com.akube.framework.util.FormatUtils;
import com.hk.admin.dto.ticket.TicketDto;
import com.hk.admin.manager.TicketManager;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.ticket.EnumTicketType;
import com.hk.domain.Ticket;
import com.hk.domain.TicketType;
import com.hk.domain.courier.Courier;
import com.hk.domain.order.Order;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.manager.EmailManager;
import com.hk.manager.LinkManager;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.service.UserService;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * User: rahul Time: 9 Mar, 2010 3:16:15 PM
 */
@Secure(hasAllPermissions = PermissionConstants.CREATE_TICKETS, authActionBean = AdminPermissionAction.class)
@Breadcrumb(level = 4, name = "Create Ticket", context = HealthkartConstants.BreadcrumbContext.admin)
@Component
public class CreateTicketAction extends BaseAction {
    @Autowired
    TicketManager       ticketManager;
    @Autowired
    LinkManager         linkManager;
    @Autowired
    EmailManager        emailManager;
    @Autowired
    OrderDao            orderDao;
    @Autowired
    private UserService userService;

    @Validate(required = true, on = { "createOrderTrackingTicket", "createOrderRelatedTicket", "createPaymentTypeTicket" })
    private Order       order;

    @Validate(required = true, on = "createOrderTrackingTicket")
    private Courier     courier;

    @Validate(required = true, on = "createOrderTrackingTicket")
    private String      trackingId;

    @Validate(required = true, on = "createOrderTrackingTicket")
    private Address     address;

    @Validate(required = true, on = "createPaymentTypeTicket")
    private Date        paymentDate;

    @Validate(required = true, on = "createPaymentTypeTicket")
    private String      gatewayOrderId;

    private Date        shippedDate;
    private String      message;

    @ValidateNestedProperties( {
            @Validate(field = "shortDescription", required = true, maxlength = 200, on = "create"),
            @Validate(field = "owner", required = true, on = "create"),
            @Validate(field = "ticketStatus", required = true, on = "create"),
            @Validate(field = "ticketType", required = true, on = "create"),
            @Validate(field = "associatedEmail", converter = EmailTypeConverter.class, maxlength = 45),
            @Validate(field = "associatedPhone", maxlength = 20) })
    private TicketDto   ticketDto;

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/createTicket.jsp");
    }

    @ValidationMethod(on = "create")
    public void validate() {
        if (ticketDto.getAssociatedOrderId() != null) {
            Order associatedOrder = orderDao.get(Order.class, ticketDto.getAssociatedOrderId());
            if (associatedOrder == null) {
                addRedirectAlertMessage(new LocalizableMessage("/CreateTicket.action.order.not.found", ticketDto.getAssociatedOrderId()));
            } else {
                ticketDto.setAssociatedOrder(associatedOrder);
            }
        }
    }

    public Resolution create() {
        User reporter = getUserService().getUserById(getPrincipal().getId());
        ticketDto.setReporter(reporter);
        Ticket ticket = ticketManager.createTicket(ticketDto);
        addRedirectAlertMessage(new LocalizableMessage("/CreateTicket.action.created", linkManager.getViewTicketUrl(ticket)));
        emailManager.sendCreateTicketEmail(ticket);
        return new RedirectResolution(getPreviousBreadcrumb().getUrl(), false);
    }

    public Resolution createOrderTrackingTicket() {
        ticketDto = new TicketDto();
        ticketDto.setTicketType(getBaseDao().get(TicketType.class, EnumTicketType.Tracking.getId()));
        ticketDto.setOwner(getUserService().findByLogin(EnumTicketType.Tracking.getDefaultOwnerEmail()));
        ticketDto.setReporter(getUserService().getUserById(getPrincipal().getId()));
        ticketDto.setAssociatedOrder(order);
        ticketDto.setAssociatedOrderId(order.getId());
        ticketDto.setAssociatedEmail(order.getUser().getEmail());
        ticketDto.setAssociatedTrackingId(trackingId);
        ticketDto.setAssociatedCourier(courier);

        String shortDescription = "Tracking id - " + trackingId + ", " + address.getCity() + ", " + FormatUtils.getFormattedDateForUserEnd(shippedDate) + " not delivered";
        String fullDescription = "Address: \n" + address.getName() + "\n" + address.getLine1() + "\n" + address.getLine2() + "\n" + address.getCity() + " - " + address.getPincode()
                + "\n" + address.getState() + "\n" + "Ph. " + address.getPhone();

        ticketDto.setShortDescription(shortDescription);
        ticketDto.setFullDescription(fullDescription);

        return new ForwardResolution("/pages/admin/createTicket.jsp");
    }

    public Resolution createOrderRelatedTicket() {
        ticketDto = new TicketDto();
        ticketDto.setTicketType(getBaseDao().get(TicketType.class, EnumTicketType.Order_Related.getId()));
        ticketDto.setOwner(getUserService().findByLogin(EnumTicketType.Order_Related.getDefaultOwnerEmail()));
        ticketDto.setReporter(getUserService().getUserById(getPrincipal().getId()));
        ticketDto.setAssociatedOrder(order);
        ticketDto.setAssociatedOrderId(order.getId());
        ticketDto.setAssociatedEmail(order.getUser().getEmail());

        return new ForwardResolution("/pages/admin/createTicket.jsp");
    }

    public Resolution createPaymentTypeTicket() {

        ticketDto = new TicketDto();
        ticketDto.setTicketType(getBaseDao().get(TicketType.class,EnumTicketType.Payment.getId()));
        ticketDto.setOwner(getUserService().findByLogin(EnumTicketType.Payment.getDefaultOwnerEmail()));
        ticketDto.setReporter(getUserService().getUserById(getPrincipal().getId()));
        ticketDto.setAssociatedOrder(order);
        ticketDto.setAssociatedOrderId(order.getId());
        ticketDto.setAssociatedEmail(order.getUser().getEmail());

        String shortDescription = message + ", " + gatewayOrderId + ", Payment Date: " + FormatUtils.getFormattedDateForUserEnd(paymentDate);
        ticketDto.setShortDescription(shortDescription);

        return new ForwardResolution("/pages/admin/createTicket.jsp");
    }

    public TicketDto getTicketDto() {
        return ticketDto;
    }

    public void setTicketDto(TicketDto ticketDto) {
        this.ticketDto = ticketDto;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Date getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(Date shippedDate) {
        this.shippedDate = shippedDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getGatewayOrderId() {
        return gatewayOrderId;
    }

    public void setGatewayOrderId(String gatewayOrderId) {
        this.gatewayOrderId = gatewayOrderId;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
