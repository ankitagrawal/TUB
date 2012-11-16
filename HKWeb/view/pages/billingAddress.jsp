<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.constants.payment.EnumPaymentMode" %>
<%@ page import="com.hk.constants.core.Keys" %>
<%@ page import="com.hk.constants.payment.EnumPaymentMode" %>
<%@ page import="com.hk.constants.marketing.AnalyticsConstants" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.payment.EnumPaymentType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean
        beanclass="com.hk.web.action.core.order.OrderSummaryAction" event="pre"
        var="orderSummary"/>
<s:useActionBean beanclass="com.hk.web.action.core.payment.PaymentModeAction"
                 event="pre" var="paymentModeBean"/>


<s:layout-render name="/layouts/checkoutLayout.jsp"
                 pageTitle="Payment Options">
<s:layout-component name="htmlHead">
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/otherScripts/jquery.session.js"></script>
</s:layout-component>
<s:layout-component name="steps">
    <div class='steps'><s:link
            beanclass="com.hk.web.action.core.user.SelectAddressAction"
            style="margin-top: 0; margin-bottom: 0;">
        <div class='step prev_step' id="step1">
            <h2>Step 1</h2>

            <div class='small'>Select shipping address</div>
        </div>
    </s:link> <s:link beanclass="com.hk.web.action.core.order.OrderSummaryAction"
                      style="margin-top: 0; margin-bottom: 0;">
        <div class='step prev_step' id="step2">
            <h2>Step 2</h2>

            <div class='small'>Confirm your order</div>
        </div>
    </s:link>
        <div class='step current_step'>
            <h2>Step 3</h2>

            <div class='small'>Choose Payment Method</div>
        </div>
    </div>
</s:layout-component>


<s:layout-component name="steps_content">
<div class='current_step_content step3'>
<jsp:include
        page="/includes/checkoutNotice.jsp"/>

<div class='pre' align="center">
    <h4>Your total billable amount is <strong class='num'> <fmt:formatNumber
            value="${orderSummary.pricingDto.grandTotalPayable}" type="currency"
            currencySymbol="Rs "/> </strong></h4>
    <h6>If you have any trouble during the payment process, call our
        helpline number <strong class='red'> 0124 - 4551616 </strong></h6>
</div>

<div class="alert messages" style="font-size: 14px; color: red">
    <s:errors/><s:messages key="generalMessages"/></div>

    <%--<div style="border: 1px solid gray; padding: 10px; background-color: darkred; color: white; text-align: center; font-size: 1.3em;">
      Our payment gateway is experiencing techincal problems. Please try cash on delivery. <br/>
      We are working on getting the payment gateway back up asap.
    </div>--%>

<div class='payment_container'>

<%--<div style="visibility: hidden;" class="offer-banner"><img
                    src="<hk:vhostImage/>/images/banners/pay_online_banner.jpg">
                </div>--%>
<div style="display: none;"><s:link
        beanclass="com.hk.web.action.admin.SetInCookieAction"
        id="setInCookieLink"/></div>
<div class='outer'>
<div class='left_controls tabs' style="width:110px">
    <ul>
    </ul>
</div>
<div class='right_content'>
<div id="tabs_content7" class="tab_content" style="display: none;">
    <h3>
        Enter your Billing Address
    </h3> <br>
    <s:form beanclass="com.hk.web.action.core.user.BillingAddressAction" id="newAddressForm"
            onsubmit="return validateForm()" method="post" name="BillingAddressForm">
        <s:hidden name="order" value="${orderSummary.order.id}"/>
        <s:hidden name="bankId" value="70"/>
        <s:hidden name="paymentMode" value="100"/>
        <s:hidden name="billingAddress.id"/>
        <span class="aster special">(Fields marked * are required.)</span>

        <div class='label'>Name<span class="aster">*</span></div>
        <s:text name="billingAddress.name" value="${paymentModeBean.billingAddress.name}"/>
        <div class='label'>Address Line 1<span class="aster">*</span></div>
        <s:text name="billingAddress.line1" value="${paymentModeBean.billingAddress.line1}"/>
        <div class='label'>Address Line 2</div>
        <s:text name="billingAddress.line2" value="${paymentModeBean.billingAddress.line2}"/>
        <div class='label'>City<span class="aster">*</span></div>
        <s:text name="billingAddress.city" value="${paymentModeBean.billingAddress.city}"/>
        <div class='label'>State<span class="aster">*</span></div>
        <s:text name="billingAddress.state" value="${paymentModeBean.billingAddress.state}"/>
        <%--<s:text name="address.state"/>--%>
        <div class='label'>PIN Code / ZIP Code<span class="aster">*</span></div>
        <s:text name="billingAddress.pin" class="pincode" value="${paymentModeBean.billingAddress.pin}"/>
        <div class='label'>Phone / Mobile<span class="aster">*</span></div>
        <s:text name="billingAddress.phone" value="${paymentModeBean.billingAddress.phone}"/>
        <table>  <tr>
        <td>  <s:submit name="save" value="Save the address and continue"/>  </td>
        <td>  <s:link beanclass="com.hk.web.action.core.payment.PaymentModeAction" class="notifyMe button_orange" style="background:green"> Cancel </s:link> </td>
        </tr> </table>
    </s:form>
</div>

<c:set var="url" value="${pageContext.request.contextPath}/core/user/BillingAddress.action"/>

<%--<c:set var="url" value="${pageContext.request.contextPath}/core/user/SelectAddress.action"/>--%>
<script type="text/javascript">
    $(document).ready(function() {

           $('.tab_content').first().show();

        $('#paypal').change(function() {
            if ($(this).is(':checked')) {
                location.href = '${url}';
            }
        });

    });

    function validateForm()
    {
//        var numbers = /^[0-9]+$/;
        var billingAddressName = document.forms["BillingAddressForm"]["billingAddress.name"].value;
        var billingAddressLine1 = document.forms["BillingAddressForm"]["billingAddress.line1"].value;
        var billingAddressCity = document.forms["BillingAddressForm"]["billingAddress.city"].value;
        var billingAddressState = document.forms["BillingAddressForm"]["billingAddress.state"].value;
        var billingAddressPin = document.forms["BillingAddressForm"]["billingAddress.pin"].value;
        var billingAddressPhone = document.forms["BillingAddressForm"]["billingAddress.phone"].value;
        if (billingAddressName == null || billingAddressName == "")
        {
            alert("Name must be filled out");
            return false;
        }
        if (billingAddressLine1 == null || billingAddressLine1 == "")
        {
            alert("Address Line 1 must be filled out");
            return false;
        }
        if (billingAddressCity == null || billingAddressCity == "")
        {
            alert("City must be filled out");
            return false;
        }
        if (billingAddressState == null || billingAddressState == "")
        {
            alert("State must be filled out");
            return false;
        }
        if (billingAddressPin == null || billingAddressPin == "")
        {
            alert("Pincode must be filled out");
            return false;
        }
//          if (!(billingAddressPin.match(numbers)))
//        {
//            alert("Pincode must be Numeric only");
//            return false;
//        }
        if (billingAddressPhone == null || billingAddressPhone == "")
        {
            alert("Phone/Mobile must be filled out");
            return false;
        }
    }
</script>
<div class='floftfix'></div>

</div>

</div>
</div>
</s:layout-component>

<s:layout-component name="analytics">
    <jsp:include page="/includes/_analytics.jsp"/>
</s:layout-component>

<s:layout-component name="zopim">
    <jsp:include page="/includes/_zopim.jsp"/>
</s:layout-component>

</s:layout-render>

