package com.hk.pact.service.codbridge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 2/19/13
 * Time: 1:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class EffortBpoOrderJson {

    String orderID;

    List<ProductVariant> productList;


    public class ProductVariant {
        String productVariantId;
        String productName;
        String price;
        int qty;

        public String getProductVariantId() {
            return productVariantId;
        }

        public void setProductVariantId(String productVariantId) {
            this.productVariantId = productVariantId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }


    public List<ProductVariant> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductVariant> productList) {
        this.productList = productList;
    }
}
