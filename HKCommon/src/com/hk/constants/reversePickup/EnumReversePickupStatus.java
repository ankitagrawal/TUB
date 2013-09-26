package com.hk.constants.reversePickup;

import com.hk.domain.reversePickupOrder.ReversePickupStatus;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 7/19/13
 * Time: 2:42 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumReversePickupStatus {

    RPU_Initiated(10L, "RPU Initiated"),
    RPU_Picked(20L, "RPU Picked"),
    RPU_Received(30L, "RPU Received"),
    RPU_QC_Checked_In(40L, "RPU_QC_Checked_In"),
    /* Status If Return By Customer*/
    Return_Initiated(50L, "Return Initiated"),
    Return_Received(60L, "Return Received"),
    Return_QC_Checkin(70L, "Return QC checkin"),
    RPU_Scheduled(80L, "RPU_Scheduled"),
    RPU_CANCEL(90L, "RPU Cancel"),
    RPU_CLOSED(100L, "RPU Closed"),
    RPU_RECONCILATION(110L,"RPU Reconcilation"),
    RPU_NOTAVAILABLE(120L, "RPU Not Available "),
    RPU_APPROVED(130L,"RPU Approved");
    private Long id;
    private String status;

    EnumReversePickupStatus(Long id, String actionTaken) {
        this.id = id;
        status = actionTaken;
    }

    public static ReversePickupStatus getAsReversePickupStatus(EnumReversePickupStatus enumReversePickupStatus) {
        ReversePickupStatus reversePickupStatus = new ReversePickupStatus();
        reversePickupStatus.setId(enumReversePickupStatus.getId());
        reversePickupStatus.setStatus(enumReversePickupStatus.getStatus());
        return reversePickupStatus;
    }


    public static List<ReversePickupStatus> getHealthKartManagedRPStatus() {
        return Arrays.asList(RPU_Initiated.asReversePickupStatus(),
                RPU_Picked.asReversePickupStatus(),
                RPU_Received.asReversePickupStatus(),
                RPU_CANCEL.asReversePickupStatus());
    }

    public ReversePickupStatus asReversePickupStatus() {
        ReversePickupStatus reversePickupStatus = new ReversePickupStatus();
        reversePickupStatus.setId(this.getId());
        reversePickupStatus.setStatus(this.getStatus());
        return reversePickupStatus;
    }

    public static List<ReversePickupStatus> getPreRPStatusList(){
        return Arrays.asList(
                RPU_Initiated.asReversePickupStatus(),
                RPU_Picked.asReversePickupStatus(),
                RPU_Received.asReversePickupStatus(),
                RPU_Scheduled.asReversePickupStatus(),
                Return_QC_Checkin.asReversePickupStatus(),
                RPU_RECONCILATION.asReversePickupStatus(),
                RPU_APPROVED.asReversePickupStatus());
    }

    public static List<ReversePickupStatus> getSearchRPStatusList() {
        return Arrays.asList(
                RPU_Initiated.asReversePickupStatus(),
                RPU_Picked.asReversePickupStatus(),
                RPU_Scheduled.asReversePickupStatus(),
                RPU_QC_Checked_In.asReversePickupStatus(),
                Return_Initiated.asReversePickupStatus(),
                RPU_CLOSED.asReversePickupStatus(),
                RPU_CANCEL.asReversePickupStatus(),
                RPU_NOTAVAILABLE.asReversePickupStatus(),
                Return_QC_Checkin.asReversePickupStatus(),
                RPU_RECONCILATION.asReversePickupStatus(),
                RPU_APPROVED.asReversePickupStatus());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
