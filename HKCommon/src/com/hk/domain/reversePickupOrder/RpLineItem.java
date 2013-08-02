package com.hk.domain.reversePickupOrder;

import com.hk.domain.analytics.Reason;
import com.hk.domain.shippingOrder.LineItem;
import com.itextpdf.text.pdf.LongHashtable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 7/18/13
 * Time: 6:11 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "rp_line_item")
public class RpLineItem implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reverse_pickup_id", nullable = false)
    private ReversePickupOrder reversePickupOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_item_id", nullable = false)
    private LineItem lineItem;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_reason_for_return_id")
    private Reason customerReasonForReturn;

    @Column(name = "customer_comment", length = 100)
    private String customerComment;

    @Column(name = "action_taken_id")
    private Long actionTaken;

    @Column(name = "action_on_status_id")
    private Long actionTakenOnStatus;

    @Column(name = "warehouse_remark", length = 100)
    private String warehouseRemark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_condition_id")
    private Reason warehouseReceivedCondition;

    @Column(name = "customer_action_status")
    private Long customerActionStatus;

    @Column(name = "warehouse_comment")
    private Long warehouseComment;

    @Transient
    private String    itemBarcode;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReversePickupOrder getReversePickupOrder() {
        return reversePickupOrder;
    }

    public void setReversePickupOrder(ReversePickupOrder reversePickupOrder) {
        this.reversePickupOrder = reversePickupOrder;
    }

    public LineItem getLineItem() {
        return lineItem;
    }

    public void setLineItem(LineItem lineItem) {
        this.lineItem = lineItem;
    }

    public Reason getCustomerReasonForReturn() {
        return customerReasonForReturn;
    }

    public void setCustomerReasonForReturn(Reason customerReasonForReturn) {
        this.customerReasonForReturn = customerReasonForReturn;
    }

    public String getCustomerComment() {
        return customerComment;
    }

    public void setCustomerComment(String customerComment) {
        this.customerComment = customerComment;
    }

    public Long getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(Long actionTaken) {
        this.actionTaken = actionTaken;
    }

    public Long getActionTakenOnStatus() {
        return actionTakenOnStatus;
    }

    public void setActionTakenOnStatus(Long actionTakenOnStatus) {
        this.actionTakenOnStatus = actionTakenOnStatus;
    }

    public String getWarehouseRemark() {
        return warehouseRemark;
    }

    public void setWarehouseRemark(String warehouseRemark) {
        this.warehouseRemark = warehouseRemark;
    }

    public Reason getWarehouseReceivedCondition() {
        return warehouseReceivedCondition;
    }

    public void setWarehouseReceivedCondition(Reason warehouseReceivedCondition) {
        this.warehouseReceivedCondition = warehouseReceivedCondition;
    }

    public Long getCustomerActionStatus() {
        return customerActionStatus;
    }

    public void setCustomerActionStatus(Long customerActionStatus) {
        this.customerActionStatus = customerActionStatus;
    }

    public Long getWarehouseComment() {
        return warehouseComment;
    }

    public void setWarehouseComment(Long warehouseComment) {
        this.warehouseComment = warehouseComment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof RpLineItem))
            return false;

        RpLineItem rpLineItem = (RpLineItem) o;

        if (this.id != null && rpLineItem.getId() != null) {
            return id.equals(rpLineItem.getId());
        }
        return false;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }
}
