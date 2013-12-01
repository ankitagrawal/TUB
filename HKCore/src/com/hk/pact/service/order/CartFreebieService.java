package com.hk.pact.service.order;

import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;

public interface CartFreebieService {

    
    public String getFreebieBanner(Order order) ;
    public String getFreebieItem(ShippingOrder order) ;
}
