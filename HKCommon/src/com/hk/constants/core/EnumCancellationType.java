package com.hk.constants.core;

import com.hk.domain.core.CancellationType;
import com.hk.domain.courier.Courier;

import java.util.Arrays;
import java.util.List;


/**
 * Generated
 */
public enum EnumCancellationType {

    Payment_Not_Received(10L, "Payment Not Received"),
    Product_Not_Available(20L, "Product Not Available"),
    Shipment_Delay(30L, "Shipment Delay"),
    Delivery_Delay(40L, "Delivery Delay"),
    Customer_Not_Interested(50L, "Customer Not Interested"),
    COD_Cancellation(60L, "COD Cancellation"),
    Service_Not_Available(70L, "Service Not Available"),
    Other(100L, "Other"),

    Cod_Authorization_Failure(200L, "Cod_Authorization_Failure"),
    Online_Payment_Failure(210L, "Online_Payment_Failure"),
    Payment_Not_Received_Cheque_Cash_Neft(220L, "Payment_Not_Received_Cheque_Cash_Neft"),
    Customer_Request_Product_Change(230L, "Customer_Request_Product_Change"),
    Customer_Request_Not_Interested(240L, "Customer_Request_Not_Interested"),
    Customer_Request_Will_Place_New_Order(250L, "Customer_Request_Will_Place_New_Order"),
    Tech_Issue_Pricing_Mismatch(260L, "Tech_Issue_Pricing_Mismatch"),
    Tech_Issue_Bug(270L, "Tech_Issue_Bug"),
    Duplicate_Order(280L, "Duplicate_Order"),
    Test_Order(290L, "Test_Order");

    private String name;
    private Long id;

    EnumCancellationType(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public static List<CancellationType> getValidCancellationReasons() {
        return Arrays.asList(
                Cod_Authorization_Failure.asCancellationType(),
                Online_Payment_Failure.asCancellationType(),
                Payment_Not_Received_Cheque_Cash_Neft.asCancellationType(),
                Customer_Request_Product_Change.asCancellationType(),
                Customer_Request_Not_Interested.asCancellationType(),
                Customer_Request_Will_Place_New_Order.asCancellationType(),
                Tech_Issue_Pricing_Mismatch.asCancellationType(),
                Tech_Issue_Bug.asCancellationType(),
                Duplicate_Order.asCancellationType(),
                Test_Order.asCancellationType()
        );
    }

    public CancellationType asCancellationType() {
        CancellationType cancellationType = new CancellationType();
        cancellationType.setId(this.id);
        cancellationType.setName(this.name);
        return cancellationType;
    }


    public Courier asCourier() {
        Courier courier = new Courier();
        courier.setId(this.id);
        courier.setName(this.name);
        return courier;
    }
}