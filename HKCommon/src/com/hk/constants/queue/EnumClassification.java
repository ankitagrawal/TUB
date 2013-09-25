package com.hk.constants.queue;

import com.hk.domain.queue.Classification;

/*
 * User: Pratham
 * Date: 15/04/13  Time: 16:23
*/
public enum EnumClassification {

    BASKET_CATEGORY_HOME_LIVING(1L, "Basket Category", "HOME_LIVING"),
    BASKET_CATEGORY_NUTRITION(5L, "Basket Category", "Nutrition"),
    BASKET_CATEGORY_PARENTING(10L, "Basket Category", "BABY"),
    BASKET_CATEGORY_BEAUTY(20L, "Basket Category", "BEAUTY"),
    BASKET_CATEGORY_DIABETES(30L, "Basket Category", "DIABETES"),
    BASKET_CATEGORY_EYE(40L, "Basket Category", "EYE"),
    BASKET_CATEGORY_HEALTH_DEVICES(50L, "Basket Category", "HEALTH_DEVICES"),
    BASKET_CATEGORY_PERSONAL_CARE(60L, "Basket Category", "PERSONAL_CARE"),
    BASKET_CATEGORY_SERVICES(70L, "Basket Category", "SERVICES"),
    BASKET_CATEGORY_SPORTS(80L, "Basket Category", "SPORTS"),
    BASKET_CATEGORY_HEALTH_NUTRITION(90L, "Basket Category", "HEALTH_NUTRITION"),
    BASKET_CATEGORY_SPORTS_NUTRITION(100L, "Basket Category", "SPORTS_NUTRITION"),
    Cheque_Cash_Neft(110L, "Cheque/Cash/Neft", "Authorization Pending"),
    COD(210L, "COD", "Authorization Pending"),
    Online_Payment_Disputes(120L, "Online Payment Disputes", "Auth_Pending/Error"),
    JIT(220L, "JIT", "JIT"),
    DropShip(230L, "JIT", "DropShip"),
    Services(240L, "JIT", "Services"),
    Dispatch_Issues(310L, "Dispatch Issue", "Shipment Not Created"),
    Warehouse(410L, "Warehouse Processing" , "In Process"),
    AD_HOC(460L, "AD_HOC" , "Pending with CC"),
    Customer_Service(480L, "Customer_Service" , "Pending with CC"),
    Tech_Support(490L, "Tech_Support" , "Bug"),
    B2B(500L, "B2B" , "Bright"),
    Vendor(510L, "Vendor", "Procurement"),
    Logistics(520L, "Logistics", "Pickup/Delivery"),
    Receiving(530L, "Receiving", "GRN"),
    ProductDamaged(540L, "Product Damaged", "Reverse Pickup Product Damaged "),
    ProductExpired(550L, "Product Expired", "Reverse Pickup Product Expired "),
    WrongColor(560L, "Wrong Color", "Reverse Pickup Wrong Color"),

    CUSTOMER_REQUEST_PRODUCT_CHANGE(610L, "Customer Request", "Product Change"),
    CUSTOMER_REQUEST_CANCEL(620L, "Customer Request", "Cancellation"),
    CUSTOMER_REQUEST_ON_HOLD(630L, "Customer Request", "Put on Hold"),
    PRODUCT_NOT_AVAILABLE_DAMAGE(640L, "Product Not Available", "Damaged"),
    PRODUCT_NOT_AVAILABLE_EXPIRE(650L, "Product Not Available", "Expired"),
    PRODUCT_NOT_AVAILABLE_INV_MISMATCH(660L, "Product Not Available", "Inventory Mismatch"),
    MRP_MISMATCH_CHEAPER(670L, "Mrp Mismatch", "Invoice MRP > Product MRP"),
    MRP_MISMATCH_DEARER(680L, "Mrp Mismatch", "Invoice MRP < 50% Product MRP && Diff > 1000"),
    DISPATCH_ISSUE_COURIER_CHANGE(690L, "Dispatch Issue", "Courier service changed"),
    INV_PRESENT_DIFF_WAREHOUSE(1560L, "SO Not cancelled automatically", "Inventory found in other warehouse"),
    JIT_IN_SO(1570L, "SO Not cancelled automatically", "No inventory only for JIT items"),

    Refund_In_Cash(2110l,"Refund in Cash", "Reconcile"),
    Refund_Reward_Points(2120l,"Refund Reward Points", "Reconcile"),
    Replacement_Order(2130l,"Replacement Order", "Reconcile"),
    Decide_Later(2140l,"Decide Later", "Reconcile"),
    Pending_Approval(2010l,"Pending Approval", "Approval"),
    Approved(2020l,"Approved", "Approval"),
    Force_Approval(2030l,"Force Approval", "Approval"),

    ;


  private Long id;
    private String primary;
    private String secondary;

    EnumClassification(Long id, String primary, String secondary) {
        this.id = id;
        this.primary = primary;
        this.secondary = secondary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getSecondary() {
        return secondary;
    }

    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }

    public Classification asClassification() {
        Classification classification = new Classification();
        classification.setId(id);
        classification.setPrimary(primary);
        classification.setSecondary(secondary);
        return classification;
    }
}
