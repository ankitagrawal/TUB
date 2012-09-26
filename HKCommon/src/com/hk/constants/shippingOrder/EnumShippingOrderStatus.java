package com.hk.constants.shippingOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hk.domain.order.ShippingOrderStatus;


public enum EnumShippingOrderStatus {

    SO_ActionAwaiting(110L, "SO Action Awaiting"),     //when shipping order could not be auto escalated
    SO_OnHold(112L, "SO On Hold"),
    SO_EscalatedBack(115L, "SO Escalated Back"),
    SO_ReadyForProcess(120L, "SO Ready for Process"),     //same as escalated to packing queue
    SO_MarkedForPrinting(130L, "SO Gone for Printing"),
    SO_Picking(140L, "SO Picking"),
    SO_CheckedOut(150L, "SO Checked Out"),
    SO_Packed(160L, "SO Packed"),
    SO_Shipped(180L, "SO Shipped"),
    SO_Delivered(190L, "SO Delivered"),
    SO_Returned(200L, "SO Returned"),
    SO_Lost(210L, "SO Lost"),
    SO_Replaced(220L, "SO Replaced"),
    SO_Cancelled(999L, "SO Cancelled"),
    RTO_Initiated(230L, "RTO Initiated");


    private java.lang.String name;

    private java.lang.Long id;

    EnumShippingOrderStatus(Long id, java.lang.String name) {
        this.name = name;
        this.id = id;
    }

    public java.lang.String getName() {
        return name;
    }

    public java.lang.Long getId() {
        return id;
    }

    public ShippingOrderStatus asShippingOrderStatus() {
        ShippingOrderStatus shippingOrderStatus = new ShippingOrderStatus();
        shippingOrderStatus.setId(this.getId());
        shippingOrderStatus.setName(this.getName());
        return shippingOrderStatus;
    }

    public static List<Long> getShippingOrderStatusIDs(List<EnumShippingOrderStatus> enumShippingOrderStatuses) {
        List<Long> shippingOrderStatusIds = new ArrayList<Long>();
        for (EnumShippingOrderStatus enumOrderStatus : enumShippingOrderStatuses) {
            shippingOrderStatusIds.add(enumOrderStatus.getId());
        }
        return shippingOrderStatusIds;
    }

    public static List<EnumShippingOrderStatus> getStatusForProcessingQueue() {
        return Arrays.asList(EnumShippingOrderStatus.SO_ReadyForProcess,
                EnumShippingOrderStatus.SO_MarkedForPrinting, EnumShippingOrderStatus.SO_Picking, EnumShippingOrderStatus.SO_CheckedOut);
    }

    public static List<EnumShippingOrderStatus> getStatusForBookedInventory() {
        return Arrays.asList(EnumShippingOrderStatus.SO_ActionAwaiting,
                EnumShippingOrderStatus.SO_OnHold,
                EnumShippingOrderStatus.SO_ReadyForProcess,
                EnumShippingOrderStatus.SO_MarkedForPrinting,
                EnumShippingOrderStatus.SO_Picking,
                EnumShippingOrderStatus.SO_EscalatedBack);
    }


    public static List<EnumShippingOrderStatus> getStatusForPuttingOrderOnHold() {

        return Arrays.asList(EnumShippingOrderStatus.SO_ActionAwaiting,
                EnumShippingOrderStatus.SO_ReadyForProcess,
                EnumShippingOrderStatus.SO_MarkedForPrinting,
                EnumShippingOrderStatus.SO_Picking,
                EnumShippingOrderStatus.SO_CheckedOut,
                EnumShippingOrderStatus.SO_Packed);
    }


    public static List<EnumShippingOrderStatus> getStatusForActionQueue() {
        return Arrays.asList(EnumShippingOrderStatus.SO_ActionAwaiting, EnumShippingOrderStatus.SO_OnHold);//, EnumShippingOrderStatus.SO_EscalatedBack);
    }

    public static List<EnumShippingOrderStatus> getStatusForPicking() {
        return Arrays.asList(EnumShippingOrderStatus.SO_MarkedForPrinting);
    }

    public static List<EnumShippingOrderStatus> getStatusForPrinting() {
        return Arrays.asList(EnumShippingOrderStatus.SO_ReadyForProcess);
    }

    public static List<EnumShippingOrderStatus> getStatusForShipmentAwaiting() {
        return Arrays.asList(EnumShippingOrderStatus.SO_Packed);
    }

    public static List<EnumShippingOrderStatus> getStatusForDeliveryAwaiting() {
        return Arrays.asList(EnumShippingOrderStatus.SO_Shipped);
    }

    public static List<EnumShippingOrderStatus> getStatusSearchingInDeliveryQueue() {
        return Arrays.asList(EnumShippingOrderStatus.SO_Shipped, EnumShippingOrderStatus.SO_Returned, EnumShippingOrderStatus.SO_Lost);
    }

    public static List<EnumShippingOrderStatus> getStatusForCRMReport() {

        return Arrays.asList(EnumShippingOrderStatus.SO_Packed,
                EnumShippingOrderStatus.SO_Shipped,
                EnumShippingOrderStatus.SO_Delivered);
    }

    public static List<ShippingOrderStatus> getStatusForChangingShipmentDetails() {
        return Arrays.asList(EnumShippingOrderStatus.SO_Shipped.asShippingOrderStatus(),
                EnumShippingOrderStatus.SO_Delivered.asShippingOrderStatus(),
                EnumShippingOrderStatus.SO_Lost.asShippingOrderStatus());
    }

    public static List<ShippingOrderStatus> getStatusForReconcilationReport() {
        return Arrays.asList(EnumShippingOrderStatus.SO_Shipped.asShippingOrderStatus(),
                EnumShippingOrderStatus.SO_Delivered.asShippingOrderStatus(),
                EnumShippingOrderStatus.SO_Returned.asShippingOrderStatus(),
                EnumShippingOrderStatus.SO_Lost.asShippingOrderStatus());
    }

    public static List<Long> getStatusForSearchOrderAndEnterCourierInfo() {
           return Arrays.asList(EnumShippingOrderStatus.SO_Packed.asShippingOrderStatus().getId(),
                   EnumShippingOrderStatus.SO_CheckedOut.asShippingOrderStatus().getId());
       }


}
