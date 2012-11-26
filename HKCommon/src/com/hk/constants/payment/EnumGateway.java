package com.hk.constants.payment;

import com.hk.domain.payment.Gateway;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: 11/26/12
 * Time: 1:14 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumGateway {

    TECHPROCESS(1L, "Techprocess", 0.021D),
    CITRUS(10L, "Citrus", 0.0215D),
    ICICI(15L, "Icici", 0.019D),
    PAYPAL(5L, "Paypal", 0.039D),
    EBS(30L, "Ebs", 0.02D),
    CCAVENUE_DUMMY(25L, "CCAvenue Dummy", 0.0D);

    private java.lang.String name;
    private java.lang.Long id;
    private Double reconciliationCharges;

    EnumGateway(java.lang.Long id, java.lang.String name, Double reconciliationCharges) {
        this.name = name;
        this.id = id;
        this.reconciliationCharges = reconciliationCharges;
    }

    public static EnumGateway getGateway(Gateway gateway) {
        if (gateway != null) {
            for (EnumGateway enumGateway : values()) {
                if (enumGateway.getId().equals(gateway.getId())) {
                    return enumGateway;
                }
            }
        }
        return null;
    }


    public java.lang.String getName() {
        return name;
    }

    public java.lang.Long getId() {
        return id;
    }

    public Double getReconciliationCharges() {
        return reconciliationCharges;
    }

    public Gateway asGateway() {
        Gateway gateway = new Gateway();
        gateway.setId(id);
        gateway.setName(name);
        return gateway;
    }

    public static EnumGateway getGatewayFromId(Long id) {
        for (EnumGateway gateway : values()) {
            if (gateway.getId().equals(id)) return gateway;
        }
        return null;
    }


    public static List<Long> getPaymentModeIDs(List<EnumPaymentMode> enumPaymentModes) {
        List<Long> paymenLModeIds = new ArrayList<Long>();
        for (EnumPaymentMode enumPaymentMode : enumPaymentModes) {
            paymenLModeIds.add(enumPaymentMode.getId());
        }
        return paymenLModeIds;
    }
}


