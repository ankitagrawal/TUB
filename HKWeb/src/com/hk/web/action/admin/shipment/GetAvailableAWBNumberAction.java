package com.hk.web.action.admin.shipment;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.admin.pact.dao.courier.AwbDao;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.user.UserDao;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.*;


@Secure(hasAnyPermissions = {PermissionConstants.SEARCH_ORDERS}, authActionBean = AdminPermissionAction.class)
@Component
public class GetAvailableAWBNumberAction extends BaseAction implements ValidationErrorHandler {
    @Autowired
    AwbService awbService;

    @Autowired
    UserDao userDao;

    @Validate(required = true)
    private Courier courier;

    @JsonHandler
    public Resolution pre() {
        User user = null;
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
        }
        Set<Warehouse> warehouses = user.getWarehouses();

        if (courier == null || warehouses == null || warehouses.isEmpty()) {
            HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "courier not set or is not warehouse admin");
            return new JsonResolution(healthkartResponse);
        }

        Map<String, Object> data = new HashMap<String, Object>(1);
        List<Awb> awbs = awbService.getUnusedAwbForCourierByWarehouseAndCod(courier, warehouses.iterator().next(),null);
        List<String> awbNumbers = new ArrayList<String>();
        for (Awb awb : awbs) {
            awbNumbers.add(awb.getAwbNumber());
        }
        HealthkartResponse healthkartResponse = null;
        if (awbs != null && !awbs.isEmpty()) {
            data.put("awbnumbers", awbNumbers);
            healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Got awb", data);
        } else {
            healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "AWB numbers exhausted");
        }
        return new JsonResolution(healthkartResponse);
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public Resolution handleValidationErrors(ValidationErrors validationErrors) throws Exception {
        return new JsonResolution(validationErrors, getContext().getLocale());
    }
}
