package com.hk.manager;

import com.akube.framework.gson.JsonUtils;
import com.akube.framework.util.BaseUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hk.constants.core.Keys;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.payment.Payment;
import com.hk.domain.store.StoreOrder;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.store.StoreService;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 8/30/12
 * Time: 5:55 PM
 */
@Component
public class StoreOrderService {


    private static Logger logger = LoggerFactory.getLogger(StoreOrderService.class);

    public Order updateOrderStatusInStore(Order order){
        try {
            ClientRequest request = new ClientRequest(
                    BaseUtils.getPropertyValue(Keys.Env.mihRestUrl)+"/order/update/"+order.getId()+"/status/"+order.getOrderStatus().getId());
            request.accept(MediaType.APPLICATION_JSON);

            logger.debug("trying to update orderstatus for store-"+order.getStore().getId()+"-order Id-"+order.getId());

            ClientResponse<String> response = request.post(String.class);

            if (response.getStatus() != 200) {
                logger.debug("failed to update order status in store-"+order.getStore().getId()+" some error -order Id-"+order.getId());
            }
            return order;

        } catch (IOException e) {

            e.printStackTrace();

        } catch (Exception e) {

            e.printStackTrace();

        }
        return order;
    }

}
