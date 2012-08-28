package com.hk.web.action.core.payment;

import com.akube.framework.stripes.action.BaseAction;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 8/27/12
 * Time: 6:05 PM
 * To change this template use File | Settings | File Templates.
 */

/*
  parametrized read encrypted gateway order id from url

  the payment amount would again be precomputed using pricing engine, good thing is pricing engine doesn't apply cod charges
  whereas the shipping charges will be as applicable

  read encrypted offerID from url

  steps involved, if all is well, create a payment and mark it as successful  --> like always

  then for the previous payment if any, mark the payment status as Retried for Prepay  --> no need to

  change the payment id for the corresponding order     --> automatically plan done

  no need to change the order status, but at the final step it should not be in cart.   --> done

  remove cod cart_line_item if any, and nullify cod charges on line_item        --> done

 */


public class RegisterOnlinePaymentAction extends BaseAction {


}
