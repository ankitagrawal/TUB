package com.hk.web.action.core.user;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import com.hk.domain.user.User;
import com.hk.domain.user.Address;
import com.hk.domain.user.BillingAddress;
import com.hk.domain.order.Order;
//import com.hk.domain.order.OrderBillingAddress;
import com.hk.pact.service.core.AddressService;
import com.hk.pact.service.UserService;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.core.AddressDao;
import com.hk.manager.OrderManager;
import com.hk.web.action.core.payment.PaymentModeAction;
import com.akube.framework.stripes.action.BaseAction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Nov 8, 2012
 * Time: 1:31:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class BillingAddressAction extends BaseAction {

    private BillingAddress billingAddress;
    @Autowired
    AddressDao addressDao;

    Order order;


    public Resolution pre() {
//           User user = getUserService().getUserById(getPrincipal().getId());
//           email = user.getEmail();
//
//           addresses = addressDao.getVisibleAddresses(user);
//           Order order = orderManager.getOrCreateOrder(user);
//           selectedAddress = order.getAddress();
//           if (selectedAddress == null) {
//               // get the last order address? for not selecting just first non deleted one.
//               if (addresses != null && addresses.size() > 0) {
//                   selectedAddress = addresses.get(0);
//               }
//           }

        return new ForwardResolution("/pages/billingaddressbook.jsp");

    }


    public Resolution save() {
        User user = getUserService().getUserById(getPrincipal().getId());
         BillingAddress billingAddress =  addressDao.searchBillingAddress(user);
        if (billingAddress == null){
              billingAddress = new BillingAddress();
        }
              billingAddress.setUser(user);
              billingAddress.getOrders().add(order);
              addressDao.save(billingAddress);
//        }else {
//            billingAddress.getOrders().add(order);
//        }


//        OrderBillingAddress orderBillingAddress = addressDao.orderAlreadywithBillingAddress(order.getId());
//        if (orderBillingAddress == null) {
//            billingAddress.getOrders().add(order);
//        } else {
//            orderBillingAddress.setBillingAddress(billingAddress);
//        }

        return new RedirectResolution(PaymentModeAction.class);
    }


    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

    public AddressDao getAddressDao() {
        return addressDao;
    }

    public void setAddressDao(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    public void setBillingAddress(BillingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
