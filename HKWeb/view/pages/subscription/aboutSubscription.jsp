<%--
  Created by IntelliJ IDEA.
  User: Pradeep
  Date: 7/25/12
  Time: 11:19 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.referral.ReferralManagerAction" var="couponBean" event="pre"/>
<s:layout-render name="/layouts/default.jsp" pageTitle="Referral Program">

    <s:layout-component name="htmlHead">

    </s:layout-component>
    <s:layout-component name="heading">Subscriptions</s:layout-component>
    <s:layout-component name="lhsContent">

    </s:layout-component>
    <s:layout-component name="rhsContent">
        <ul>

            <li>
                The subscription may vary from minimum 180 days to 450 days
            </li>
            Benefits of subscription:

            <li>
                Additional discounts upto 10 %.
            </li>
            <li>
                It also provides an advantage of Price Protection i.e. as per the shipping date, if prices are higher, he/ she still gets it at same price and if prices are lower, the balance would be reimbursed.
            </li>
            <li>
                Additional freebies & samples available
            </li>
            <li>
                A confirmation or reminder call will be given for the safe delivery of your products.
            </li>
            <li>
                A customer is free to unsubscribe anytime. If a customer opts out, the discounts would be taken away and balance amount would be credited as reward points.
            </li>
        </ul>


    </s:layout-component>

</s:layout-render>

</script>