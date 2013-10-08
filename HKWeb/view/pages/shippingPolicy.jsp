<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultG.jsp" pageTitle="Shipping Policy">
<s:layout-component name="heading">Shipping Policy</s:layout-component>
<s:layout-component name="lhsContent">
  <jsp:include page="about-nav.jsp"/>
</s:layout-component>
<s:layout-component name="htmlHead">
  <style type="text/css">
   .tncNavUL{list-style: none; line-height: 25px;padding-left: 0;}
   .tncContent {list-style-type: circle; padding-left: 0;}
   .tncNavUL li, .tncContent li {margin-left: 20px;}
  </style>
</s:layout-component>
<s:layout-component name="rhsContent">
<div class="grid_18">


<b>1. How do you ship your orders?</b><br>
<p style="text-align: justify">
  We ship certain heavy items such as treadmills, dumbbells etc. by ground couriers while other light items are shipped
  by air. We deliver most of our orders through our own delivery service – Healthkart Reach, or through courier partners
  such as Bluedart and FedEx. For a few remote locations where there is no other courier service available, we use India
  Post to deliver your orders.
</p>


  <b>2. Do you ship to international locations?</b><br>
  <p style="text-align: justify">
    We currently ship only within India and do not ship to any international location.
  </p>

  <b>3. How much time does it take for an order to be delivered?</b><br>
  <p style="text-align: justify">
    We deliver your order within 2-3 working days post-dispatch in A-1 and A-2 Metros (New Delhi, Mumbai, Kolkata,
    Bengaluru, Chennai, Pune, Ahmedabad and Hyderabad). For the rest of the cities, we deliver between 2-5 business
    days. Delivery by ground takes a little longer than air couriers. Ground-shipped orders are delivered to you between
    5-7 business days post-dispatch. Deliveries to very remote locations such as North East may take up to 7 business
    days or longer, depending on the location’s geographical constraints.
  </p>



  <b>4. How can I track my order?</b><br>
  <p style="text-align: justify">
    As soon as we ship your order, you receive an e-mail notification with a link to track your order. You can also
    access this information from your ‘my accounts’ section at the top right corner on our website
    <a href="http://www.healthkart.com">www.healthkart.com</a>. Online tracking usually goes live within 24-48 business hours post shipping.
  </p>


  <b>5. How long do you take to dispatch an order?</b><br>
  <p style="text-align: justify">
    The duration of dispatching an order depends on the type of order you have placed.
    In-stock items (Items that we stock in our 4 warehouses across India: Gurgaon, Mumbai,
    Bangalore and Delhi): These items are dispatched the same or the next day of order placement.
    JIT Items (Just-in-time Items): These items are procured from vendors on receiving your confirmation on an order.
    It takes 2-7 business days to dispatch JIT orders after you have placed an order. <br />
    Drop-ship Items: These items are shipped directly by our vendors to you. It takes 2-5 business days to dispatch drop-ship orders.  <br />
    We strive to dispatch your order as soon as possible. In case you have placed a mixed order of in- stock, JIT and
    drop-ship items, we sub-divide your order and dispatch it in parts, shipping the earliest available items first.  <br />
    At all steps of your order processing, we are in constant touch with you over phone, email and SMS to keep you updated.
  </p>

  <b>6. What are the shipping charges on healthkart.com?</b><br>
  <p style="text-align: justify">
    We deliver an order free of cost at your doorstep for all orders above Rs 500. We bear all the Octroi/sales tax and
    there is no additional shipping charge that you have to pay at the time of the delivery.  <br />
    However, if you order amount is less than Rs 500, a minimal shipping charge of Rs 50 is applicable.   <br />

    Note: Certain additional procedures like state-wise taxes may apply to your order depending upon you order type,
    the utility of your order, the order size and the state/region the order is being delivered to.
  </p>



</div>

</s:layout-component>
</s:layout-render>


<script type="text/javascript">
  window.onload = function() {
    document.getElementById('shippingP').style.fontWeight = "bold";
  };
</script>