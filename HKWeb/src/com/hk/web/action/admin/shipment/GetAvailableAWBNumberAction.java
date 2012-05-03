package com.hk.web.action.admin.shipment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;

import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;


import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.admin.impl.dao.courier.AwbDao;
import com.hk.constants.core.PermissionConstants;
import com.hk.dao.user.UserDaoImpl;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.error.AdminPermissionAction;


@Secure(hasAnyPermissions = {PermissionConstants.SEARCH_ORDERS}, authActionBean = AdminPermissionAction.class)
@Component
public class GetAvailableAWBNumberAction extends BaseAction implements ValidationErrorHandler {
    
    AwbDao awbDao;

    
    UserDaoImpl userDao;

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
        List<Awb> awbs = awbDao.getAvailableAwbForCourier(courier, warehouses.iterator().next());
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
