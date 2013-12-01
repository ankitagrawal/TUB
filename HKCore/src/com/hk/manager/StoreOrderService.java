package com.hk.manager;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hk.constants.core.Keys;
import com.hk.domain.order.Order;

/**
 * Created with IntelliJ IDEA. User: Pradeep Date: 8/30/12 Time: 5:55 PM
 */
@Component
public class StoreOrderService {

    private static Logger logger = LoggerFactory.getLogger(StoreOrderService.class);

    public Order updateOrderStatusInStore(Order order) {
        try {
            ClientRequest request = new ClientRequest(order.getStore().getCallbackRestUrl() + "/order/update/" + order.getId() + "/status/" + order.getOrderStatus().getId());
            request.accept(MediaType.APPLICATION_JSON);

            logger.debug("trying to update orderstatus for store-" + order.getStore().getId() + "-order Id-" + order.getId());

            ClientResponse<String> response = request.get(String.class);

            if (response.getStatus() != 200) {
                logger.debug("failed to update order status in store-" + order.getStore().getId() + " some error -order Id-" + order.getId());
            }
            return order;

        } catch (IOException e) {
            logger.error("IO exception updating status in store", e);
        } catch (Exception e) {
            logger.error("exception updating status in store", e);
        }
        return order;
    }

}
