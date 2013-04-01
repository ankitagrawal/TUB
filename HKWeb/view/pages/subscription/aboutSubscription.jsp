<%--
  Created by IntelliJ IDEA.
  User: Pradeep
  Date: 7/25/12
  Time: 11:19 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.subscription.AboutSubscriptionAction" var="aba"/>
<s:layout-render name="/layouts/default.jsp" pageTitle="Subscriptions">

    <s:layout-component name="htmlHead">

    </s:layout-component>
    <s:layout-component name="heading">Subscriptions</s:layout-component>
    <s:layout-component name="centralContent">
        <script type="text/javascript">
            $(document).ready(function() {
                $('.faqs h3').each(function() {
                    var tis = $(this), state = false, answer = tis.next('div').hide().css('height','auto').slideUp();
                    tis.click(function() {
                        state = !state;
                        answer.slideToggle(state);
                        tis.toggleClass('active',state);
                    });
                });
            });
        </script>
        <div>
            <p>
            We know it is hard to manage products which you require in continuous supply throughout the year.
            <b><strong>HealthKart Subscriptions</strong></b> is a feature which makes life easy for you.
            </p>
        </div>
        <img src="../../images/banners/Subscription-banner.jpg" alt="subscription banner">

        <div style="font-size: 13px;">

            <br/>
            <h4>How does it work?</h4>
        <p> You select a product and choose a plan specifying your delivery frequency; you get additional discounts based on the plan you choose.
            After finalizing your product and plan, you have to prepay the whole amount for your subscription. When the delivery date arrives, our Customer Care Executive will give you a reminder call and will ensure a safe and sound delivery of your specified products.
          </p>
            <p>
                You can notify our customer care in case you want to change your delivery date or your shipment address, according to your convenience.  The customer can drop in an email to <strong>info@healthkart.com</strong> or can call our Customer Care at <strong>0124-4616444</strong> notifying us about the new address of delivery.
        </p>

           <p></p>

            <h4>Benifits of subscription</h4>
            <br/>
            <strong>Additional Discount</strong>
            <p>You get an additional discount which may vary from 4-10% on the subscribed products according to your subscription plan.</p>
            <strong> Price Protection</strong>
            <p>Prices of nutrition products may vary with time. In a situation of increase of the price of the product, the customer will receive the product at subscription price. In case, the price of the product goes down, the customer gets reward points after </p>
            <strong>Additional Freebies</strong>
            <p>You will be entitled to avail additional freebies as and when applicable to your subscribed products.</p>
            </ul>

            <h4>Cancellation </h4>
            <p>
            A customer is free to cancel his subscription anytime. If a customer opts out, the discounts and benifits that he/she received  as part of the subscription will be added to the cost of
            the shipments delivered till that point and the balance amount would be credited as reward points.
                </p>
            <br/><br/>
           Please refer to the following Frequently Asked Question in case of any questions. If you still have unanswered questions, please contact our customer care through email
            or a phone call.

        </div>
        <br/><br/>

        <div id="faqs" class="faqs">
            <h4>Frequently Asked Questions</h4><br/>
            <h3>How can I buy a product subscription?</h3>
            <div>
                <p>
                    You can buy product subscriptions only for products that are subscribable. You can know that a product is subscribable in two ways:
                <ul >
                    <li>
                        In case of subscribable products, you will be given an option to subscribe from the product page itself.  <strong>or</strong>
                    </li>
                    <li>
                        You will be given an option to subscribe for a product in the cart page if that product is subscribable.
                    </li>

                </ul>
                </p>
            </div>
            <h3>Is CashOnDelivery available for subscriptions?</h3>
            <div>
                <p>No. You have to prepay the whole amount if you have subscribed for the product in your cart.</p>
            </div>
            <h3>Why aren't discount coupons working for my subscription?</h3>
            <div>
                <p>You won't get any additional discounts apart from the subscription discount. So, coupons won't work for subscriptions.</p>
            </div>
            <h3>What if my address changes?</h3>
            <div>
                <p>In case of a change of shipping address, the customer can drop in an email to info@healthkart.com or a call our customer care at 0124-4616444 notifying us about the new address of delivery.</p>
            </div>
            <h3>What if I go on a vacation?</h3>
            <div>
                <p>Send in a request for a change in the shipping date. Please notify our customer care when you receive a reminder call about your order or you can drop an e-mail to info@healthkart.com or you can call our customer care.</p>
            </div>
            <h3>Can I change my next shipment date?</h3>
            <div>
                <p>Yes, you can. You can notify our customer care when you receive a reminder call about your order or you can drop an email to info@healthkart.com or you can call our customer care at 0124-4616444.</p>
            </div>
            <h3>Can I change the product during subscription?</h3>
            <div>
                <p>No. As of now, you can't.</p>
            </div>
            <h3>Can I cancel my subscription midway?</h3>
            <div>
                <p>A customer is free to cancel his subscription anytime. If a customer opts out, the discounts and benefits will be added to the cost of the shipments delivered till that point. The balance would be credited as reward points.
                </p>
            </div>
            <h3>Where can I see my subscriptions and shipment details?</h3>
            <div>
                <p>If you have subscribed to any product, go to your account and you can see your subscriptions under the subscriptions tab.
                    More over, we will send you an email when ever a subscription order is shipped.
                </p>
            </div>
            <br/><br/>
            <br/><br/>

        </div>

    </s:layout-component>

</s:layout-render>

</script>