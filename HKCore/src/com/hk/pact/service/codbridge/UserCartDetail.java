package com.hk.pact.service.codbridge;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 2/19/13
 * Time: 1:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserCartDetail implements Serializable {

    Long orderId;

    List<ProductVariant> productList;

    public class ProductVariant {
        String id;
        String name;
        Double price;
        Long qty;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Long getQty() {
            return qty;
        }

        public void setQty(Long qty) {
            this.qty = qty;
        }
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setProductList(List<ProductVariant> productList) {
        this.productList = productList;
    }
}
