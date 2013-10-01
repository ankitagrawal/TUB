package com.hk.pojo;

import com.hk.domain.shippingOrder.LineItem;

import java.util.List;
import java.util.Set;

/*
 * User: Pratham
 * Date: 26/09/13  Time: 02:09
*/
public class LedgerMap {


    String ledgerName;

    Set<LineItem> lineItems;

    Double amount;

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public Set<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(Set<LineItem> lineItems) {
        this.lineItems.addAll(lineItems);
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount += amount;
    }

    public LedgerMap(String ledgerName, Set<LineItem> lineItems, Double amount) {
        this.ledgerName = ledgerName;
        this.lineItems = lineItems;
        this.amount = amount;
    }
}
