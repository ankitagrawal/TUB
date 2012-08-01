<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.constants.payment.EnumPaymentMode" %>
<%@ page import="com.hk.constants.core.Keys" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<%
  Double codMaxAmount = Double.parseDouble((String)ServiceLocatorFactory.getProperty(Keys.Env.codMaxAmount));
  Double codMinAmount = Double.parseDouble((String)ServiceLocatorFactory.getProperty(Keys.Env.codMinAmount));
  Double codCharges = Double.parseDouble((String)ServiceLocatorFactory.getProperty(Keys.Env.codCharges));
%>
<c:set var="codMaxAmount" value="<%=codMaxAmount%>"/>
<c:set var="codMinAmount" value="<%=codMinAmount%>"/>
<c:set var="codCharges" value="<%=codCharges%>"/>
<c:set var="orderDate" value="<%=new DateTime().toDate()%>"/>


<s:layout-render name="/layouts/checkoutLayout.jsp" pageTitle="Order Summary">
  <s:layout-component name="htmlHead">


  </s:layout-component>
  <s:layout-component name="steps">
    <div class='steps'>
      <s:link beanclass="com.hk.web.action.core.user.SelectAddressAction" style="margin-top: 0; margin-bottom: 0;">
        <div class='step prev_step'>
          <h2>Step 1</h2>

          <div class='small'>
            Select shipping address
          </div>
        </div>
      </s:link>

      <div class='step current_step'>
        <h2>Step 2</h2>

        <div class='small'>
          Confirm your order
        </div>
      </div>
      <div class='step'>
        <h2>Step 3</h2>

        <div class='small'>
          Choose Payment Method
        </div>
      </div>
    </div>
  </s:layout-component>

  <s:layout-component name="steps_content">
    <s:useActionBean beanclass="com.hk.web.action.core.order.OrderSummaryAction" event="pre" var="orderSummary"/>
    <div class='current_step_content step2'>

    <jsp:include page="/includes/checkoutNotice.jsp"/>
      <c:choose>
          <c:when test="${(orderSummary.hideCod == true)}">
                    <div align="center" style="color:red; font-size:1.2em;">Your order contain Ground shipping item. We suggest you to choose pre-payment option and you will get this much amount save<br>
                        Delivery may take 5-7 days</div>
          </c:when>

           <c:when test="${orderSummary.availableCourierList == null}">
                <div align="center" style="color:red; font-size:1.2em;">This pincode is serviced only through Speed Post. Delivery may take 5-7 days</div>
           </c:when>

      </c:choose>
      
    <%--<c:if test="${orderSummary.availableCourierList == null}">--%>
      <%--<div align="center" style="color:red; font-size:1.2em;">This pincode is serviced only through Speed Post. Delivery may take 5-7 days</div>--%>
    <%--</c:if>--%>

    <h3>
      You selected
    </h3>
    <s:layout-render name="/layouts/embed/orderSummaryTableDetailed.jsp" pricingDto="${orderSummary.pricingDto}" orderDate="${orderDate}"/>
    <script type="text/javascript">

      $('.cartButton').html("<img class='icon' src='${pageContext.request.contextPath}/images/icons/cart.png'/><span class='num' id='productsInCart'>${orderSummary.pricingDto.productLineCount}</span> item in<br/>your shopping cart");

    </script>
    <div class='right_container address_box'>
      <div class='title'>
        <h5>
          To be shipped to
        </h5>
      </div>
      <div class='detail'>
        <div class='name'>
            ${orderSummary.order.address.name}
        </div>
        <div class='address'>
            ${orderSummary.order.address.line1}
          <c:if test="${hk:isNotBlank(orderSummary.order.address.line2)}">
            <br/>
            ${orderSummary.order.address.line2}
          </c:if>
          <br/>
            ${orderSummary.order.address.city}
          <br/>
            ${orderSummary.order.address.state}
          <br/>
            ${orderSummary.order.address.pin}
          <br/>
            ${orderSummary.order.address.phone}
        </div>
      </div>
        <span class="small">
          <s:link beanclass="com.hk.web.action.core.user.SelectAddressAction" style="color: #888; float: right;">
            (change) </s:link>
        </span>
    </div>
    <c:if test="${orderSummary.redeemableRewardPoints > 0}">
      <div class="right_container" style="left: 40px;">
        <div class="title">
          <h5>
            REDEEM REWARD POINTS
          </h5>

          <p>
            <c:choose>
              <c:when test="${orderSummary.useRewardPoints}">
                You are using your reward points for payment.<br/>
                You have
                <strong><fmt:formatNumber value="${orderSummary.redeemableRewardPoints}" type="currency" currencySymbol=" "/></strong> reward points.<br/>
                Reward points left after this order =
                <strong><fmt:formatNumber value="${orderSummary.redeemableRewardPoints - orderSummary.pricingDto.redeemedRewardPoints}" type="currency" currencySymbol=" "/></strong><br/>
                <s:link beanclass="com.hk.web.action.core.order.OrderSummaryAction" event="pre">
                  <s:param name="useRewardPoints" value="false"/>
                  <span>Don't Redeem Points</span>
                </s:link>
              </c:when>
              <c:otherwise>
                You have
                <strong><fmt:formatNumber value="${orderSummary.redeemableRewardPoints}" type="currency" currencySymbol=" "/></strong> reward points.<br/>
                You can use these points to pay for your order.<br/>
                <s:link beanclass="com.hk.web.action.core.order.OrderSummaryAction" event="pre">
                  <s:param name="useRewardPoints" value="true"/>
                  <span>Redeem Points</span>
                </s:link>
              </c:otherwise>
            </c:choose>
          </p>
        </div>
      </div>
    </c:if>
    <div class="right_container" style="left: 40px;">
      <div class="title">
        <h5>
          Instructions if any (e.g Preferred Delivery Time/Flavour Needed)
        </h5>
        <s:form beanclass="com.hk.web.action.core.order.OrderSummaryAction" method="post">
        <s:hidden name="order" value="${orderSummary.order.id}"/>
        <s:textarea name="order.userComments" rows="2" cols="20" style="width:175px;height:110px"/>
          <div class="title">
            <h5>
              Confirm order
            </h5>
          </div>
          <div style="margin:10px; padding-top:10px">
            <div class="buttons">
              <s:submit name="orderReviewed" value="Make Payment"/>
            </div>
          </div>
      </div>
      </s:form>
    </div>
  </s:layout-component>
</s:layout-render>
