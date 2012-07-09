<%@ page import="com.hk.web.HealthkartResponse" %>
<%--
  Created by IntelliJ IDEA.
  User: Pradeep
  Date: 7/4/12
  Time: 12:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.subscription.SubscriptionUpdateAction" var="sua" event="pre"/>
<c:set var="productVariant" value="${sua.subscription.productVariant}"/>
<c:set var="product" value="${sua.subscription.productVariant.product}"/>
<c:set var="sp" value="${sua.subscriptionProduct}"/>
<c:set var="subscription" value="${sua.subscription}" />
<s:layout-render name="/layouts/modal.jsp">
  <s:layout-component name="heading">
    Subscription for ${product.name}
  </s:layout-component>

  <s:layout-component name="content">
    <div  id="subscription-container">

      <div >
        <s:form beanclass="com.hk.web.action.core.subscription.SubscriptionUpdateAction" event="save" class="editSubscriptionForm">
          <fieldset>
            <s:hidden name="subscription" value="${subscription.id}" id="subscriptionId"/>
            min frequency: ${sp.minFrequencyDays} days - max frequency: ${sp.maxFrequencyDays} days
            <br/>

            Start Date: <s:text  name="subscription.startDate" value="${subscription.startDate}" id="subscriptionStartDate" />
            <br/>
            subscription period(days): <s:text name="subscription.subscriptionPeriodDays" value="${subscription.subscriptionPeriodDays}" id="subscriptionPeriod"/>
            <br/>
            frequency(days): <s:text name="subscription.frequencyDays" value="${subscription.frequencyDays}" id="subscriptionFrequency" />
            <br/>
            <s:hidden name="subscription.productVariant" value="${productVariant.id}"/>
            total qty:<s:text class="qty" name="subscription.qty" value="${subscription.qty}" id="subscriptionQty"/>
            <br/>
            qty per delivery:<s:text class="qtyPerDelivery" name="subscription.qtyPerDelivery"  id="subscriptionQtyPerDelivery"/>
            <s:submit name="saveSubscription" value="Save" onclick="closeSubscriptionWindow();" />
          </fieldset>
        </s:form>
      </div>
      <span >Subscribe and save <fmt:formatNumber value="${sp.subscriptionDiscount180Days}" maxFractionDigits="2"/>  to   <fmt:formatNumber value="${sp.subscriptionDiscount360Days}" maxFractionDigits="2"/> &#37;   </span>

      <script type="text/javascript">
        function _addSubscription(res) {
          if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
            $('.message .line1').html("<strong>" + res.data.name + "</strong> has been added to your shopping cart");
            $('.cartButton').html("<img class='icon' src='${pageContext.request.contextPath}/images/icons/cart.png'/><span class='num' id='productsInCart'>" + res.data.itemsInCart + "</span> items in<br/>your shopping cart");
            $('.progressLoader').hide();

            show_sub_message();
          }
          $('#gulal').show();
        }
        function show_sub_message() {
          $('.message').css("top", "70px");
          $('.message').animate({
            opacity: 1
          }, 500);
        }
        $('.editSubscriptionForm').ajaxForm({dataType: 'json', success: _addSubscription});

        $( "#subscriptionStartDate" ).datepicker({ minDate: -20, maxDate: "+1M +10D" });
        function closeSubscriptionWindow(){
          $("#subscriptionWindow").jqmHide();
        }
      </script>
    </div>


  </s:layout-component>

</s:layout-render>
