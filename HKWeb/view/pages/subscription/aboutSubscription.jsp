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
        <%
            if(aba.isFaqs()){
        %>
        <p style="text-align:center;font-size: 14px;">
            <s:link beanclass="com.hk.web.action.core.subscription.AboutSubscriptionAction" event="pre" style="font-size:14px;">About Subscriptions</s:link> |
           <b><strong> FAQs </strong></b>
        </p>
        <%
            }else{
        %>
        <p style="text-align:center;font-size: 14px;">
            <b><strong> About Subscriptions </strong></b> | <s:link beanclass="com.hk.web.action.core.subscription.AboutSubscriptionAction" event="faqs" style="font-size:14px;">FAQs</s:link>

        </p>
        <%
            }
        %>

        <%
            if(aba.isFaqs()){
        %>
        <div class="faqs">
            <h3>How can I buy a product subscription?</h3>
            <div>
                <p>
                    You can buy product subscriptions only for products that are subscribable. You can know that a product is subscribable in two ways:
                <ul >
                <li>
                    In case of subscribable products, you will be given an option to subscribe from the product page itself.  or
                </li>
                    <li>
                        You will be given an option to subscribe for a product in the cart page if that product is subscribable.
                    </li>

                </ul>
                </p>
            </div>
            <h3>Is CashOnDelivery available for subscriptions?</h3>
            <div>
                <p>No. You have to prepay the whole amount if you have subscriptions in your cart.</p>
            </div>
            <h3>Why aren't discount coupons working for my subscription?</h3>
            <div>
                <p>You won't get any additional discounts apart from the subscription discount. So, coupons won't work for subscriptions.</p>
            </div>
            <h3>What if my address changes?</h3>
            <div>
                <p>In case of a change of shipping address, the customer can drop in an e-mail to info@healthkart.com or a call our customer care
                    notifying us about the new address of delivery.</p>
            </div>
            <h3>What if I go on a vacation?</h3>
            <div>
                <p>Just request for a change in shipment date. Please notify our customer care when you receive a reminder call about your order or
                    you can can drop an e-mail to info@healthkart.com or you can call our customer care.</p>
            </div>
            <h3>Can I change my next shipment date?</h3>
            <div>
                <p>Yes, you can. You can notify our customer care when you receive a reminder call about your order or
                    you can can drop an e-mail to info@healthkart.com or you can call our customer care.</p>
            </div>
            <h3>Can I change the product during subscription?</h3>
            <div>
                <p>No. As of now, you can't.</p>
            </div>
            <h3>Can I cancel my subscription midway?</h3>
            <div>
                <p>You are free to cancel your subscription anytime. If a customer opts out, the discounts and benifits that you received  as part of the subscription will be added to the cost of
                    the shipments delivered till that point and the balance amount would be credited as reward points or refunded as per your preference.
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
        <%
            }else{
        %>
        <div style="font-size: 13px;">

            <h4>How does it work?</h4>
        <p>You select a product and subscribe for a plan specifying your frequency. You have to prepay the amount. When
            the due date arrives, we will give you a reminder call and will send you the shipment.
        </p>

           <p></p>

            <h4>Benifits of subscription</h4>
            <br/>
            <strong>Additional Discount</strong>
            <p>You get additinal 4-10% discount on the subscription products according to your subscription plan. </p>
            <strong> Price Protection</strong>
            <p>Prices of nutrition products vary with time. </p>
            <strong>Additional Freebies</strong>
            <p>You will be entitled to additional freebies as and when applicable to your subscription product.</p>
            </ul>

            <h4>Cancellation </h4>
            <p>
            A customer is free to cancel his subscription anytime. If a customer opts out, the discounts and benifits that he/she received  as part of the subscription will be added to the cost of
            the shipments delivered till that point and the balance amount would be credited as reward points or refunded as per customer preference.
                </p>
            <br/><br/>
           Please refer to the Frequently Asked Questions(<s:link beanclass="com.hk.web.action.core.subscription.AboutSubscriptionAction" event="faqs" style="font-size:14px;">FAQs</s:link>) in case of any questions. If you still have unanswered questions, please contact our customer care through email
            or a phone call.

        </div>
        <br/><br/>
        <%
            }
        %>


    </s:layout-component>

</s:layout-render>

</script>